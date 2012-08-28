/**
 * 
 */
package queryProcessor;

/**
 * @author Saleh
 *
 */
public interface Schema {
	
	//return true if the table is in the database, false otherwise
	public   boolean tableExist(String table);

	//return true if a column is in a specific table, false otherwise
	public  boolean columnExist(String column, String table);
	
	// return the type of the column
	public  String getType(String column, String table);
	
	//returns the no of columns in a table
	public  int countColumns(String table);
	
}
