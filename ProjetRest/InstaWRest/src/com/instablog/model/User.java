package com.instablog.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@XmlRootElement(name = "user")
@Entity(name = "user")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String firstname;
	private String lastname;
	private String pseudo;
	private String password;
	private boolean isActive;
	private String creationDate;
	private String UUID;

	@OneToMany( mappedBy = "user", cascade =  CascadeType.ALL )
	  private Collection< Post > listePosts;
	
	@OneToMany( mappedBy = "user", cascade =  CascadeType.ALL )
	  private Collection< Comment > listeComments;
	
	public User() {
	}

	public User(int id, String firstname, String lastname, String pseudo, String password) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.pseudo = pseudo;
		this.password = password;
	}

	public User(String firstname, String lastname, String pseudo, String password) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.pseudo = pseudo;
		this.password = password;
		this.isActive = true;
		this.UUID = null;
	}

	public String getFirstname() {
		return firstname;
	}

	@XmlElement
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public boolean isActive() {
		return isActive;
	}

	@XmlTransient
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getDateCreation() {
		return creationDate;
	}

	@XmlElement
	public void setDateCreation(String dateCreation) {
		this.creationDate = dateCreation;
	}

	public int getId() {
		return id;
	}

	@XmlElement
	public void setId(int id) {
		this.id = id;
	}


	public String getLastname() {
		return lastname;
	}

	@XmlElement
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPseudo() {
		return pseudo;
	}

	@XmlElement
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getPassword() {
		return password;
	}

	@XmlTransient
	public void setPassword(String password) {
		this.password = password;
	}

	public String getUUID() {
		return UUID;
	}

	@XmlTransient
	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public Collection<Post> getListePosts() {
		return listePosts;
	}

	@XmlElement
	public void setListePosts(List<Post> listePosts) {
		this.listePosts = listePosts;
	}

	public Collection<Comment> getListeComments() {
		return listeComments;
	}

	@XmlElement
	public void setListeComments(List<Comment> listeComments) {
		this.listeComments = listeComments;
	}

	/*@Override
	public String toString() {
		return "User [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", pseudo=" + pseudo
				+ ", password=" + password + ", isActive=" + isActive + ", creationDate=" + creationDate + ", UUID="
				+ UUID + ", listePosts=" + listePosts + ", listeComments=" + listeComments + "]";
	}*/
	
	@Override
	public String toString() {
		return "User [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", pseudo=" + pseudo
				+ ", password=" + password + ", isActive=" + isActive + ", creationDate=" + creationDate + ", UUID="
				+ UUID + "]";
	}

}