package DBSystem;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * @author Database Project CSED 2014<address @ example.com>
 * @version 1.0 (the version of the package this class was first added to)
 * @since 2011-12-4 (a date or the version number of this program)
 */

public interface Writer {

	/**
	 * write an array list of entry of a table to its file
	 * 
	 * @param ArrayList
	 *            <Entry> values , String name -table name - , ArrayList<String>
	 *            types (type of each variable)
	 * @return void
	 * @throws FileNotFoundException
	 * @throws ParserConfigurationException
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 * @throws IOException 
	 */

	public void writeTable(ArrayList<Entry> values, String[] types)
			throws FileNotFoundException, ParserConfigurationException,
			TransformerConfigurationException, TransformerException, IOException;

}
