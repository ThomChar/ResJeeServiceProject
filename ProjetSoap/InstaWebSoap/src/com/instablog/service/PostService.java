package com.instablog.service;

import java.time.LocalDateTime;
import java.util.List;

import com.instablog.connexion.*;
import com.instablog.dao.*;
import com.instablog.model.*;


public class PostService {  
	
   PostDao postDao = new PostDao();
   UserDao userDao = new UserDao();
   
  
   public List<Post> posts() throws InstaException{ 
      return postDao.getAllPosts(); 
   }  
   
   public List<Post> postsUser(int userId) throws NumberFormatException, InstaException{ 
	   List<Post> postList;
	   try {
		   postList = postDao.getAllPostsUserId(userId);
	} catch (Exception e) {
		throw new RuntimeException(e.getMessage());
	}
      return postList; 
   }
   
   public Post post(int id) throws NumberFormatException, InstaException{ 
	   Post post;
	   try {
		post = postDao.getPostById(id);
	} catch (Exception e) {
		throw new RuntimeException(e.getMessage());
	}
      return post; 
   }
   
   public Post create(String comment, String image, int userId, String authentificationToken) throws Exception{ 
	   Post post = null;
	   String UUID ="";
	   try{
           User userBD = userDao.getUserById(userId);
           UUID = userBD.getUUID();
       }catch(Exception e){
           throw new Exception("Le userIDMember du token ne correspond à aucun utilisateur");
       }
       
       if (!TokenManagement.verifyToken(authentificationToken,UUID)) {

             throw new Exception("Invalid token");

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
   
   public String update( int id,  String comment, String image, String authentificationToken) throws Exception{ 
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
       if (!TokenManagement.verifyToken(authentificationToken,UUID)) {

             throw new Exception("Invalid token");

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
   
   
   public String delete(int id, String authentificationToken) throws Exception{ 
	   boolean deleted = false;
	   String UUID ="";
	   try{
		   Post post = postDao.getPostById(id);
           User userBD = post.getUser();
           UUID = userBD.getUUID();
       }catch(Exception e){
           throw new Exception("Le userIDMember du token ne correspond à aucun utilisateur");
       }
       
       if (!TokenManagement.verifyToken(authentificationToken,UUID)) {

             throw new Exception("Invalid token");

       }
	   String response = "Error, Post can t be deleted ";
	   deleted = postDao.deletePostById(id); 
	   if (deleted) {
		   response = "Post " + id + " delete";
	   }
	   return response;
   } 
}
