package com.instablog.service;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.instablog.dao.*;
import com.instablog.model.*;

@Path("/CommentService") 

public class CommentService {  
   CommentDao commentDao = new CommentDao();  
   
   @GET 
   @Path("/comments") 
   @Produces(MediaType.APPLICATION_XML) 
   public List<Comment> getComments(){ 
      return commentDao.getAllComments(); 
   }  
   
   
   @GET 
   @Path("/comments/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public Comment getComment(@PathParam("id") String id){ 
      return commentDao.getCommentById( Integer.parseInt(id)); 
   }
   
   @POST
   @Path("/comments") 
   @Produces(MediaType.APPLICATION_XML) 
   public String createPost(String comment){ 
	   commentDao.createComment(comment);
	   return comment;
   } 
   
   @PUT 
   @Path("/comments/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public String updateCommentById(@PathParam("id") String id){ 
	   boolean updated = false;
	   String response = "Error, Comment can t be updated ";
	   updated = commentDao.updateCommentById( Integer.parseInt(id)); 
	   if (updated) {
		   response = "Comment " + id + " updated";
	   }
	   return response;
   } 
   
   @DELETE 
   @Path("/comments/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public String DeleteComment(@PathParam("id") String id){ 
	   boolean deleted = false;
	   String response = "Error, Comment can t be deleted ";
	   deleted = commentDao.deleteCommentById( Integer.parseInt(id)); 
	   if (deleted) {
		   response = "Comment " + id + " delete";
	   }
	   return response;
   } 
}
