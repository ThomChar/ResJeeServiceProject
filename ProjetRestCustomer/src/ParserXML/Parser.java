package ParserXML;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Parser {
	
	ParserUsers users;
	
	public Parser() {
		users = new ParserUsers();
	}
	
	
	
	public static String getTagValue(String tag, Element element) {
		try {
		    NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
		    Node node = (Node) nodeList.item(0);
	    return node.getNodeValue();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try  
        {  
            builder = factory.newDocumentBuilder();  
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return null;
    }
}
