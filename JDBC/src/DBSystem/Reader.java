package DBSystem;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * @author Database Project CSED 2014<address @ example.com>
 * @version 1.0 (the version of the package this class was first added to)
 * @since 2011-12-4 (a date or the version number of this program)
 */
public interface Reader {
	/**
	 * Load a table from file
	 * 
	 * @param String
	 *            URL
	 * @return ArrayList <Entry>
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */

	public ArrayList<Entry> readTable(String URL, String n, int colNum,
			String[] colNames, String[] colTypes)

	throws Exception, IOException, ParserConfigurationException;

}
