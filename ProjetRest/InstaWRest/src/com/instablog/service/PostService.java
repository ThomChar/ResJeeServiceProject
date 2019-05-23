package com.instablog.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.instablog.connexion.Connexion;
import com.instablog.connexion.InstaException;
import com.instablog.connexion.TokenManagement;
import com.instablog.dao.*;
import com.instablog.model.*;

@Path("/PostService") 

public class PostService {  
	
   Connexion cx = new Connexion();	
   PostDao postDao = new PostDao();
   UserDao userDao = new UserDao();
   
   @GET 
   @Path("/posts") 
   @Produces(MediaType.APPLICATION_XML) 
   public List<Post> getPosts() throws InstaException{ 
      return postDao.getAllPosts(); 
   }  
   
   @GET 
   @Path("/posts/User/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public List<Post> getPostByUserId(@PathParam("id") int id) throws NumberFormatException, InstaException{ 
    	  List<Post> postList= postDao.getAllPostsUserId(id);
	      return postList;
   }
   
   @GET 
   @Path("/posts/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public Response getPost(@PathParam("id") int id) throws NumberFormatException, InstaException{ 
	   try {
		   Post post =  postDao.getPostById(id);
	      return Response.ok(post).build();
	   } catch(Exception e) {
		   System.out.println("ERROR:"+e.getMessage());
		   return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
	   }
   }
   
   @POST
   @Path("/posts") 
   @Produces(MediaType.APPLICATION_XML) 
   public Response createPost(@FormParam("comment") String comment, @FormParam("image") String image, @FormParam("userId") int userId, @HeaderParam(value="authentificationToken") String headers) throws Exception{ 
	   try {
		   Post post = null;
		   String UUID ="";
		   try{
	           User userBD = userDao.getUserById(userId);
	           UUID = userBD.getUUID();
	       }catch(Exception e){
	           throw new Exception("Vous n'êtes pas connecté.");
	       }
	       
	       //headers.getHeaderString("authentificationToken")
	       if (!TokenManagement.verifyToken(headers,UUID)) {
	             throw new NotAuthorizedException("Le token n'est pas valide.");
	       }
	       
		   try {
			   LocalDateTime creationDate = LocalDateTime.now();
			 //Recuperer user corrrespondant à l'id et creer l'objet
			   post = new Post(image, comment, creationDate.toString() ,userDao.getUserById(userId));
			   postDao.createPost(post);
			} catch (Exception e) {
				throw new InstaException("Les parametres ne sont pas corrects (comment,image,userId), veuillez les verifier les parametres entres");
			}
		   return Response.ok(post).build();
	   } catch(Exception e) {
		   System.out.println("ERROR:"+e.getMessage());
		   return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
	   }
   } 
   
   @PUT 
   @Path("/posts/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public Response updatePostById(@PathParam("id") int id, @FormParam("comment") String comment, @FormParam("image") String image, @HeaderParam(value="authentificationToken") String headers) throws Exception{ 
	   try {
		   boolean updated = false;
		   String UUID ="";
		   try{
			   Post post = postDao.getPostById(id);
	           User userBD = post.getUser();
	           UUID = userBD.getUUID();
	       }catch(Exception e){
	           throw new Exception("Ce n'est pas votre poste.");
	       }
	       
	       //headers.getHeaderString("authentificationToken")
	       if (!TokenManagement.verifyToken(headers,UUID)) {
	             throw new NotAuthorizedException("Invalid token");
	       }
	       
		   String response = "Error, Post can t be updated ";
		   try {		   
			   updated = postDao.updatePostById(id, comment, image); 
			   if (updated) {
				   response = "Post " + id + " updated";
			   }
			} catch (Exception e) {
				throw new InstaException("Les parametres ne sont pas corrects (comment,image), veuillez les verifier les parametres entres");
			}

		   Post post = postDao.getPostById(id);
		   
		   return Response.ok(post).build();
		   
	   } catch(Exception e) {
		   System.out.println("ERROR:"+e.getMessage());
		   return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
	   }
   } 
   
   @DELETE 
   @Path("/posts/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public Response DeletePost(@PathParam("id") int id, @HeaderParam(value="authentificationToken") String headers) throws Exception{ 
	   try {
		   boolean deleted = false;
		   String UUID ="";
		   try{
			   Post post = postDao.getPostById(id);
	           User userBD = post.getUser();
	           UUID = userBD.getUUID();
	       }catch(Exception e){
	    	   throw new Exception("Ce n'est pas votre poste.");
	       }
	       
	       //headers.getHeaderString("authentificationToken")
	       if (!TokenManagement.verifyToken(headers,UUID)) {
	
	             throw new NotAuthorizedException("Invalid token");
	
	       }
		   String response = "Error, Post can t be deleted ";
		   deleted = postDao.deletePostById(id); 
		   if (deleted) {
			   response = "Post " + id + " delete";
		   }

		   return Response.ok().build();
		   
	   } catch(Exception e) {
		   System.out.println("ERROR:"+e.getMessage());
		   return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
	   }
   } 
}
