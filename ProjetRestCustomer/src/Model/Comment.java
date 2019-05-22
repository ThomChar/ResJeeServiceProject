package Model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "comment")

public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String message;
	private String postDate;
	private int id_user;
	private int id_post;
	private int id_comment;

	public Comment() {
	}

	public Comment(int id, String message, String postDate, int id_user, int id_post, int id_comment) {
		this.id = id;
		this.message = message;
		this.postDate = postDate;
		this.id_user = id_user;
		this.id_post = id_post;
		this.id_comment = id_comment;
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

	public int getId_post() {
		return id_post;
	}

	@XmlElement
	public void setId_post(int id_post) {
		this.id_post = id_post;
	}

	public int getId_comment() {
		return id_comment;
	}

	@XmlElement
	public void setId_comment(int id_comment) {
		this.id_comment = id_comment;
	}
	
}