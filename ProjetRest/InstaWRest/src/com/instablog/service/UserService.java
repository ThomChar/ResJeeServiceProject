package com.instablog.service;  

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces; 
import javax.ws.rs.core.MediaType;

import com.instablog.connexion.TokenManagement;
import com.instablog.dao.UserDao;
import com.instablog.model.User;  
@Path("/UserService") 

public class UserService { 
	
   UserDao userDao = new UserDao(); 
   
   @GET 
   @Path("/users") 
   @Produces(MediaType.APPLICATION_XML) 
   public List<User> getUsers(){ 
      return userDao.getAllUsers(); 
   } 
   
   @GET 
   @Path("/users/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public User getUser(@PathParam("id") String id, @HeaderParam(value="authentificationToken") String headers) throws Exception{ 
	   String UUID ="";
       
       try{
           User userBD = userDao.getUserById(Integer.parseInt(id));
           UUID = userBD.getUUID();
       }catch(Exception e){
           throw new Exception("Le userIDMember du token ne correspond à aucun utilisateur");
       }
       
       //headers.getHeaderString("authentificationToken")
       if (!TokenManagement.verifyToken(headers,UUID)) {

             throw new NotAuthorizedException("Invalid token");

       }
      return userDao.getUserById( Integer.parseInt(id)); 
   }
   
   @POST
   @Path("/users") 
   @Produces(MediaType.APPLICATION_JSON) 
   public String createUser(String user){ 
	   userDao.createUser(user);
	   return user;
   } 
   
   @PUT 
   @Path("/users/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public String updateUserById(@PathParam("id") String id){ 
	   boolean updated = false;
	   String response = "Error, User can t be updated ";
	   updated = userDao.updateUserById( Integer.parseInt(id)); 
	   if (updated) {
		   response = "User " + id + " updated";
	   }
	   return response;
   } 
   
   @DELETE 
   @Path("/users/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public String DeleteUser(@PathParam("id") String id){ 
	   boolean deleted = false;
	   String response = "Error, User can t be deleted ";
	   deleted = userDao.deleteUserById( Integer.parseInt(id)); 
	   if (deleted) {
		   response = "User " + id + " delete";
	   }
	   return response;
   } 
}