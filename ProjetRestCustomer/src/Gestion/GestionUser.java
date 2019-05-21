package Gestion;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;

import org.glassfish.jersey.client.ClientConfig;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import Model.User;

public class GestionUser {
	
	GestionInstagram gestionInstagram;
	
	public GestionUser(GestionInstagram gestionInstagram) {
		this.gestionInstagram = gestionInstagram;
	}
	
	public void displayUsers() {
		
    	ClientConfig config = new ClientConfig();

        Client client = ClientBuilder.newClient(config);
    	
        WebTarget target = client.target(gestionInstagram.getWebApiUrl());
        
        Response  response = target.path("rest").path("UserService")
                .path("users").
                request().
                accept(MediaType.APPLICATION_XML).
                get(Response.class);
        
        String plainAnswer = target.path("rest").path("UserService")
                .path("users").
                request().
                accept(MediaType.APPLICATION_XML).
                get(String.class)
                .toString();
        
        System.out.println(response);
        System.out.println(plainAnswer);
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = convertStringToDocument(plainAnswer);
            doc.getDocumentElement().normalize();
            
            
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("user");
            
            //now XML is loaded as Document in memory, lets convert it to Object List
            List<User> empList = new ArrayList<User>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                empList.add(getUser(nodeList.item(i)));
            }
            System.out.println("Les utilisateurs :");
            //lets print Employee list information
            for (User emp : empList) {
                System.out.println(emp.toString());
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
	}
	
	private static User getUser(Node node) {
	    //XMLReaderDOM domReader = new XMLReaderDOM();
	    User emp = new User();
	    if (node.getNodeType() == Node.ELEMENT_NODE) {
	        Element element = (Element) node;
	        emp.setName(getTagValue("name", element));
	        emp.setLastname(getTagValue("lastname", element));
	        emp.setId(Integer.parseInt(getTagValue("id", element)));
	        emp.setPseudo(getTagValue("pseudo", element));
	    }

	    return emp;
	}


	private static String getTagValue(String tag, Element element) {
	    NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
	    Node node = (Node) nodeList.item(0);
	    return node.getNodeValue();
	}
	
    private static Document convertStringToDocument(String xmlStr) {
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


