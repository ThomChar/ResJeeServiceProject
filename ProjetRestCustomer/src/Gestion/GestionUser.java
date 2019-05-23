package Gestion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.ws.rs.core.Form;

import org.glassfish.jersey.client.ClientConfig;

import Model.User;
import ParserXML.ParserUsers;

public class GestionUser {
	
	private GestionInstagram gestionInstagram;
	private WebTarget target;
	
	public GestionUser(GestionInstagram gestionInstagram) {
		this.gestionInstagram = gestionInstagram;
		
		// init target
		ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        target = client.target(GestionInstagram.getPathGestionInstagram() + "/UserService/users");
	}
	
	public void displayUsers() {
		
        Response  response = target.request().
                accept(MediaType.APPLICATION_XML).
                get(Response.class);
        
        if(response.getStatus()==200) // OK
        {
        	String usersXML = response.readEntity(String.class);
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
        } else {
			String serverErrorMsg = response.readEntity(String.class);
			System.out.println("ERROR : "+ serverErrorMsg);
		}
	}
	
	public void displayUser(String idUtilisateur) {
		
		Response  response = target.path(idUtilisateur).request().
                accept(MediaType.APPLICATION_XML).
                get(Response.class);
		
		if(response.getStatus()==200) // OK
        {
			String userXML = response.readEntity(String.class);
	        User user = ParserUsers.getUserXML(userXML);
	        System.out.println(user.toString());
        } else {
			String serverErrorMsg = response.readEntity(String.class);
			System.out.println("ERROR : "+ serverErrorMsg);
		}
	}
	
	public void ajouter(String firstName, String lastName, String pseudo, String password) {

		Form form = new Form();
		   form.param("firstname", firstName)
		   .param("lastname", lastName)
		   .param("pseudo", pseudo)
		   .param("password", password);
		
		Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);
		
		Response response = target.request(MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_XML).post(entity);
		
		if(response.getStatus() == 200) {
			System.out.println("L'utilisateur a bien été ajouté.");
			//System.out.println(user.toString());
		} else {
			String serverErrorMsg = response.readEntity(String.class);
			System.out.println("ERROR : "+ serverErrorMsg);
		}
	}
	
	public void login(String pseudo, String password) {
		
		Form form = new Form();
		   form.param("pseudo", pseudo)
		   .param("password", password);
		
		Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);
		
		Response response = target.path("login").request(MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_XML).post(entity);

		if(response.getStatus() == 200) {
			String token = response.readEntity(String.class);
			//System.out.println("token:"+token);
			gestionInstagram.setToken(token);

			String usersXML = target.path("pseudo").path(pseudo).
	                request().
	                accept(MediaType.APPLICATION_XML).
	                get(String.class)
	                .toString();
	        
	        User userConnected = ParserUsers.getUserXML(usersXML);
			gestionInstagram.setUserConnected(userConnected);
			
			System.out.println("Vous êtes connectés avec succès " + userConnected.getFirstName() + " " + userConnected.getLastname() +" !");
			System.out.println(userConnected.toString());
		} else {
			String serverErrorMsg = response.readEntity(String.class);
			System.out.println("ERROR : "+ serverErrorMsg);
		}

	}
	
	public void logout() {
		
		if(!gestionInstagram.userIsConnected())
			return;
		
		Response response = target.path("logout")
				.path(Integer.toString(gestionInstagram.getUserConnected().getId()))
				.request().accept(MediaType.APPLICATION_XML).delete();

		if(response.getStatus() == 200) {
			gestionInstagram.setToken(null);
			gestionInstagram.setUserConnected(null);
			System.out.println("Vous êtes déconnectés avec succès.");
			
		} else {
			String serverErrorMsg = response.readEntity(String.class);
			System.out.println("ERROR : "+ serverErrorMsg);
		}
	}
	
	public void logoutAllUsers() {
		
		Response response = target.path("logout")
				.request().accept(MediaType.APPLICATION_XML).delete();

		if(response.getStatus() == 200) {
			gestionInstagram.setToken(null);
			gestionInstagram.setUserConnected(null);
			System.out.println("Tous les utilisateurs ont été déconnecté avec succès.");
			
		} else {
			String serverErrorMsg = response.readEntity(String.class);
			System.out.println("ERROR : "+ serverErrorMsg);
		}
	}
	
	public void update(String firstName, String lastName, String pseudo) {
		
		if(!gestionInstagram.userIsConnected())
			return;
		
		Form form = new Form();
		   form.param("firstname", firstName)
		   .param("lastname", lastName)
		   .param("pseudo", pseudo);
		
		Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);
		
		Response response = target.path(Integer.toString(gestionInstagram.getUserConnected().getId()))
				.request(MediaType.APPLICATION_FORM_URLENCODED)
				.header("authentificationToken", gestionInstagram.getToken())
				.accept(MediaType.APPLICATION_XML).put(entity);

		if(response.getStatus() == 200) {
			System.out.println("La modification a été enregistré.");
			
			String usersXML = response.readEntity(String.class);
			User user = ParserUsers.getUserXML(usersXML);
			gestionInstagram.setUserConnected(user);
			System.out.println(user.toString());
			
		} else {
			String serverErrorMsg = response.readEntity(String.class);
			System.out.println("ERROR : "+ serverErrorMsg);
		}
	}
	
	public void delete() {
		
		if(!gestionInstagram.userIsConnected())
			return;
		
		BufferedReader keyboard=new BufferedReader(new InputStreamReader(System.in));
		String confirm = "";
		do {
			System.out.print("Voulez-vous vraiment supprimer votre compte ? (yes/no)");
			try {
				confirm=keyboard.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}while(!confirm.equals("yes") && !confirm.equals("no"));
		
		if(confirm.equals("no"))
			return;
		
		Response response = target.path(Integer.toString(gestionInstagram.getUserConnected().getId())).request()
				.header("authentificationToken", gestionInstagram.getToken())
				.accept(MediaType.APPLICATION_XML).delete();

		
		if(response.getStatus() == 200) {
			System.out.println("La suppression a été effectuée.");
			
		} else {
			String serverErrorMsg = response.readEntity(String.class);
			System.out.println("ERROR : "+ serverErrorMsg);
		}
	}
	
	public void displayHelp() {
		System.out.println("***** AIDE MANIPULATIONS DES UTILISATEURS *****");
		System.out.println("afficherUtilisateurs");
		System.out.println("afficherUtilisateur,<id>");
		System.out.println("ajouterUtilisateur,<pseudo>,<prénom>,<nom>,<mot de passe>");
		System.out.println("login,<pseudo>,<mot de passe>");
		System.out.println("modifierProfile,<pseudo>,<prénom>,<nom>");
		System.out.println("supprimerProfile");
	}
}