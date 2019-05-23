package ParserXML;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Model.User;

public class ParserUsers {
	
	public ParserUsers() {
	}
	
	public static User getUser(Node node) {
	    User emp = new User();
	    if (node.getNodeType() == Node.ELEMENT_NODE) {
	        Element element = (Element) node;
	        emp.setFirstName(Parser.getTagValue("firstname", element));
	        emp.setLastname(Parser.getTagValue("lastname", element));
	        emp.setId(Integer.parseInt(Parser.getTagValue("idUser", element)));
	        emp.setPseudo(Parser.getTagValue("pseudo", element));
	        emp.setDateCreation(Parser.getTagValue("dateCreation", element));
	    }

	    return emp;
	}
	
	public static List<User> getUsersXML(String usersXML) {
		List<User> userList = new ArrayList<User>();
        
        try {
        	// load XML as Document in memory
            Document doc = Parser.convertStringToDocument(usersXML);
            doc.getDocumentElement().normalize();
            
            NodeList nodeList = doc.getElementsByTagName("user");
            
            // convert document to Object List
            for (int i = 0; i < nodeList.getLength(); i++) {
            	userList.add(getUser(nodeList.item(i)));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
		return userList;
	}
	
	public static User getUserXML(String userXML) {
		User user = new User();
		
		try {
		// load XML as Document in memory
		 Document doc = Parser.convertStringToDocument(userXML);
         doc.getDocumentElement().normalize();
         
         NodeList nodeList = doc.getElementsByTagName("user");

         // convert document to Object
         user = getUser(nodeList.item(0));

		} catch (Exception e1) {
            e1.printStackTrace();
        }


	    return user;
	}
}
