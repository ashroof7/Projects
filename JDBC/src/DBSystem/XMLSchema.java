package DBSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLSchema implements schema {

    private String URL;
    private String[] tableNames;
    private String[][] colNames;
    private String[][] colTypes;
    private String fileName;
    private String userName;
    private String password;

    @Override
    public boolean read() throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        FileInputStream file = new FileInputStream(new File(URL + fileName + ".xml"));
        Document d = builder.parse(file);
        NodeList e = d.getElementsByTagName("User");
        if (e.getLength() == 0) {
            return false;
        }
        Node User = e.item(0);
        userName = User.getAttributes().getNamedItem("username").getNodeValue();
        password = User.getAttributes().getNamedItem("password").getNodeValue();
        e = d.getElementsByTagName("table");
        int tableLengths = e.getLength();
        tableNames = new String[tableLengths];
        colNames = new String[tableLengths][];
        colTypes = new String[tableLengths][];
        for (int ll = 0; ll < tableLengths; ll++) {
            Node nNode = e.item(ll);
            tableNames[ll] = nNode.getAttributes().getNamedItem("name").getNodeValue();

            NodeList nElements = nNode.getChildNodes();
            int length = nElements.getLength();
            colNames[ll] = new String[length];
            colTypes[ll] = new String[length];

            for (int l = 0; l < length; l++) {
                Node temp = nElements.item(l);
                NamedNodeMap par = temp.getAttributes();
                colNames[ll][l] = par.getNamedItem("name").getNodeValue();
                colTypes[ll][l] = par.getNamedItem("Type").getNodeValue();
            }

        }

        file.close();
        return true;
    }

    @Override
    public String[] getTableNames() {
        return tableNames;
    }

    @Override
    public String[][] getColNames() {
        // TODO Auto-generated method stub
        return colNames;
    }

    @Override
    public String[][] getColTypes() {
        // TODO Auto-generated method stub
        return colTypes;
    }

    @Override
    public void write(String UserName, String Password) throws IOException {
        // TODO Auto-generated method stub

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element root = doc.createElement("Databases");
            Element User = doc.createElement("User");
            User.setAttribute("password", Password);
            User.setAttribute("username", UserName);
            root.appendChild(User);

            int length = tableNames.length;
            for (int i = 0; i < length; i++) {
                Element childElement = doc.createElement("table");
                childElement.setAttribute("name", tableNames[i]);
                int len = colTypes[i].length;
                for (int k = 0; k < len; k++) {
                    Element temp = doc.createElement("variable");
                    temp.setAttribute("name", colNames[i][k]);
                    temp.setAttribute("Type", colTypes[i][k]);
                    childElement.appendChild(temp);
                }
                root.appendChild(childElement);
            }
            doc.appendChild(root);
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();

            Source s = new DOMSource(doc);
            FileOutputStream file = new FileOutputStream(new File(URL + fileName + ".xml"));
            Result res = new StreamResult(file);
            transformer.transform(s, res);
            file.close();
        } catch (Exception e) {
            throw new IOException("XML Parsing error");
        }

    }

    public XMLSchema(String URLs, String fileName) {
        URL = URLs;
        this.fileName = fileName;
    }

    public void set(String[] tables, String[][] names, String[][] types) {
        // TODO Auto-generated method stub
        tableNames = tables;
        colNames = names;
        colTypes = types;

    }

    @Override
    public String getUser() {
        // TODO Auto-generated method stub
        return userName;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return password;
    }
}
