package com.instablog.service;

import java.time.LocalDateTime;
import java.util.List;

import com.instablog.connexion.*;
import com.instablog.dao.*;
import com.instablog.model.*;


public class CommentService { 
	
   CommentDao commentDao = new CommentDao();
   PostDao postDao = new PostDao();
   UserDao userDao = new UserDao();
   
   public List<Comment> comments() throws InstaException{ 
      return commentDao.getAllComments(); 
   }  
   
   public List<Comment> commentsUser(int userId) throws NumberFormatException, InstaException{ 
	   List<Comment> commentList;
	   try {
		   commentList = commentDao.getAllCommentsUserId(userId);
	} catch (Exception e) {
		throw new RuntimeException(e.getMessage());
	}
      return commentList; 
   }
   
   public List<Comment> commentsPost(int postId) throws NumberFormatException, InstaException{ 
	   List<Comment> commentList;
	   try {
		   commentList = commentDao.getAllCommentsPostId(postId);
	} catch (Exception e) {
		throw new RuntimeException(e.getMessage());
	}
      return commentList; 
   }
   
   public List<Comment> commentsComment(int commentId) throws NumberFormatException, InstaException{ 
	   List<Comment> commentList;
	   try {
		   commentList = commentDao.getAllCommentsAnswer(commentId);
	} catch (Exception e) {
		throw new RuntimeException(e.getMessage());
	}
      return commentList; 
   }
   
   
   public Comment comment(int id){ 
	   Comment comment;
	   try {
		   comment = commentDao.getCommentById(id);
	} catch (Exception e) {
		throw new RuntimeException(e.getMessage());
	}
      return comment; 
   }
   
   public Comment create( String message, int userId, int postId, int commentId,String authentificationToken) throws Exception{
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
       if (!TokenManagement.verifyToken(authentificationToken,UUID)) {

             throw new Exception("Invalid token");

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
   
   public String update(int id, String message, String authentificationToken) throws Exception{ 
	   boolean updated = false;
	   String UUID ="";
	   try{
		   Comment comment = commentDao.getCommentById(id);
		   System.out.println(comment.getId());
           User userBD = userDao.getUserById(comment.getUser().getId());
           System.out.println(userBD.toString());
           UUID = userBD.getUUID();
       }catch(Exception e){
           throw new Exception("Le userIDMember du token ne correspond à aucun utilisateur");
       }
       
       //headers.getHeaderString("authentificationToken")
       if (!TokenManagement.verifyToken(authentificationToken,UUID)) {

             throw new Exception("Invalid token");

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
   
   public String delete(int id, String authentificationToken) throws Exception{ 
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
       if (!TokenManagement.verifyToken(authentificationToken,UUID)) {

             throw new Exception("Invalid token");

       }
	   String response = "Error, Comment can t be deleted ";
	   deleted = commentDao.deleteCommentById(id); 
	   if (deleted) {
		   response = "Comment " + id + " delete";
	   }
	   return response;
   } 
}
