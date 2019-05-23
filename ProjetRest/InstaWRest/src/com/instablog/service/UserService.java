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
import com.instablog.dao.UserDao;
import com.instablog.model.User;  
@Path("/UserService") 

public class UserService { 

   Connexion cx = new Connexion();	
   UserDao userDao = new UserDao(); 
   
   @GET 
   @Path("/users") 
   @Produces(MediaType.APPLICATION_XML) 
   public Response getUsers() throws InstaException{ 	  
      try {
		   List<User> users = userDao.getAllUsers(); 
		   return Response.ok(users).build();
      } catch(Exception e) {
		   System.out.println("ERROR:"+e.getMessage());
		   return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
	   }
   } 
   
   @GET 
   @Path("/users/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public Response getUser(@PathParam("id") String id) throws Exception{ 
      try {
    	  User user =  userDao.getUserById( Integer.parseInt(id)); 
	      return Response.ok(user).build();
	   } catch(Exception e) {
		   System.out.println("ERROR:"+e.getMessage());
		   return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
	   }
   }
   
   @GET 
   @Path("/users/pseudo/{pseudo}") 
   @Produces(MediaType.APPLICATION_XML) 
   public Response getUserByPseudo(@PathParam("pseudo") String pseudo) throws Exception{ 
      try {
    	  User user = userDao.getUserAccountByPseudo(pseudo); 
	      return Response.ok(user).build();
	   } catch(Exception e) {
		   System.out.println("ERROR:"+e.getMessage());
		   return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
	   }
   }
   
   @POST
   @Path("/users/login") 
   @Produces(MediaType.APPLICATION_XML) 
   public Response login(@FormParam("pseudo") String pseudo, @FormParam("password") String password) throws Exception{ 
	   // Authenticate the user using the credentials provided
	   try {
		   String token = null;
	       System.out.println("authentification begin");
	       int id = authenticate(pseudo, password);
	       System.out.println("authentification done");
	       
	       if (id == 0){
	           throw new SecurityException("Invalid user/password id = 0");
	       }

	       // Issue a token for the user
	       token = TokenManagement.generateToken(id,userDao);
	       System.out.println("token created");
	       System.out.println("token : "+ token);
	       return Response.ok(token).build();
		   
	   } catch(Exception e) {
		   System.out.println("ERROR:"+e.getMessage());
		   return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
	   }
   } 
   
   private int authenticate(String pseudo, String password) throws Exception {
       int id = 0;
       User user = userDao.getUserAccountByPseudo(pseudo);
       if (user == null || !user.getPassword().equals(password)){
           throw new SecurityException("Invalid pseudo/password");
       }else{
           id = user.getId();
       }
       return id;
   }
   
   @DELETE
   @Path("/users/logout/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public Response logout(@PathParam("id") int id, @HeaderParam(value="authentificationToken") String headers) throws Exception{ 
	   try {
	   
		   String UUID ="";
		   try{
	           User userBD = userDao.getUserById(id);
	           UUID = userBD.getUUID();
	       }catch(Exception e){
	           throw new Exception("Vous n'�tes pas connect�.");
	       }
	       
	       //headers.getHeaderString("authentificationToken")
	       if (!TokenManagement.verifyToken(headers,UUID)) {
	
	             throw new NotAuthorizedException("Le token est invalide.");
	       }
		   
		   if (id == 0){
	           throw new SecurityException("Invalid user/password id = 0");
	       }      
	       try{
	    	   if(userDao.getUserById(id).getUUID()!=null)
	    	   userDao.changeUserUUID(id, null);
	       }catch(Exception e){
	           throw new Exception("Le user ne peut pas �tre deconnect�");
	       }
	       
		   String message = "User "+id+" est maintenant deconnect�";
		   return Response.status(Response.Status.OK).entity(message).build();
		   
	   } catch(Exception e) {
		   System.out.println("ERROR:"+e.getMessage());
		   return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
	   }
   } 
   
   @DELETE
   @Path("/users/logout/") 
   @Produces(MediaType.APPLICATION_XML) 
   public Response logoutAllUser() throws Exception{     
	   try {
	       try{
	    	   userDao.logoutAllUser();
	       }catch(Exception e){
	           throw new Exception("L'ensemble des user ne peut �tre deconnect�");
	       }  	   

		   String message = "Tous les utilisateurs sont maintenant deconnect�s";;
		   return Response.status(Response.Status.OK).entity(message).build();
	   } catch(Exception e) {
		   System.out.println("ERROR:"+e.getMessage());
		   return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
	   }
   } 
   
   @POST
   @Path("/users") 
   @Produces(MediaType.APPLICATION_XML) 
   public Response createUser(@FormParam("firstname") String firstname, @FormParam("lastname") String lastname, @FormParam("pseudo") String pseudo, @FormParam("password") String password) throws InstaException{ 
	   try {
		   User user = null;
		   if(userDao.existe(pseudo)) {
			   throw new RuntimeException("Un user possedant ce pseudo et ce password existe deja");
		   }else {
			   user = new User(firstname, lastname, pseudo, password);
			   LocalDateTime creationDate = LocalDateTime.now();
			   user.setDateCreation(creationDate.toString());
			   userDao.createUser(user);
		   }
		   return Response.ok(user).build();
	   } catch(Exception e) {
		   System.out.println("ERROR:"+e.getMessage());
		   return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
	   }
   } 
   
   @PUT 
   @Path("/users/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public Response updateUserById(@PathParam("id") int id,@FormParam("firstname") String firstname, @FormParam("lastname") String lastname,  @FormParam("pseudo") String pseudo, @HeaderParam(value="authentificationToken") String headers) throws Exception{ 
	   try {
		   boolean updated = false;
		   String UUID ="";
		   try{
	           User userBD = userDao.getUserById(id);
	           UUID = userBD.getUUID();
	       }catch(Exception e){
	           throw new Exception("L id user du token ne correspond � aucun utilisateur");
	       }
	       
	       //headers.getHeaderString("authentificationToken")
	       if (!TokenManagement.verifyToken(headers,UUID)) {
	
	             throw new NotAuthorizedException("Invalid token");
	       }
		   updated = userDao.updateUserById( id, firstname, lastname, pseudo); 
		   
		   if (!updated)
			   throw new Exception("L id user du token ne correspond � aucun utilisateur");
		   
		   User user =  userDao.getUserById(id);
		   
		   return Response.ok(user).build();
		   
	   } catch(Exception e) {
		   System.out.println("ERROR:"+e.getMessage());
		   return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
	   }
   } 
   
   @DELETE 
   @Path("/users/{id}") 
   @Produces(MediaType.APPLICATION_XML) 
   public Response deleteUser(@PathParam("id") String id, @HeaderParam(value="authentificationToken") String headers) throws Exception{ 
	   try {
		   boolean deleted = false;
		   String UUID ="";
		   try{
	           User userBD = userDao.getUserById(Integer.parseInt(id));
	           UUID = userBD.getUUID();
	       }catch(Exception e){
	           throw new Exception("Le userIDMember du token ne correspond � aucun utilisateur");
	       }

	       if (!TokenManagement.verifyToken(headers,UUID))
	             throw new NotAuthorizedException("Invalid token");

		   deleted = userDao.deleteUserById( Integer.parseInt(id)); 
		   if (!deleted) 
			   throw new Exception("L'utilisateur ne peut pas �tre supprim�");

		   return Response.ok().build();
		   
	   } catch(Exception e) {
		   System.out.println("ERROR:"+e.getMessage());
		   return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
	   }
   } 
}