package com.instablog.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.instablog.connexion.EMFManager;
import com.instablog.connexion.InstaException;
import com.instablog.model.*;

public class PostDao {

	private EntityManager em;
	
	public PostDao() {
		this.em = EMFManager.getEntityManager(); 
	}
	
	public List<Post> getAllPosts() throws InstaException{ 
	      
	      List<Post> postList = null; 
	      List<Comment> commentList = new ArrayList<>(); 
	      Query query2;
	      this.em = EMFManager.getEntityManager();
			try {
				this.em.getTransaction().begin();
				Query query = em.createQuery("select p from post p where isActive = true") ;
				postList = query.getResultList() ;
				/*for(Post post : postList) {
					//System.out.println(post.getId());
					query2 = em.createQuery("select c from comment c where post_id = " + post.getId()) ;
					commentList = query2.getResultList();
					//System.out.println(commentList.toString());
					//if (commentList.size()!=0)System.out.println(commentList.toString());
					post.setListeComments(commentList);
				}*/
				// Commit
				this.em.getTransaction().commit();
				this.em.close();
			} catch (Exception e) {
				this.em.getTransaction().rollback();
				//throw new InstaException("les posts n ont pas pu etre affiche");
				throw e;
			}
	      return postList; 
	   }

	public List<Post> getAllPostsUserId(int id) {

		//em
		List<Post> postList = null;
		this.em = EMFManager.getEntityManager();
		//User user = null ;
		try {
			this.em.getTransaction().begin();
			Query query = em.createQuery("select p from post p where user_id = '" + id +"' and isActive = true") ;  
			postList = query.getResultList() ;
			// Commit
			this.em.getTransaction().commit();
			this.em.close();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
			throw e;
		}
		return postList;
	}
	
	public Post getPostById(int id) throws InstaException {
		// Recherche User correspondant dans la BD et le renvoie
				this.em = EMFManager.getEntityManager();
			Post post = null ;
				try {
					//System.out.println("hello");
					this.em.getTransaction().begin();
					//System.out.println(post.toString());
					Query query = em.createQuery("select p from post p where id = " + id + " and isActive = true") ;  
					//Query query2 = em.createQuery("select c from comment c where post_id = '" + id +"'") ;
					//Query query3 = em.createQuery("select u from user u where id = '" + id +"'") ;
					List <Post> listPost = query.getResultList() ;
					post = listPost.get(0);
					//System.out.println(post.toString());
					//Recherche liste comment associer
					//List <Comment> listeComments = query2.getResultList();
					//post.setListeComments(listeComments);
					//Recherche liste des commentaire associer
					//List <Comment> listeComments =  query3.getResultList();
					//user.setListeComments(listeComments);
					// Commit
					this.em.getTransaction().commit();
					this.em.close();
				} catch (Exception e) {
					this.em.getTransaction().rollback();
					throw new InstaException("Le post "+ id +" n a pas pu etre affiche");
				}
				return post;
	}

	public Post createPost(Post post) throws InstaException {
		this.em = EMFManager.getEntityManager();
		try {
			this.em.getTransaction().begin();
			this.em.persist(post);
			// Commit
			this.em.getTransaction().commit();
			this.em.close();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
			throw new InstaException("Le post n'a pas pu être cree");
		}
		return post;
	}

	public boolean updatePostById(int id, String comment, String image/*, User user*/) throws InstaException {
		boolean updated = false;
		this.em = EMFManager.getEntityManager();
		try {
			this.em.getTransaction().begin();
			Query query = em.createQuery("update post set comment = '" + comment + "' , image = '" + image + 
					"' where id = '" + id +  "'") ;
			if(comment == null && image == null) {
				query = em.createQuery("update post set comment = NULL , image = NULL where id = '" + id +  "'") ;
			}else if(comment == null) {
				query = em.createQuery("update post set comment = NULL , image = '" + image +"' where id = '" + id +  "'") ;
			}else if(image == null) {
				query = em.createQuery("update post set comment = '" + comment + "' , image = NULL where id = '" + id +  "'") ;
			}
			
			query.executeUpdate();
			updated = true;
			// Commit
			this.em.getTransaction().commit();
			this.em.close();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
			throw new InstaException("Le post "+ id +" n a pas pu être update");
		}
		return updated;
	}

	public boolean deletePostById(int id) throws InstaException {
		boolean deleted = false;
		this.em = EMFManager.getEntityManager();
		try {
			this.em.getTransaction().begin();
			//Query query = em.createQuery("update user set isActive = false where id = '" + id +  "'") ;
			Query query = em.createQuery("update post set isActive = false where id = '" + id +  "'") ;
			Query query2 = em.createQuery("update comment set isActive = false where post_id = '" + id +  "'") ;
			query.executeUpdate();
			query2.executeUpdate();
			//query3.executeUpdate();
			deleted = true;
			// Commit
			this.em.getTransaction().commit();
			this.em.close();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
			throw new InstaException("Le post "+ id +" n a pas pu etre supprime");
		}
		return deleted;
	} 
}
