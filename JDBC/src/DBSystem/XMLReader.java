package DBSystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLReader implements Reader {

	@Override
	public ArrayList<Entry> readTable(String URL, String n, int colNum,

	String[] colNames, String[] colTypes)

	throws SAXException, IOException, ParserConfigurationException {
		final ArrayList<Entry> table = new ArrayList<Entry>();
		final String url = URL;
		final String name = n;
		final int colNums = colNum;
		final String[] ColNames = Arrays.copyOf(colNames, colNums);
		final String[] ColTypes = Arrays.copyOf(colTypes, colNums);

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		DefaultHandler handler = new DefaultHandler() {

			String values[] = new String[colNums];
			int index = 0;
			boolean found[] = new boolean[colNums];

			public void startElement(String uri, String localName,

			String qName, Attributes attributes) throws SAXException {
				if (qName.equalsIgnoreCase(name)) {
					index = 0;
					found = new boolean[colNums];
				} else {
					boolean detected = false;
					for (int i = 0; i < colNums; i++) {
						if (!found[i] && qName.equals(ColNames[i])) {
							detected = true;
							index = i;
							found[i] = true;
							break;
						}
					}
					if (!detected && !qName.equals(name + "Table")) {
						throw new SAXException("Column name is not valid");
					}
				}
			}

			public void endElement(String uri, String localName, String qName)

			throws SAXException {
				if (qName.equalsIgnoreCase(name)) {
					MyEntry entry = new MyEntry(values);
					table.add(entry);
					values = new String[colNums];
					for (int i = 0; i < colNums; i++) {
						if (!found[i]) {
							throw new SAXException("XML File Reader Error!");
						}
					}
				}
			}

			public void characters(char ch[], int start, int length)

			throws SAXException {
				String z = new String(ch, start, length);

				if (isParsable(z, ColTypes[index])) {
					values[index] = z;
				} else {
					throw new SAXException("Data Corrupted");
				}
			}

		};
                
                FileInputStream file = new FileInputStream(url+ name + ".xml");
                		saxParser.parse(file, handler);
		file.close();
		return table;
	}

	public static boolean isParsable(String value, String type) {

		if (value.equals(null)) {
			return true;
		}
		if (type.equals("integer")) {
			try {
				Integer.parseInt(value);
			} catch (Exception e) {
				return false;
			}
		} else {
			if (type.equals("double")) {
				try {
					Double.parseDouble(value);
				} catch (Exception e) {
					return false;
				}
			} else {
				if (type.equals("long")) {
					try {
						Long.parseLong(value);
					} catch (Exception e) {
						return false;
					}
				} else {
					if (type.equals("boolean")) {
						if (!(value.equals("true") || value.equals("false"))) {
							return false;
						}
					} else {
						if (type.length() > 7
								&& type.substring(0, 7).equals("varchar")) {
							type = type.substring(7, type.length());
							type = type.replaceFirst("\\(", "");
							type = type.replaceFirst("\\)", "");
							try {
								int i = Integer.parseInt(type.trim());
								if (i < value.length()) {
									return false;
								}
							} catch (Exception e) {
								return false;
							}
						} else {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
}
