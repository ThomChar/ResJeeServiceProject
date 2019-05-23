package com.instablog.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "post")
@Entity(name = "post")
public class Post implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String image;
	private String comment;
	private String postDate;
	private boolean isActive;

	@ManyToOne(fetch = FetchType.EAGER)
	private User user;
	
	/*@OneToMany( mappedBy = "post", cascade =  CascadeType.ALL )
	private Collection< Comment > listeComments;*/
	
	public Post() {
	}

	public Post(int id, String image, String comment, String postDate) {
		this.id = id;
		this.image = image;
		this.comment = comment;
		this.postDate = postDate;
		this.isActive = true;
	}

	public Post(String image, String comment, String postDate, User user) {
		this.image = image;
		this.comment = comment;
		this.postDate = postDate;
		this.user = user;
		this.isActive = true;
	}
	
	public Post(String image, String comment, String postDate) {
		this.image = image;
		this.comment = comment;
		this.postDate = postDate;
		this.isActive = true;
	}

	public int getId() {
		return id;
	}

	@XmlElement
	public void setId(int id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	@XmlElement
	public void setImage(String image) {
		this.image = image;
	}

	public String getComment() {
		return comment;
	}

	@XmlElement
	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPostDate() {
		return postDate;
	}

	@XmlElement
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public User getUser() {
		return user;
	}

	@XmlElement
	public void setUser(User user) {
		this.user = user;
	}

	/*public Collection<Comment> getListeComments() {
		return listeComments;
	}

	@XmlElement
	public void setListeComments(List<Comment> listeComments) {
		this.listeComments = listeComments;
	}*/

	public boolean isActive() {
		return isActive;
	}

	@XmlElement
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", image=" + image + ", comment=" + comment + ", postDate=" + postDate + ", isActive="
				+ isActive + ", user=" + user + "]";
	}

	
	
}