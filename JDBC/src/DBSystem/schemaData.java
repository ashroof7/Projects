package DBSystem;

import java.io.IOException;

public interface schemaData {

	public String[] getTablesNames();

	public String[] getTableParameters(int i);

	public String[] getTableParTypes(int i);

	public void Read() throws Exception;

	public void write() throws IOException;

	boolean deleteTable(String name);
	public void setData(String UserName ,String UserPassword) throws IOException;
	
	boolean addTable(String name, String[] colName, String[] colType);
	String getUserName();
	String getPassword();
}
