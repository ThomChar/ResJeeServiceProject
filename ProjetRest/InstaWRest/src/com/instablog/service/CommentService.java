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
   public List<Comment> getCommentByUserId(@PathParam("id") int id) { 
	  List<Comment> commentList = commentDao.getAllCommentsUserId(id);
      return commentList; 
   }
   
   @GET 
   @Path("/comments/posts/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public List<Comment> getCommentByPostId(@PathParam("id") int id) { 
	  List<Comment> commentList;
	  commentList = commentDao.getAllCommentsPostId(id);
	  return commentList; 
   }
   
   @GET 
   @Path("/comments/comments/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public List<Comment> getCommentAnswerId(@PathParam("id") int id) throws NumberFormatException, InstaException{ 
	  List<Comment> commentList;
	  commentList = commentDao.getAllCommentsAnswer(id);
      return commentList; 
   }
   
   @GET 
   @Path("/comments/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public Response getComment(@PathParam("id") int id){ 
	   try {
		   Comment comment = commentDao.getCommentById(id);
		   return Response.ok(comment).build();
	   } catch(Exception e) {
		   System.out.println("ERROR:"+e.getMessage());
		   return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
	   }
   }
   
   @POST
   @Path("/comments") 
   @Produces(MediaType.APPLICATION_XML) 
   public Response createComment(@FormParam("message") String message, @FormParam("userId") int userId, @FormParam("postId") int postId, @FormParam("commentId") int commentId, @HeaderParam(value="authentificationToken") String headers) throws Exception{
	   try {
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
	       
		   LocalDateTime creationDate = LocalDateTime.now();
		   
		   User user = userDao.getUserById(userId);
		   Post post = postDao.getPostById(postId);
		   Comment commentRefered = new Comment();
		   
		   if(commentId != 0) {
			   commentRefered = commentDao.getCommentById(commentId);
			   if(commentRefered.getPost().getId() == post.getId()) {
				   comment = new Comment(message, creationDate.toString(), user, post , commentRefered);
			   }else {
				   throw new Exception("commentResponse doit coincider avec post_id dans le cas ou il n'est pas null");
			   }
		   }else {
			   //commentRefered.setId(0);
			   comment = new Comment(message, creationDate.toString(), user, post);
		   }
		   
		   commentDao.createComment(comment);
			 

		   return Response.ok().build();
	   } catch(Exception e) {
		   System.out.println("ERROR:"+e.getMessage());
		   return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
	   }
   } 
   
   @PUT 
   @Path("/comments/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public Response updateCommentById(@PathParam("id") int id, @FormParam("message") String message, @HeaderParam(value="authentificationToken") String headers) throws Exception{ 
	   try {
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
	       
		   try {		   
			   updated = commentDao.updateCommentById(id, message); 
			   if (!updated)
				   throw new NotAuthorizedException("Error, Comment can t be updated");
			} catch (Exception e) {
				throw new InstaException("Les parametres ne sont pas corrects (message), veuillez les verifier les parametres entres");
			}
		   
		   Comment comment = commentDao.getCommentById(id);
		   
		   return Response.ok(comment).build();
	   } catch(Exception e) {
		   System.out.println("ERROR:"+e.getMessage());
		   return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
	   }
   } 
   
   @DELETE 
   @Path("/comments/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public Response DeleteComment(@PathParam("id") int id, @HeaderParam(value="authentificationToken") String headers) throws Exception{ 
	   try {
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
	
	             throw new Exception("Invalid token");
	
	       }
		   deleted = commentDao.deleteCommentById(id); 
		   if (!deleted)
			   throw new Exception("Error, Comment can t be deleted");

		   return Response.ok().build();
		   
	   } catch(Exception e) {
		   System.out.println("ERROR:"+e.getMessage());
		   return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
	   }
   }
}
