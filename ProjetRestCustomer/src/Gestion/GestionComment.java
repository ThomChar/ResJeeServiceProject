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

import Model.Comment;
import Model.Post;
import ParserXML.ParserComments;
import ParserXML.ParserPosts;

public class GestionComment {
	
	private GestionInstagram gestionInstagram;
	private WebTarget target;
	
	public GestionComment(GestionInstagram gestionInstagram) {
		this.gestionInstagram = gestionInstagram;
		
		// init target
		ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        target = client.target(GestionInstagram.getPathGestionInstagram() + "/CommentService/comments");
	}
	
	public void displayComments() {
		
        Response  response = target.request().
                accept(MediaType.APPLICATION_XML).
                get(Response.class);
        
        if(response.getStatus()==200) // OK
        {
        	String commentsXML = response.readEntity(String.class);
        	
            List<Comment> comments = ParserComments.getCommentsXML(commentsXML);
            if(comments != null && comments.size()>0) {
            	System.out.println("Les commentaires ("+comments.size()+") :");
    	        for(Comment comment : comments) {
    	        	System.out.println(comment.toString());
    	        }
            }
            else {
            	System.out.println("Il n'y a pour le moment aucun commentaire !");
            }
        } else {
			String serverErrorMsg = response.readEntity(String.class);
			System.out.println("ERROR : "+ serverErrorMsg);
		}
	}
	
	public void displayCommentsByUser(String idUser) {
		
		Response  response = target.path("users").path(idUser).request().
                accept(MediaType.APPLICATION_XML).
                get(Response.class);
		
		if(response.getStatus()==200) // OK
        {
        	String commentsXML = response.readEntity(String.class);
        	List<Comment> comments = ParserComments.getCommentsXML(commentsXML);
            if(comments != null && comments.size()>0) {
            	System.out.println("Les commentaires ("+comments.size()+") :");
    	        for(Comment comment : comments) {
    	        	System.out.println(comment.toString());
    	        }
            }
            else {
            	System.out.println("Il n'y a pour le moment aucun commentaire par cet utilisateur !");
            }

        } else {
			String serverErrorMsg = response.readEntity(String.class);
			System.out.println("ERROR : "+ serverErrorMsg);
		}
	}
	
	public void displayCommentsByPost(String idPost) {
		
		Response  response = target.path("posts").path(idPost).request().
                accept(MediaType.APPLICATION_XML).
                get(Response.class);
		
		if(response.getStatus()==200) // OK
        {
        	String commentsXML = response.readEntity(String.class);
        	List<Comment> comments = ParserComments.getCommentsXML(commentsXML);
            if(comments != null && comments.size()>0) {
            	System.out.println("Les commentaires ("+comments.size()+") :");
    	        for(Comment comment : comments) {
    	        	System.out.println(comment.toString());
    	        }
            }
            else {
            	System.out.println("Il n'y a pour le moment aucun commentaire pour ce poste !");
            }

        } else {
			String serverErrorMsg = response.readEntity(String.class);
			System.out.println("ERROR : "+ serverErrorMsg);
		}
	}
	
	
	public void displayComment(String idComment) {
		
		Response  response = target.path(idComment).request().
                accept(MediaType.APPLICATION_XML).
                get(Response.class);
		
		if(response.getStatus()==200) // OK
        {
        	String commentXML = response.readEntity(String.class);
            Comment comment = ParserComments.getCommentXML(commentXML);
    	    System.out.println(comment.toString());

        } else {
			String serverErrorMsg = response.readEntity(String.class);
			System.out.println("ERROR : "+ serverErrorMsg);
		}
	}
	
	public void ajouter(String message, String postId, String commentId) {
		
		if(!gestionInstagram.userIsConnected())
			return;
		
		if(commentId.equals("null"))
			commentId = "0";

		Form form = new Form();
		   form.param("message", message)
		   .param("postId", postId)
		   .param("commentId", commentId)
		   .param("userId", Integer.toString(gestionInstagram.getUserConnected().getId()));
		
		Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);
		
		Response response = target.request(MediaType.APPLICATION_FORM_URLENCODED)
				.header("authentificationToken", gestionInstagram.getToken())
				.accept(MediaType.APPLICATION_XML).post(entity);
		
		if(response.getStatus() == 200) {
			System.out.println("Le commentaire a bien été ajouté.");
		} else {
			String serverErrorMsg = response.readEntity(String.class);
			System.out.println("ERROR : "+ serverErrorMsg);
		}
	}

	public void update(String idMessage, String message) {
		
		if(!gestionInstagram.userIsConnected())
			return;
		
		Form form = new Form();
		   form.param("message", message);
		
		Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);
		
		Response response = target.path(idMessage)
				.request(MediaType.APPLICATION_FORM_URLENCODED)
				.header("authentificationToken", gestionInstagram.getToken())
				.accept(MediaType.APPLICATION_XML).put(entity);

		if(response.getStatus() == 200) {
			System.out.println("La modification a été enregistré.");
			
			String commentXML = response.readEntity(String.class);
			Comment comment = ParserComments.getCommentXML(commentXML);
			System.out.println(comment.toString());
			
		} else {
			String serverErrorMsg = response.readEntity(String.class);
			System.out.println("ERROR : "+ serverErrorMsg);
		}
	}
	
	public void delete(String idComment) {
		BufferedReader keyboard=new BufferedReader(new InputStreamReader(System.in));
		String confirm = "";
		do {
			System.out.print("Voulez-vous vraiment supprimer votre commentaire ? (yes/no)");
			try {
				confirm=keyboard.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}while(!confirm.equals("yes") && !confirm.equals("no"));
		
		if(confirm.equals("no"))
			return;
		
		Response response = target.path(idComment).request()
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
		System.out.println("***** AIDE MANIPULATIONS DES COMMENTAIRES *****");
		System.out.println("afficherCommentaires");
		System.out.println("afficherCommentairesParUtilisateur,<idUser>");
		System.out.println("afficherCommentairesParPoste,<idPost>");
		System.out.println("afficherCommentaire,<idComment>");
		System.out.println("ajouterCommentaire,<message>,<postId>,<commentId : default: null>");
		System.out.println("modifierCommentaire,<idMessage>,<message>");
		System.out.println("supprimerCommentaire,<idMessage>");
	}
}