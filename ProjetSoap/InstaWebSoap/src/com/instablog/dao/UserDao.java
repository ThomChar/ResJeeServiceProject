package com.instablog.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.instablog.connexion.*;
import com.instablog.model.*;


public class UserDao {
	
	private EntityManager em;
	
	public UserDao() {
		this.em = EMFManager.getEntityManager(); 
	}
	
	public List<User> getAllUsers() throws InstaException {
		List<User> userList = null;
		this.em = EMFManager.getEntityManager();
		try {
			this.em.getTransaction().begin();
			Query query = em.createQuery("select u from user u where isActive = true") ;  
			userList = query.getResultList() ;
			// Commit
			this.em.getTransaction().commit();
			this.em.close();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
			throw new InstaException("Les User n ont pas pu etre affiche");
		}
		return userList;
	}

	public User getUserById(int id) throws InstaException {
		// Recherche User correspondant dans la BD et le renvoie
		this.em = EMFManager.getEntityManager();
		User user = null ;
		List <Post> listePosts = new ArrayList();
		//listePosts.add(new Post());
		List <Comment> listeComments = new ArrayList();
		//listeComments.add(new Comment());
		try {
			this.em.getTransaction().begin();
			Query query = em.createQuery("select u from user u where id = '" + id +  "'") ;  
			Query query2 = em.createQuery("select p from post p where user_id = '" + id +"'") ;
			Query query3 = em.createQuery("select c from comment c where user_id = '" + id +"'") ;
			
			List <User> listUser = query.getResultList() ;
			user = listUser.get(0);
			//Recherche liste post associer
			listePosts = query2.getResultList();
			//System.out.println(listePosts.toString());
			//System.out.println(listePosts);
			//user.setListePosts(listePosts);
			//Recherche liste des commentaire associer
			listeComments =  query3.getResultList();
			//System.out.println(listeComments);
			//user.setListeComments(listeComments);
			// Commit
			this.em.getTransaction().commit();
			this.em.close();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
			throw new InstaException("User n'a pas pu être charge");
		}
		return user;
	}

	
	public boolean deleteUserById(int id) throws InstaException {
		boolean deleted = false;
		this.em = EMFManager.getEntityManager();
		try {
			this.em.getTransaction().begin();
			Query query = em.createQuery("update user set isActive = false where id = '" + id +  "'") ;
			Query query2 = em.createQuery("update post set isActive = false where user_id = '" + id +  "'") ;
			Query query3 = em.createQuery("update comment set isActive = false where user_id = '" + id +  "'") ;
			query.executeUpdate();
			query2.executeUpdate();
			query3.executeUpdate();
			deleted = true;
			// Commit
			this.em.getTransaction().commit();
			this.em.close();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
			throw new InstaException("user na pas pu être supprime");
		}
		return deleted;
	}

	public boolean updateUserById(int id, String firstname, String lastname, String pseudo) throws InstaException {
		boolean updated = false;
		this.em = EMFManager.getEntityManager();
		try {
			this.em.getTransaction().begin();
			Query query = em.createQuery("update user set firstname = '" + firstname + "' , lastname = '" + lastname + 
										"' , pseudo = '" + pseudo + "' where id = '" + id +  "'") ;
			query.executeUpdate();
			updated = true;
			// Commit
			this.em.getTransaction().commit();
			this.em.close();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
			throw new InstaException("user n a pas pu être update");
		}
		return updated;
	}

	/**
	 * methode permettant de créer un nouvel utilisateur dans la base de donnée
	 * @param user
	 * @return
	 * @throws InstaException 
	 */
	public User createUser(User user) throws InstaException {
		//Creation du user dans la base de donnnee
		this.em = EMFManager.getEntityManager();
		try {
			this.em.getTransaction().begin();
			this.em.persist(user);
			// Commit
			this.em.getTransaction().commit();
			this.em.close();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
			throw new InstaException("user n'a pas pu être cree");
		}
		return user;
	}

	public void changeUserUUID(int id, String randomUUIDString) throws InstaException {
		this.em = EMFManager.getEntityManager();
		try {
			this.em.getTransaction().begin();
			Query query;
			if(randomUUIDString == null) {
			query = em.createQuery("update user set UUID = NULL where id = '" + id +  "'") ;
			}else {query = em.createQuery("update user set UUID = '" + randomUUIDString + "' where id = '" + id +  "'") ;}
			
			query.executeUpdate();
			// Commit
			this.em.getTransaction().commit();
			this.em.close();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
			throw new InstaException("UUID n'a pas pu être change");
		}
	}

	public boolean existe(String pseudo) throws InstaException {
		// Cherche dans la base de donnée si une personne ayant pseudo et password existe
		this.em = EMFManager.getEntityManager(); 
		boolean find = false;
		//User
		this.em.getTransaction().begin();
		Query query = em.createQuery("select u from user u where pseudo = '" + pseudo +  "'") ;  
		List<User> listUser = query.getResultList() ;
		if(listUser.size()!=0)find = true;
		// Commit
		//em.getTransaction().commit();
		this.em.close();

		return find;
	}
	
	public User getUserAccountByLoginPassword(String pseudo, String password) throws InstaException {
		// Cherche dans la base de donnée si une personne ayant pseudo et password existe
		this.em = EMFManager.getEntityManager(); 
		User user;
		//User
		try {
			this.em.getTransaction().begin();
			Query query = em.createQuery("select u from user u where pseudo = '" + pseudo +  "' and password = '" + password + "'") ;  
			List<User> listUser = query.getResultList() ;
			user = listUser.get(0);
			// Commit
			//em.getTransaction().commit();
			this.em.close();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
			throw new InstaException("La recherche du user n a pas pu etre effectuee");
		}
		return user;
	}

	public void logoutAllUser() throws InstaException {
		this.em = EMFManager.getEntityManager();
		try {
			this.em.getTransaction().begin();
			Query query = em.createQuery("update user set UUID = NULL") ;
			query.executeUpdate();
			// Commit
			this.em.getTransaction().commit();
			this.em.close();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
			throw new InstaException("UUID n'a pas pu être change pour tous les utilisateurs");
		}
	}
}