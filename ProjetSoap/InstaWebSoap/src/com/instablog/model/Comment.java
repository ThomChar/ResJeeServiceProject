package com.instablog.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "comment")
@Entity(name = "comment")
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String message;
	private String commentDate;
	private boolean isActive;
	//private int id_user;
	//private int id_post;
	//private int id_comment;

	@ManyToOne(fetch = FetchType.EAGER)
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Post post;
	
	//Commentaires dont le present commentaire est la reference
	/*@OneToMany( mappedBy = "commentResponse", cascade =  CascadeType.ALL )
	  private Collection< Comment > listeComments;*/
	
	//Commentaire auquel le present commentaire fait reference si necessaire
	@ManyToOne(fetch = FetchType.EAGER)
	private Comment commentResponse;
	
	public Comment() {
	}

	public Comment(int id, String message, String commentDate) {
		this.id = id;
		this.message = message;
		this.commentDate = commentDate;
	}

	public Comment(String message, String commentDate, User user, Post post, Comment commentRefered) {
		this.message = message;
		this.commentDate = commentDate;
		this.user = user;
		this.post = post;
		this.commentResponse = commentRefered;
		this.isActive = true;
	}

	public Comment(String message, String commentDate, User user, Post post) {
		this.message = message;
		this.commentDate = commentDate;
		this.user = user;
		this.post = post;
		//this.commentResponse = commentRefered;
		this.isActive = true;
	}

	public int getId() {
		return id;
	}

	@XmlElement
	public void setId(int id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	@XmlElement
	public void setMessage(String message) {
		this.message = message;
	}

	public String getCommentDate() {
		return commentDate;
	}

	@XmlElement
	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}

	public boolean isActive() {
		return isActive;
	}
	
	@XmlElement
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public User getUser() {
		return user;
	}

	@XmlElement
	public void setUser(User user) {
		this.user = user;
	}

	public Post getPost() {
		return post;
	}

	@XmlElement
	public void setPost(Post post) {
		this.post = post;
	}

	/*public Collection<Comment> getListeComments() {
		return listeComments;
	}

	@XmlElement
	public void setListeComments(List<Comment> listeComments) {
		this.listeComments = listeComments;
	}*/

	public Comment getComment() {
		return commentResponse;
	}

	@XmlElement
	public void setComment(Comment comment) {
		this.commentResponse = comment;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", message=" + message + ", commentDate=" + commentDate + ", isActive=" + isActive
				+ ", user=" + user + ", post=" + post + ", comment=" + commentResponse + "]";
	}
}