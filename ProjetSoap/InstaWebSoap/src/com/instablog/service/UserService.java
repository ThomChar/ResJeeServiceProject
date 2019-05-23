package com.instablog.service;  

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.instablog.connexion.*;
import com.instablog.dao.UserDao;
import com.instablog.model.User;  

public class UserService { 
	
   UserDao userDao = new UserDao(); 
      
   public List<User> users() throws InstaException{ 	  
      return userDao.getAllUsers(); 
   } 
   
   public String defaultMessage( String test) { 
      return "Welecome to User Service "+ test;
  }


   public User user(String id) throws Exception { 
      return userDao.getUserById(Integer.parseInt(id)); 
   }
   
   public String login(String pseudo,String password) throws Exception{ 
	   // Authenticate the user using the credentials provided
       System.out.println("authentification begin");
       int id = authenticate(pseudo, password);
       System.out.println("authentification done");
       if (id == 0){
           throw new SecurityException("Invalid user/password id = 0");
       }
       
       User user = userDao.getUserById(id);
       if (!user.isActive()){
           throw new SecurityException("Ce compte a été supprimé vous ne pouvez donc plus vous connecter, veuillez recreer un compte");
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
   
   public String logout(int id,String authentificationToken) throws Exception{ 
	   String UUID ="";
	   try{
           User userBD = userDao.getUserById(id);
           UUID = userBD.getUUID();
       }catch(Exception e){
           throw new Exception("L id user du token ne correspond à aucun utilisateur");
       }
       
       if (!TokenManagement.verifyToken(authentificationToken,UUID)) {

             throw new Exception("Invalid token");

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
   
   public String logoutAllUser() throws Exception{     
       try{
    	   userDao.logoutAllUser();
       }catch(Exception e){
           throw new Exception("L'ensemble des user ne peut être deconnecté");
       }  	   
	   return "Tous les utilisateurs sont maintenant deconnectés";
   } 
   
   public User create(String firstname, String lastname, String pseudo, String password) throws InstaException{ 
	   User user = null;
	   if(userDao.existe(pseudo)) {
		   throw new InstaException("Un user possedant ce pseudo existe deja");
	   }else {
		   user = new User(firstname, lastname, pseudo, password);
		   LocalDateTime creationDate = LocalDateTime.now();
		   user.setDateCreation(creationDate.toString());
		   userDao.createUser(user);
	   }
	   
	   return user;
   } 
   
   public String update( int id, String firstname, String lastname,   String pseudo, String authentificationToken) throws Exception{ 
	   boolean updated = false;
	   String UUID ="";
	   try{
           User userBD = userDao.getUserById(id);
           UUID = userBD.getUUID();
       }catch(Exception e){
           throw new Exception("L id user du token ne correspond à aucun utilisateur");
       }
       
       if (!TokenManagement.verifyToken(authentificationToken,UUID)) {

             throw new Exception("Invalid token");

       }
	   String response = "Error, User can t be updated ";
	   updated = userDao.updateUserById( id, firstname, lastname, pseudo); 
	   if (updated) {
		   response = "User " + id + " updated";
	   }
	   return response;
   } 
   
   public String delete(String id, String authentificationToken) throws Exception{ 
	   boolean deleted = false;
	   String UUID ="";
	   try{
           User userBD = userDao.getUserById(Integer.parseInt(id));
           UUID = userBD.getUUID();
       }catch(Exception e){
           throw new Exception("Le userIDMember du token ne correspond à aucun utilisateur");
       }
       
       if (!TokenManagement.verifyToken(authentificationToken,UUID)) {

             throw new Exception("Invalid token");

       }
	   String response = "Error, User can t be deleted ";
	   String logout = this.logout(Integer.parseInt(id), authentificationToken);
	   deleted = userDao.deleteUserById( Integer.parseInt(id)); 
	   if (deleted) {
		   response = "User " + id + " delete";
	   }
	   return response;
   } 
}