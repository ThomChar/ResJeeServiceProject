package com.instablog.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.instablog.model.*;

public class CommentDao {

	public List<Comment> getAllComments(){ 
	      
	      List<Comment> commentList = null; 
	      		LocalDateTime now = LocalDateTime.now();
	            Comment comment = new Comment(1, "message1", now.toString(), 1, 1, 0);
	            Comment comment2 = new Comment(1, "message2", now.toString(), 2, 2, 0);
	            commentList = new ArrayList<Comment>(); 
	            commentList.add(comment); 
	            commentList.add(comment2);
	          
	      return commentList; 
	   }

	public Comment getCommentById(int parseInt) {
		// TODO Auto-generated method stub
		return null;
	}

	public void createComment(String comment) {
		// TODO Auto-generated method stub
		
	}

	public boolean updateCommentById(int parseInt) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteCommentById(int parseInt) {
		// TODO Auto-generated method stub
		return false;
	} 
}
