package com.instablog.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "post")

public class Post implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String image;
	private String comment;
	private String postDate;
	private int id_user;

	public Post() {
	}

	public Post(int id, String image, String comment, String postDate, int id_user) {
		this.id = id;
		this.image = image;
		this.comment = comment;
		this.postDate = postDate;
		this.id_user = id_user;
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

	public int getId_user() {
		return id_user;
	}

	@XmlElement
	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", image=" + image + ", comment=" + comment + ", postDate=" + postDate + ", id_user="
				+ id_user + "]";
	}

	
}