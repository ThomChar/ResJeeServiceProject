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
import ParserXML.Parser;
import ParserXML.ParserUsers;

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
        
        String usersXML = target.path("rest").path("UserService")
                .path("users").
                request().
                accept(MediaType.APPLICATION_XML).
                get(String.class)
                .toString();
        
        System.out.println(response);
        //System.out.println(usersXML);
        
        
        List<User> users = ParserUsers.getUsersXML(usersXML);
        if(users != null) {
        	System.out.println("Les utilisateurs :");
	        for(User user : users) {
	        	System.out.println(user.toString());
	        }
        }
        else {
        	System.out.println("Il n'y a pour le moment aucun utilisateur !");
        }
	}
	
	


	
}


