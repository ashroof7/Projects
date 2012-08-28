package DBSystem;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLWriter implements Writer {
	private String url, name;

	public XMLWriter(String url, String name) {
		this.url = url;
		this.name = name;
	}

	@Override
	public void writeTable(ArrayList<Entry> values, String[] types)
			throws ParserConfigurationException, TransformerException,
			IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();
		Element root = doc.createElement(name + "Table");
		int length = types.length;
		for (Entry e : values) {
			String[] val = e.getValues();
			Element z = doc.createElement(name);
			for (int i = 0; i < length; i++) {
				Element row = doc.createElement(types[i]);
				row.setTextContent(val[i]);
				z.appendChild(row);
			}

			root.appendChild(z);

		}

		doc.appendChild(root);
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();

		Source s = new DOMSource(doc);
		FileOutputStream file = new FileOutputStream(url + name + ".xml");
		Result res = new StreamResult(file);

		transformer.transform(s, res);
		file.close();
	}

}
