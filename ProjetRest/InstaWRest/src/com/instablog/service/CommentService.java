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

import com.instablog.connexion.InstaException;
import com.instablog.connexion.TokenManagement;
import com.instablog.dao.*;
import com.instablog.model.*;

@Path("/CommentService") 

public class CommentService { 
	
   CommentDao commentDao = new CommentDao();
   PostDao postDao = new PostDao();
   UserDao userDao = new UserDao();
   
   @GET 
   @Path("/comments") 
   @Produces(MediaType.APPLICATION_XML) 
   public List<Comment> getComments() throws InstaException{ 
      return commentDao.getAllComments(); 
   }  
   
   @GET 
   @Path("/comments/users/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public List<Comment> getCommentByUserId(@PathParam("id") int id) throws NumberFormatException, InstaException{ 
	   List<Comment> commentList;
	   try {
		   commentList = commentDao.getAllCommentsUserId(id);
	} catch (Exception e) {
		throw new RuntimeException(e.getMessage());
	}
      return commentList; 
   }
   
   @GET 
   @Path("/comments/posts/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public List<Comment> getCommentByPostId(@PathParam("id") int id) throws NumberFormatException, InstaException{ 
	   List<Comment> commentList;
	   try {
		   commentList = commentDao.getAllCommentsPostId(id);
	} catch (Exception e) {
		throw new RuntimeException(e.getMessage());
	}
      return commentList; 
   }
   
   @GET 
   @Path("/comments/comments/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public List<Comment> getCommentAnswerId(@PathParam("id") int id) throws NumberFormatException, InstaException{ 
	   List<Comment> commentList;
	   try {
		   commentList = commentDao.getAllCommentsAnswer(id);
	} catch (Exception e) {
		throw new RuntimeException(e.getMessage());
	}
      return commentList; 
   }
   
   @GET 
   @Path("/comments/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public Comment getComment(@PathParam("id") int id){ 
	   Comment comment;
	   try {
		   comment = commentDao.getCommentById(id);
	} catch (Exception e) {
		throw new RuntimeException(e.getMessage());
	}
      return comment; 
   }
   
   @POST
   @Path("/comments") 
   @Produces(MediaType.APPLICATION_XML) 
   public Comment createComment(@FormParam("message") String message, @FormParam("userId") int userId, @FormParam("postId") int postId, @FormParam("commentId") int commentId, @HeaderParam(value="authentificationToken") String headers) throws Exception{
	   Comment comment = null;
	   String UUID ="";
	   try{
		   //Comment comment = commentDao.getCommentById(id);
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
		   
		   User user = userDao.getUserById(userId);
		   Post post = postDao.getPostById(postId);
		   Comment commentRefered = new Comment();
		   
		   if(commentId != 0) {
			   commentRefered = commentDao.getCommentById(commentId);
			   if(commentRefered.getPost().getId() == post.getId()) {
				   comment = new Comment(message, creationDate.toString(), user, post , commentRefered);
			   }else {
				   throw new InstaException("commentResponse doit coincider avec post_id dans le cas ou il n'est pas null");
			   }
		   }else {
			   //commentRefered.setId(0);
			   comment = new Comment(message, creationDate.toString(), user, post);
		   }
		   
		   commentDao.createComment(comment);
		 
		} catch (Exception e) {
			throw new InstaException("Les parametres ne sont pas corrects (message,userId,postId,commentId), veuillez les verifier les parametres entres");
		}
	   return comment;
   } 
   
   @PUT 
   @Path("/comments/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public String updateCommentById(@PathParam("id") int id, @FormParam("message") String message, @HeaderParam(value="authentificationToken") String headers) throws Exception{ 
	   boolean updated = false;
	   String UUID ="";
	   try{
		   Comment comment = commentDao.getCommentById(id);
           User userBD = userDao.getUserById(comment.getId());
           UUID = userBD.getUUID();
       }catch(Exception e){
           throw new Exception("Le userIDMember du token ne correspond à aucun utilisateur");
       }
       
       //headers.getHeaderString("authentificationToken")
       if (!TokenManagement.verifyToken(headers,UUID)) {

             throw new NotAuthorizedException("Invalid token");

       }
       
	   String response = "Error, Comment can t be updated ";
	   try {		   
		   updated = commentDao.updateCommentById(id, message); 
		   if (updated) {
			   response = "Comment " + id + " updated";
		   }
		} catch (Exception e) {
			throw new InstaException("Les parametres ne sont pas corrects (message), veuillez les verifier les parametres entres");
		}
	   return response;
   } 
   
   @DELETE 
   @Path("/comments/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public String DeleteComment(@PathParam("id") int id, @HeaderParam(value="authentificationToken") String headers) throws Exception{ 
	   boolean deleted = false;
	   String UUID ="";
	   try{
		   Comment comment = commentDao.getCommentById(id);
           User userBD = comment.getUser();
           UUID = userBD.getUUID();
       }catch(Exception e){
           throw new Exception("Le userIDMember du token ne correspond à aucun utilisateur");
       }
       
       //headers.getHeaderString("authentificationToken")
       if (!TokenManagement.verifyToken(headers,UUID)) {

             throw new NotAuthorizedException("Invalid token");

       }
	   String response = "Error, Comment can t be deleted ";
	   deleted = commentDao.deleteCommentById(id); 
	   if (deleted) {
		   response = "Comment " + id + " delete";
	   }
	   return response;
   } 
}
