package DBSystem;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Database Project CSED 2014<address @ example.com>
 * @version 1.0 (the version of the package this class was first added to)
 * @since 2011-12-4 (a date or the version number of this program)
 */
public interface Database {
	/**
	 * load a Table from the schema file
	 * 
	 * @param String
	 *            name , table name
	 * @return Table
	 * @throws Exception
	 */
	public void openTable(String name) throws Exception;

	/**
	 * Delete a table from the database, details about it in the schema file ,
	 * data stored in it will all be deleted
	 * 
	 * @param String
	 *            name , table name
	 * @return boolean , true if these table exists , else false
	 * @throws Exception
	 */
	public int dropTable(String name) throws Exception;

	/**
	 * Insert into the specified table
	 * 
	 * @param name
	 * @param columns
	 * @param values
	 * @throws Exception
	 */
	public void insertIntoTable(String name, int[] columns, String[] values)
			throws Exception;

	/**
	 * Selects from the specified table
	 * 
	 * @param name
	 * @param columns
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public ResultTable selectFromTable(String name, int[] columns,
			String[] condition) throws Exception;

	/**
	 * inserts variable into table
	 * 
	 * @throws Exception
	 */

	public void createTable(String name, String[] columns, String[] types)
			throws Exception;

	/**
	 * @return
	 */
	public String[] getColumnNames();

	/**
	 * 
	 * @return
	 */
	public String[] getColumnTypes();

	/**
	 * @throws Exception
	 * 
	 */
	public int setTable(String name, int[] columns, String[] values,
			String[] condition) throws Exception;

	public int removeFromTable(String name, String[] condition)
			throws Exception;

	public boolean condition(String[] condition, Entry e);
public void setProfile(String user , String pass) throws IOException, NoSuchAlgorithmException;

}
