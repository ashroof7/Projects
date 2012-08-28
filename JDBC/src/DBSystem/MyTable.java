package DBSystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

public class MyTable implements Table {

    private ArrayList<Entry> table;
    private String[] columnNames;
    private String[] types;
    private String url;
    private String name;
    private int columnCount;
    public static final String DEF_VALUE = "null";

    public MyTable(String name, String url, int columnCount, String[] types,
            String[] names) throws Exception {
        Reader r = new XMLReader();
        table = new ArrayList<Entry>();
        columnNames = new String[columnCount];
        this.url = url;
        this.columnCount = columnCount;
        this.types = types;
        this.columnNames = names;
        this.name = name;
        try {
            table = r.readTable(MyDatabase.path, name, columnCount, columnNames, this.types);
        } catch (Exception e) {

            
            
            
        }
    }

    @Override
    public void addEntry(Entry e) throws FileNotFoundException {
        table.add(e);
    }

    @Override
    public void deleteEntry(int index) {
        table.remove(index);
    }

    @Override
    public String[] getTypes() {
        return types;
    }

    @Override
    public void update() throws TransformerConfigurationException, ParserConfigurationException,
            TransformerException, IOException {
        Writer writer = new XMLWriter(url, name);
        writer.writeTable(table, columnNames);
    }

    @Override
    public int size() {
        return table.size();
    }

    @Override
    public String[] getColumnNames() {
        return columnNames;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    /**
     * IMPLEMENTATION SHOULD BE REPLACED BY HASHMAP FOR BETTER PERFORMANCE
     */
    public int getColumnIndex(String columnName) {
        int i = 0;
        for (i = 0; i < columnNames.length
                && !columnName.equals(columnNames[i]); i++)
			;
        return (i < columnCount) ? i : -1;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void clear() {
        table.clear();
    }

    @Override
    public Entry getEntry(int i) {
        return table.get(i);
    }
}
