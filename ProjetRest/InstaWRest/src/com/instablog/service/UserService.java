package com.instablog.service;  

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.instablog.connexion.Connexion;
import com.instablog.connexion.InstaException;
import com.instablog.connexion.TokenManagement;
import com.instablog.dao.UserDao;
import com.instablog.model.User;  
@Path("/UserService") 

public class UserService { 

   Connexion cx = new Connexion();	
   UserDao userDao = new UserDao(); 
   
   @GET 
   @Path("/users") 
   @Produces(MediaType.APPLICATION_XML) 
   public List<User> getUsers() throws InstaException{ 	  
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
   @Path("/users/login") 
   @Produces(MediaType.APPLICATION_XML) 
   public String login(@FormParam("pseudo") String pseudo, @FormParam("password") String password) throws Exception{ 
	   // Authenticate the user using the credentials provided
       System.out.println("authentification begin");
       int id = authenticate(pseudo, password);
       System.out.println("authentification done");
       
       if (id == 0){
           throw new SecurityException("Invalid user/password id = 0");
       }
       
       // Issue a token for the user
       String token = TokenManagement.generateToken(id,userDao);
       System.out.println("token created");
       System.out.println("token : "+ token);
	   
	   return token;
   } 
   
   private int authenticate(String pseudo, String password) throws Exception {
       int id = 0;
       User user = userDao.getUserAccountByLoginPassword(pseudo, password);
       if (user == null){
           throw new SecurityException("Invalid pseudo/password");
       }else{
           id = user.getId();
       }
       return id;
   }
   
   @POST
   @Path("/users/logout/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public String logout(@PathParam("id") int id, @HeaderParam(value="authentificationToken") String headers) throws Exception{ 
	   String UUID ="";
	   try{
           User userBD = userDao.getUserById(id);
           UUID = userBD.getUUID();
       }catch(Exception e){
           throw new Exception("L id user du token ne correspond à aucun utilisateur");
       }
       
       //headers.getHeaderString("authentificationToken")
       if (!TokenManagement.verifyToken(headers,UUID)) {

             throw new NotAuthorizedException("Invalid token");

       }
	   
	   if (id == 0){
           throw new SecurityException("Invalid user/password id = 0");
       }      
       try{
    	   if(userDao.getUserById(id).getUUID()!=null)
    	   userDao.changeUserUUID(id, null);
       }catch(Exception e){
           throw new Exception("Le user ne peut pas être deconnecté");
       }  	   
	   return "User "+id+" est maintenant deconnecté";
   } 
   
   @POST
   @Path("/users/logout/") 
   @Produces(MediaType.APPLICATION_XML) 
   public String logoutAllUser() throws Exception{     
       try{
    	   userDao.logoutAllUser();
       }catch(Exception e){
           throw new Exception("L'ensemble des user ne peut être deconnecté");
       }  	   
	   return "Tous les utilisateurs sont maintenant deconnectés";
   } 
   
   @POST
   @Path("/users") 
   @Produces(MediaType.APPLICATION_XML) 
   public User createUser(@FormParam("firstname") String firstname, @FormParam("lastname") String lastname, @FormParam("pseudo") String pseudo, @FormParam("password") String password) throws InstaException{ 
	   User user = null;
	   if(userDao.existe(pseudo,password)) {
		   throw new InstaException("Un user possedant ce pseudo et ce password existe deja");
	   }else {
		   user = new User(firstname, lastname, pseudo, password);
		   LocalDateTime creationDate = LocalDateTime.now();
		   user.setDateCreation(creationDate.toString());
		   userDao.createUser(user);
	   }
	   
	   return user;
   } 
   
   @PUT 
   @Path("/users/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public String updateUserById(@PathParam("id") int id,@FormParam("firstname") String firstname, @FormParam("lastname") String lastname,  @FormParam("pseudo") String pseudo, @HeaderParam(value="authentificationToken") String headers) throws Exception{ 
	   boolean updated = false;
	   String UUID ="";
	   try{
           User userBD = userDao.getUserById(id);
           UUID = userBD.getUUID();
       }catch(Exception e){
           throw new Exception("L id user du token ne correspond à aucun utilisateur");
       }
       
       //headers.getHeaderString("authentificationToken")
       if (!TokenManagement.verifyToken(headers,UUID)) {

             throw new NotAuthorizedException("Invalid token");

       }
	   String response = "Error, User can t be updated ";
	   updated = userDao.updateUserById( id, firstname, lastname, pseudo); 
	   if (updated) {
		   response = "User " + id + " updated";
	   }
	   return response;
   } 
   
   @DELETE 
   @Path("/users/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public String deleteUser(@PathParam("id") String id, @HeaderParam(value="authentificationToken") String headers) throws Exception{ 
	   boolean deleted = false;
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
	   String response = "Error, User can t be deleted ";
	   deleted = userDao.deleteUserById( Integer.parseInt(id)); 
	   if (deleted) {
		   response = "User " + id + " delete";
	   }
	   return response;
   } 
}