package com.instablog.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.instablog.connexion.EMFManager;
import com.instablog.connexion.InstaException;
import com.instablog.model.*;

public class CommentDao {

	private EntityManager em;
	
	public CommentDao() {
		this.em = EMFManager.getEntityManager(); 
	}
	
	public List<Comment> getAllComments() throws InstaException{ 
	      
	      List<Comment> commentList = null; 
	      this.em = EMFManager.getEntityManager();
			try {
				this.em.getTransaction().begin();
				Query query = em.createQuery("select c from comment c where isActive = true") ;  
				commentList = query.getResultList() ;
				// Commit
				this.em.getTransaction().commit();
				this.em.close();
			} catch (Exception e) {
				this.em.getTransaction().rollback();
				throw new InstaException("les comments n ont pas pu etre affiche");
			}
	      return commentList; 
	   }

	public Comment getCommentById(int id) throws InstaException {
		// Recherche comment correspondant dans la BD et le renvoie
				this.em = EMFManager.getEntityManager();
				Comment comment = null ;
				try {
					this.em.getTransaction().begin();
					Query query = em.createQuery("select c from comment c where id = '" + id +  "' and isActive = true") ; 
					
					List <Comment> listComment = query.getResultList() ;
					comment = listComment.get(0);
					//Recherche liste comment associer
					/*List <Comment> listComment = query2.getResultList();
					comment.setListeComments(listComment);*/
					// Commit
					this.em.getTransaction().commit();
					this.em.close();
				} catch (Exception e) {
					this.em.getTransaction().rollback();
					throw new InstaException("Comment n'a pas pu être affiche");
				}
				return comment;
	}

	public List<Comment> getAllCommentsUserId(int id) {
		List<Comment> commentList = null;
		this.em = EMFManager.getEntityManager();
		//User user = null ;
		try {
			this.em.getTransaction().begin();
			Query query = em.createQuery("select c from comment c where user_id = '" + id +"' and isActive = true") ;  
			commentList = query.getResultList() ;
			// Commit
			this.em.getTransaction().commit();
			this.em.close();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
			throw e;
		}
		return commentList;
	} 
	
	public List<Comment> getAllCommentsPostId(int id) {
		List<Comment> commentList = null;
		this.em = EMFManager.getEntityManager();
		//User user = null ;
		try {
			this.em.getTransaction().begin();
			Query query = em.createQuery("select c from comment c where post_id = '" + id +"' and isActive = true") ;  
			commentList = query.getResultList() ;
			// Commit
			this.em.getTransaction().commit();
			this.em.close();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
			throw e;
		}
		return commentList;
	}
	
	public List<Comment> getAllCommentsAnswer(int id) {
		List<Comment> commentList = null;
		this.em = EMFManager.getEntityManager();
		//User user = null ;
		try {
			this.em.getTransaction().begin();
			Query query = em.createQuery("select c from comment c where commentResponse_id = '" + id +"' and isActive = true") ;  
			commentList = query.getResultList() ;
			// Commit
			this.em.getTransaction().commit();
			this.em.close();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
			throw e;
		}
		return commentList;
	}
	
	public Comment createComment(Comment comment) throws InstaException {
		this.em = EMFManager.getEntityManager();
		try {
			this.em.getTransaction().begin();
			this.em.persist(comment);
			// Commit
			this.em.getTransaction().commit();
			this.em.close();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
			throw new InstaException("Le comment n'a pas pu être cree");
		}
		return comment;
	}

	public boolean updateCommentById(int id, String message) throws InstaException {
		boolean updated = false;
		this.em = EMFManager.getEntityManager();
		try {
			this.em.getTransaction().begin();
			Query query = em.createQuery("update comment set message = '" + message + "' where id = " + id ) ;
		
			query.executeUpdate();
			updated = true;
			// Commit
			this.em.getTransaction().commit();
			this.em.close();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
			throw new InstaException("Le comment "+ id +" n a pas pu être update");
		}
		return updated;
	}

	public boolean deleteCommentById(int id) throws InstaException {
		boolean deleted = false;
		this.em = EMFManager.getEntityManager();
		try {
			this.em.getTransaction().begin();
			//Query query = em.createQuery("update user set isActive = false where id = '" + id +  "'") ;
			Query query = em.createQuery("update comment set isActive = false where id = '" + id +  "' or commentResponse_id = '"+ id +"'") ;
			//Query query2 = em.createQuery("update comment set isActive = false where post_id = '" + id +  "'") ;
			query.executeUpdate();
			//query2.executeUpdate();
			//query3.executeUpdate();
			deleted = true;
			// Commit
			this.em.getTransaction().commit();
			this.em.close();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
			throw new InstaException("Le comment "+ id +" n a pas pu etre supprime");
		}
		return deleted;
	}
	
}
