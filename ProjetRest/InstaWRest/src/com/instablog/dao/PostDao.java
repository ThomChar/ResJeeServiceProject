package com.instablog.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.instablog.model.*;

public class PostDao {

	public List<Post> getAllPosts(){ 
	      
	      List<Post> postList = null; 
	      		LocalDateTime now = LocalDateTime.now();
	            Post post = new Post(1, "image1", "comment1", now.toString(), 1);
	            Post post2 = new Post(2, "image2", "comment2", now.toString(), 1);
	            postList = new ArrayList<Post>(); 
	            postList.add(post); 
	            postList.add(post2);
	          
	      return postList; 
	   }

	public User getPostById(int parseInt) {
		// TODO Auto-generated method stub
		return null;
	}

	public void createPost(String post) {
		// TODO Auto-generated method stub
		
	}

	public boolean updatePostById(int parseInt) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deletePostById(int parseInt) {
		// TODO Auto-generated method stub
		return false;
	} 
}
