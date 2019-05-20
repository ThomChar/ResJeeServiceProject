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

@Path("/PostService") 

public class PostService {  
   PostDao postDao = new PostDao();
   
   @GET 
   @Path("/posts") 
   @Produces(MediaType.APPLICATION_XML) 
   public List<Post> getPosts(){ 
      return postDao.getAllPosts(); 
   }  
   
   @GET 
   @Path("/posts/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public User getPost(@PathParam("id") String id){ 
      return postDao.getPostById( Integer.parseInt(id)); 
   }
   
   @POST
   @Path("/posts") 
   @Produces(MediaType.APPLICATION_XML) 
   public String createPost(String post){ 
	   postDao.createPost(post);
	   return post;
   } 
   
   @PUT 
   @Path("/posts/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public String updatePostById(@PathParam("id") String id){ 
	   boolean updated = false;
	   String response = "Error, Post can t be updated ";
	   updated = postDao.updatePostById( Integer.parseInt(id)); 
	   if (updated) {
		   response = "Post " + id + " updated";
	   }
	   return response;
   } 
   
   @DELETE 
   @Path("/posts/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public String DeletePost(@PathParam("id") String id){ 
	   boolean deleted = false;
	   String response = "Error, Post can t be deleted ";
	   deleted = postDao.deletePostById( Integer.parseInt(id)); 
	   if (deleted) {
		   response = "Post " + id + " delete";
	   }
	   return response;
   } 
}
