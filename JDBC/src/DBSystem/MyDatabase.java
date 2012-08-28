package DBSystem;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class MyDatabase implements Database {

    private schemaData dbschema;
    private String[] tableNames;
    private static MyDatabase databaseConnection;
    public static String path = "";
    public static String schemaFileName = "schema";
    private Table active;
    private String dburl;
    private static schemaData schema;
    private Logger loggy;

    private MyDatabase(String URLs, String fileName, schemaData schema) {
        dburl = URLs;
        dbschema = schema;
        tableNames = dbschema.getTablesNames();
        loggy = Log.getLogger();
    }

    @Override
    public void openTable(String name) throws Exception {
        if (active == null || !active.getName().equals(name)) {
            boolean flag = false;

            for (int i = 0; i < tableNames.length; i++) {
                if (name.equals(tableNames[i])) {
                    String[] names = dbschema.getTableParameters(i);
                    String[] types = dbschema.getTableParTypes(i);
                    active = new MyTable(name, dburl, names.length, types,
                            names);
                    flag = true;
                }
            }
            if (!flag) {
                loggy.error("Table \"" + name + "\" not found");
                throw new Exception("Table not found!");
            }
            loggy.info("Table \"" + name + "\" opened successfully");
        }
    }

    @Override
    public int dropTable(String tableName) throws Exception {
        boolean flag = false;
        int out = 0;
        loggy.info("Trying to drop table \"" + tableName + "\"");
        for (int i = 0; i < tableNames.length && !flag; i++) {
            if (tableNames[i].equals(tableName)) {
                flag = true;
            }
        }
        if (flag) {
            if (active != null && active.getName().equals(tableName)) {
                out = active.size();
                active = null;
            }
            dbschema.deleteTable(tableName);
            dbschema.write();
            dbschema.Read();
            tableNames = dbschema.getTablesNames();
            File f = new File(dburl + tableName + ".xml");
            if (!f.delete()) {
                Writer e = new XMLWriter(dburl, tableName);
                e.writeTable(new ArrayList<Entry>(), new String[0]);
                f.delete();
            }
            loggy.info("Table \"" + tableName + "\" dropped successfully");
        } else {
            loggy.error("Table \"" + tableName + "\" not found to drop");
            throw new Exception("No Table to drop");
        }
        return out;
    }

    @Override
    public void createTable(String name, String[] columns, String[] types)
            throws Exception {
        loggy.info("Trying to create table \"" + name + "\"");
        for (int i = 0; i < tableNames.length; i++) {
            if (tableNames[i].equals(name)) {
                loggy.error("Table \"" + name
                        + "\" actually exists, cannot create another one");
                throw new Exception("Duplicate Table name!");
            }
        }
        dbschema.addTable(name, columns, types);
        Table t = new MyTable(name, dburl, columns.length, types, columns);
        t.update();
        dbschema.addTable(name, columns, types);
        dbschema.write();
        dbschema.Read();
        tableNames = dbschema.getTablesNames();
        loggy.info("Table \"" + name + "\" created successfully");
    }

    @Override
    public void insertIntoTable(String name, int[] columns, String[] values)
            throws Exception {
        boolean found = false;
        loggy.info("Trying to insert into table \"" + name + "\"");
        for (int i = 0; i < tableNames.length && !found; i++) {
            if (tableNames[i].equals(name)) {
                found = true;
                openTable(name);
                String[] res = new String[dbschema.getTableParameters(i).length];
                Arrays.fill(res, "null");
                for (int j = 0; j < columns.length; j++) {
                    res[columns[j]] = values[j];
                }
                Entry e = new MyEntry(res);
                active.addEntry(e);
                active.update();
            }
        }
        if (!found) {
            loggy.error("Table \"" + name + "\" could not be found");
            throw new Exception("Table not found");
        }
        loggy.info("Insertion successful in table \"" + name + "\"");
    }

    @Override
    public ResultTable selectFromTable(String name, int[] columns,

            String[] condition) throws Exception {
        loggy.info("Trying to select from table \"" + name + "\"");
        openTable(name);
        ArrayList<Entry> res = new ArrayList<Entry>();
        String[] names = new String[columns.length];
        String[] types = new String[columns.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = active.getColumnNames()[columns[i]];
            types[i] = active.getTypes()[columns[i]];
        }
        for (int i = 0; i < active.size(); i++) {
            if (condition(condition, active.getEntry(i))) {// Tests condition
                // here
                String[] arr = new String[columns.length];
                for (int j = 0; j < columns.length; j++) {
                    arr[j] = active.getEntry(i).getValues()[columns[j]];
                }
                res.add(new MyEntry(arr));
            }
        }
        loggy.info("Selection completed through table \"" + name + "\"");
        return new MyResultTable(res, types, names, active.getName());
    }

    public int removeFromTable(String name, String[] condition)
            throws Exception {
        int count = 0;
        loggy.info("Trying to remove from table \"" + name + "\"");
        openTable(name);
        for (int i = 0; i < active.size(); i++) {
            if (condition(condition, active.getEntry(i))) {// Tests condition
                // here
                count++;
                active.deleteEntry(i);
                i--;// cause i will be incremented ;)
            }
        }
        active.update();
        loggy.info("Removing from table \"" + name
                + "\" completed successfully");
        return count;
    }

    public static Database connect(String username, String password)
            throws NoSuchAlgorithmException, IOException, Exception {
        Logger loggy2 = Log.getLogger();

        if (schema == null) {
            loggy2.info("Trying to connect to database");
            schema = new schemaFile(path, schemaFileName);
            schema.setData(hash(username), hash(password));
        }
        if (databaseConnection == null) {
            try {
                schema.Read();
            } catch (Exception e) { // An Empty schemafile
                loggy2.warn("Schema file not found, Creating new database");
                return databaseConnection = new MyDatabase(path,
                        schemaFileName, schema);
            }
            if (schema.getUserName().equals(hash(username))
                    && schema.getPassword().equals(hash(password))) {
                loggy2.info("Connection to database established");
                return databaseConnection = new MyDatabase(path,
                        schemaFileName, schema);
            } else {
                loggy2.error("Connection to the database failed");
                return null;
            }
        }
        return databaseConnection;
    }

    @Override
    public String[] getColumnNames() {
        return active.getColumnNames();
    }

    @Override
    public String[] getColumnTypes() {
        return active.getTypes();
    }

    @Override
    public int setTable(String name, int[] columns, String[] values,
            String[] condition) throws Exception {
        int count = 0;
        loggy.info("Trying to update table \"" + name + "\"");
        openTable(name);
        for (int i = 0; i < active.size(); i++) {
            if (condition(condition, active.getEntry(i))) {// check
                // condition
                // here
                count++;
                for (int j = 0; j < columns.length; j++) {
                    active.getEntry(i).getValues()[columns[j]] = values[j];
                }
            }
        }
        active.update();
        loggy.info("Table \"" + name + "\" updated successfully");
        return count;
    }

    public int compare(String a, String b, String type) {
        if (a.equals("null") || b.equals("null")) {
            return Integer.MIN_VALUE;
        }
        if (type.equals("integer")) {
            Integer aa = Integer.parseInt(a);
            Integer bb = Integer.parseInt(b);
            return aa.compareTo(bb);
        } else if (type.equals("double")) {
            Double aa = Double.parseDouble(a);
            Double bb = Double.parseDouble(b);
            return aa.compareTo(bb);
        } else {
            return a.compareTo(b);
        }
    }

    @Override
    public boolean condition(String[] condition, Entry e) {
        if (condition == null) {
            return true;
        }

        for (int i = 0; i < active.getColumnNames().length; i++) {
            if (condition[0].equals(active.getColumnNames()[i])) {
                String x = e.getValues()[i];
                int y = compare(x, condition[2], active.getTypes()[i]);
                if (y == 0
                        && (condition[1].equals("=")
                        || condition[1].equals(">=") || condition[1].equals("<="))) {
                    return true;
                } else if (y > 0
                        && (condition[1].equals(">=")
                        || condition[1].equals(">") || condition[1].equals("<>"))) {
                    return true;
                } else if (y < 0
                        && y != Integer.MIN_VALUE
                        && (condition[1].equals("<=")
                        || condition[1].equals("<") || condition[1].equals("<>"))) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean checkPassword(String userName, String pw) throws Exception {
        if (schema == null) {
            schema = new schemaFile(path, schemaFileName);
            try {
                schema.Read();
            } catch (Exception e) {
                try {
                    schema.setData(hash(userName), hash(pw));
                } catch (Exception e1) {
                    throw new Exception("Invalid Schema ");
                }
                return true;
            }

        }
        try {
            return schema.getUserName().equals(hash(userName))
                    && schema.getPassword().equals(hash(pw));
        } catch (Exception e) {
           
        }
        return false;
    }

    public void setProfile(String user, String pass) throws IOException,
            NoSuchAlgorithmException {
        schema.setData(hash(user), hash(pass));
    }

    public static String hash(String s) throws NoSuchAlgorithmException {
        MessageDigest enc = MessageDigest.getInstance("MD5");
        byte[] res = enc.digest(s.getBytes());
        String out = "";
        for (int i = 0; i < res.length; i++) {
            out += (char) ((((int) res[i] + 104651) % 61) + '@');
        }
        return out;
    }
}
