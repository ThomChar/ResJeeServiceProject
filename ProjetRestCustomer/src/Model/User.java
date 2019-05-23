package Model;

import javax.xml.bind.annotation.XmlElement;

public class User {
	private int id;
	private String firstName;
	private String lastname;
	private String pseudo;
	private String dateCreation;
	private String password;

	public User() {
	}

	public User(int id, String firstName, String lastname, String pseudo, String dateCreation) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastname = lastname;
		this.pseudo = pseudo;
		this.dateCreation = dateCreation;
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

	@Override
	public String toString() {
		return "User (id=" + id + ") @" + pseudo + " : " + firstName + " " + lastname;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(String dateCreation) {
		this.dateCreation = dateCreation;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}