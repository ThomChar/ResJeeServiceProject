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
	   List<Post> postList;
	   try {
		   postList = postDao.getAllPostsUserId(id);
	} catch (Exception e) {
		throw new RuntimeException(e.getMessage());
	}
      return postList; 
   }
   
   @GET 
   @Path("/posts/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public Post getPost(@PathParam("id") int id) throws NumberFormatException, InstaException{ 
	   Post post;
	   try {
		post = postDao.getPostById(id);
	} catch (Exception e) {
		throw new RuntimeException(e.getMessage());
	}
      return post; 
   }
   
   @POST
   @Path("/posts") 
   @Produces(MediaType.APPLICATION_XML) 
   public Post createPost(@FormParam("comment") String comment, @FormParam("image") String image, @FormParam("userId") int userId, @HeaderParam(value="authentificationToken") String headers) throws Exception{ 
	   Post post = null;
	   String UUID ="";
	   try{
           User userBD = userDao.getUserById(userId);
           UUID = userBD.getUUID();
       }catch(Exception e){
           throw new Exception("Le userIDMember du token ne correspond à aucun utilisateur");
       }
       
       //headers.getHeaderString("authentificationToken")
       if (!TokenManagement.verifyToken(headers,UUID)) {

             throw new NotAuthorizedException("Invalid token");

       }
       
	   try {
		   LocalDateTime creationDate = LocalDateTime.now();
		 //Recuperer user corrrespondant à l'id et creer l'objet
		   post = new Post(image, comment, creationDate.toString() ,userDao.getUserById(userId));
		   postDao.createPost(post);
		} catch (Exception e) {
			throw new InstaException("Les parametres ne sont pas corrects (comment,image,userId), veuillez les verifier les parametres entres");
		}
	   return post;
   } 
   
   @PUT 
   @Path("/posts/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public String updatePostById(@PathParam("id") int id, @FormParam("comment") String comment, @FormParam("image") String image, @HeaderParam(value="authentificationToken") String headers) throws Exception{ 
	   boolean updated = false;
	   String UUID ="";
	   try{
		   Post post = postDao.getPostById(id);
           User userBD = post.getUser();
           UUID = userBD.getUUID();
       }catch(Exception e){
           throw new Exception("Le userIDMember du token ne correspond à aucun utilisateur");
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
	   return response;
   } 
   
   @DELETE 
   @Path("/posts/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public String DeletePost(@PathParam("id") int id, @HeaderParam(value="authentificationToken") String headers) throws Exception{ 
	   boolean deleted = false;
	   String UUID ="";
	   try{
		   Post post = postDao.getPostById(id);
           User userBD = post.getUser();
           UUID = userBD.getUUID();
       }catch(Exception e){
           throw new Exception("Le userIDMember du token ne correspond à aucun utilisateur");
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
	   return response;
   } 
}
