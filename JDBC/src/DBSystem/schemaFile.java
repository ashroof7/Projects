package DBSystem;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class schemaFile implements schemaData {

    private String[] names;
    private String[][] type;
    private String[][] columnName;
    private String URL;
    private String schemaFileName;
    private static String userName;
    private static String password;

    static {
        try {
            userName = MyDatabase.hash("leonardo");
            password = MyDatabase.hash("leo");
        } catch (Exception e) {
        }
    }

    public schemaFile(String URLs, String fileName) throws Exception {
        URL = URLs;
        schemaFileName = fileName;
        names = new String[0];
        type = new String[0][];
        columnName = new String[0][];
        try {
            userName = MyDatabase.hash("");
            password = MyDatabase.hash("");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new Exception("Databas Error, check your Schema File");
        }

    }

    @Override
    public String[] getTablesNames() {
        return names;
    }

    @Override
    public String[] getTableParameters(int i) {
        if (i < columnName.length) {
            return columnName[i];
        }
        return null;
    }

    @Override
    public String[] getTableParTypes(int i) {
        if (i < type.length) {
            return type[i];
        }
        return null;
    }

    @Override
    public void Read() throws Exception {
        schema e = new XMLSchema(URL, schemaFileName);
        if (e.read()) {
            names = e.getTableNames();
            type = e.getColTypes();
            columnName = e.getColNames();
            userName = e.getUser();
            password = e.getPassword();
        }
    }

    @Override
    public void write() throws IOException {
        schema e = new XMLSchema(URL, schemaFileName);
        e.set(names, columnName, type);

        e.write(userName, password);
    }

    @Override
    public boolean addTable(String name, String[] colName, String[] colType) {

        if (colName.length != colType.length || colName.length == 0) {
            return false;
        }
        int i = colName.length;
        if (i != colType.length || i == 0) {
            return false;
        }
        i = names.length;
        for (int k = 0; k < i; k++) {

            if (name.equals(names[k])) {
                return false;
            }

        }

        String[] temp = new String[i + 1];

        for (int k = 0; k < i; k++) {
            temp[k] = names[k];
        }

        temp[i] = name;
        names = temp;

        String[][] temp2 = new String[i + 1][];
        for (int k = 0; k < i; k++) {
            temp2[k] = columnName[k];
        }

        temp2[i] = colName;

        columnName = temp2;
        temp2 = new String[i + 1][];

        for (int k = 0; k < i; k++) {
            temp2[k] = type[k];
        }

        temp2[i] = colType;
        type = temp2;

        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean deleteTable(String name) {
        // TODO Auto-generated method stub
        int length = names.length;
        int index = -1;
        for (int i = 0; i < length; i++) {
            if (name.equals(names[i])) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;
        }

        for (int i = index; i < length - 1; i++) {
            names[i] = names[i + 1];
            type[i] = type[i + 1];
            columnName[i] = columnName[i + 1];
        }
        names = Arrays.copyOf(names, length - 1);
        type = Arrays.copyOf(type, length - 1);
        columnName = Arrays.copyOf(columnName, length - 1);
        return true;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setData(String UserName, String UserPassword)
            throws IOException {
        try {
            userName = UserName;
            password = UserPassword;
        } catch (Exception e) {
            throw new IOException ("Invalid username/password");
        }
        this.write();
    }
}
