package DBSystem;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * @author Database Project CSED 2014<address @ example.com>
 * @version 1.0 (the version of the package this class was first added to)
 * @since 2011-12-4 (a date or the version number of this program)
 */
public interface Table {
	/**
	 * 
	 * @return the number of entries in the table
	 */
	public int size();

	/**
	 * @return column count
	 */
	public int getColumnCount();

	/**
	 * Add a new Entry to the table.
	 * 
	 * @param Entry
	 *            e
	 * @return void.
	 * @throws FileNotFoundException
	 */
	public void addEntry(Entry e) throws FileNotFoundException;

	/**
	 * Delete an entry form the table .
	 * 
	 * @param int index
	 * @return void
	 */
	public void deleteEntry(int index);

	/**
	 * get the types of the table columns
	 * 
	 * @return
	 */
	public String[] getTypes();

	/**
	 * updates the current table file
	 * 
	 * @throws FileNotFoundException
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws TransformerConfigurationException
	 * @throws IOException 
	 */
	public void update() throws FileNotFoundException,
			TransformerConfigurationException, ParserConfigurationException,
			TransformerException, IOException;

	/**
	 * get the column names
	 * 
	 * @return
	 */
	public String[] getColumnNames();

	/**
	 * @return column index
	 */
	public int getColumnIndex(String columnName);

	/**
	 * @return the table name
	 */
	public String getName();

	/**
	 * clear current table
	 */
	public void clear();

	/**
	 * returns the current entry
	 */
	public Entry getEntry(int i);
}
