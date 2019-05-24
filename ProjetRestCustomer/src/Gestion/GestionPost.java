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

import Model.Post;
import ParserXML.ParserPosts;

public class GestionPost {
	
	private GestionInstagram gestionInstagram;
	private WebTarget target;
	
	public GestionPost(GestionInstagram gestionInstagram) {
		this.gestionInstagram = gestionInstagram;
		
		// init target
		ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        target = client.target(GestionInstagram.getPathGestionInstagram() + "/PostService/posts");
	}
	
	public void displayPosts() {
		
        Response  response = target.request().
                accept(MediaType.APPLICATION_XML).
                get(Response.class);
        
        if(response.getStatus()==200) // OK
        {
        	String postsXML = response.readEntity(String.class);
            List<Post> posts = ParserPosts.getPostsXML(postsXML);
            if(posts != null) {
            	System.out.println("Les posts :");
    	        for(Post post : posts) {
    	        	System.out.println(post.toString());
    	        }
            }
            else {
            	System.out.println("Il n'y a pour le moment aucun post !");
            }
        } else {
			String serverErrorMsg = response.readEntity(String.class);
			System.out.println("ERROR : "+ serverErrorMsg);
		}
	}
	
	public void displayPost(String idPost) {
		
		Response  response = target.path(idPost).request().
                accept(MediaType.APPLICATION_XML).
                get(Response.class);
		
		if(response.getStatus()==200) // OK
        {
        	String postXML = response.readEntity(String.class);
            Post post = ParserPosts.getPostXML(postXML);
    	    System.out.println(post.toString());

        } else {
			String serverErrorMsg = response.readEntity(String.class);
			System.out.println("ERROR : "+ serverErrorMsg);
		}
	}
	
	public void displayPostsForUser(String idUtilisateur) {
		
		Response  response = target.path("User").path(idUtilisateur).request().
                accept(MediaType.APPLICATION_XML).
                get(Response.class);
		
		if(response.getStatus()==200) // OK
        {
        	String postsXML = response.readEntity(String.class);
            List<Post> posts = ParserPosts.getPostsXML(postsXML);
            if(posts != null) {
            	System.out.println("Les posts :");
    	        for(Post post : posts) {
    	        	System.out.println(post.toString());
    	        }
            }
            else {
            	System.out.println("Il n'y a pour le moment aucun post !");
            }
        } else {
			String serverErrorMsg = response.readEntity(String.class);
			System.out.println("ERROR : "+ serverErrorMsg);
		}
	}
	
	public void ajouter(String comment, String image) {
		
		if(!gestionInstagram.userIsConnected())
			return;
		
		Form form = new Form();
		   form.param("comment", comment)
		   .param("image", image)
		   .param("userId", Integer.toString(gestionInstagram.getUserConnected().getId()));
		
		Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);
		
		Response response = target.request(MediaType.APPLICATION_FORM_URLENCODED)
				.header("authentificationToken", gestionInstagram.getToken())
				.accept(MediaType.APPLICATION_XML).post(entity);
		
		if(response.getStatus() == 200) {
			System.out.println("Le post a bien été ajouté.");
		} else {
			String serverErrorMsg = response.readEntity(String.class);
			System.out.println("ERROR : "+ serverErrorMsg);
		}
	}

	public void update(String idPost, String comment, String image) {
		
		if(!gestionInstagram.userIsConnected())
			return;
		
		Form form = new Form();
		   form.param("comment", comment)
		   .param("image", image);
		
		Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);
		
		Response response = target.path(idPost)
				.request(MediaType.APPLICATION_FORM_URLENCODED)
				.header("authentificationToken", gestionInstagram.getToken())
				.accept(MediaType.APPLICATION_XML).put(entity);

		if(response.getStatus() == 200) {
			System.out.println("La modification a été enregistré.");
			
			String postXML = response.readEntity(String.class);
			Post post = ParserPosts.getPostXML(postXML);
			System.out.println(post.toString());
			
		} else {
			String serverErrorMsg = response.readEntity(String.class);
			System.out.println("ERROR : "+ serverErrorMsg);
		}
	}
	
	public void delete(String idPost) {
		BufferedReader keyboard=new BufferedReader(new InputStreamReader(System.in));
		String confirm = "";
		do {
			System.out.print("Voulez-vous vraiment supprimer votre post ? (yes/no)");
			try {
				confirm=keyboard.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}while(!confirm.equals("yes") && !confirm.equals("no"));
		
		if(confirm.equals("no"))
			return;
		
		Response response = target.path(idPost).request()
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
		System.out.println("***** AIDE MANIPULATIONS DES POSTES *****");
		System.out.println("afficherPostes");
		System.out.println("afficherPoste,<idPost>");
		System.out.println("afficherPostesPourUtilisateur,<idUtilisateur>");
		System.out.println("ajoutPoste,<commentaire>,<image>");
		System.out.println("modifierPoste,<idPoste>,<commentaire>,<url image>");
		System.out.println("supprimerPoste,<idPost>");
	}
}