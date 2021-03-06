package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Post{
	private int id;
	private String image;
	private String comment;
	private LocalDateTime postDate;

	private User user;
	
	private Collection< Comment > listeComments;
	
	public Post() {
	}

	public Post(int id, String image, String comment, String postDate) {
		this.id = id;
		this.image = image;
		this.comment = comment;
		this.postDate = LocalDateTime.parse(postDate);
	}

	public Post(String image, String comment, String postDate, User user) {
		this.image = image;
		this.comment = comment;
		this.postDate = LocalDateTime.parse(postDate);
		this.user = user;
	}
	
	public Post(String image, String comment, String postDate) {
		this.image = image;
		this.comment = comment;
		this.postDate = LocalDateTime.parse(postDate);
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

	public LocalDateTime getPostDate() {
		return postDate;
	}

	@XmlElement
	public void setPostDate(String postDate) {
		this.postDate = LocalDateTime.parse(postDate);
	}

	public User getUser() {
		return user;
	}

	@XmlElement
	public void setUser(User user) {
		this.user = user;
	}

	public Collection<Comment> getListeComments() {
		return listeComments;
	}

	@XmlElement
	public void setListeComments(List<Comment> listeComments) {
		this.listeComments = listeComments;
	}

	@Override
	public String toString() {
		return "Post (id=" + id + ") " + "@" + user.getPseudo() + " : " +comment+ "\t le " + postDate.format(DateTimeFormatter.ISO_LOCAL_DATE) +" � "+ postDate.format(DateTimeFormatter.ISO_LOCAL_TIME);
	}	
}