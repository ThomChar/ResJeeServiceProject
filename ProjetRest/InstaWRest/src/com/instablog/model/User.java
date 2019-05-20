package com.instablog.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String lastname;
	private String pseudo;
	private String password;
	private String UUID;

	public User() {
	}

	public User(int id, String name, String lastname, String pseudo, String password) {
		this.id = id;
		this.name = name;
		this.lastname = lastname;
		this.pseudo = pseudo;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	@XmlElement
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	@XmlElement
	public void setName(String name) {
		this.name = name;
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

	@XmlElement
	public void setPassword(String password) {
		this.password = password;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

}