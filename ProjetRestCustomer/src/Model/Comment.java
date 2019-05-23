package Model;

import java.util.Collection;
import java.util.List;

public class Comment {

	private int id;
	private String message;
	private String commentDate;

	private User user;
	
	private Post post;
	
	private Collection< Comment > listeComments;
	
	private Comment commentResponse;
	
	public Comment() {
	}

	public Comment(int id, String message, String commentDate) {
		this.id = id;
		this.message = message;
		this.commentDate = null;
	}

	public Comment(String message, String commentDate, User user, Post post, Comment commentRefered) {
		this.message = message;
		this.commentDate = null;
		this.user = user;
		this.post = post;
		this.commentResponse = commentRefered;
	}

	public Comment(String message, String string, User user, Post post) {
		this.message = message;
		this.commentDate = null;
		this.user = user;
		this.post = post;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public Collection<Comment> getListeComments() {
		return listeComments;
	}

	public void setListeComments(List<Comment> listeComments) {
		this.listeComments = listeComments;
	}

	public Comment getComment() {
		return commentResponse;
	}
	
	public void setComment(Comment comment) {
		this.commentResponse = comment;
	}

	@Override
	public String toString() {
		return "@"+user.getPseudo() + "(id="+user.getId()+")"+
				message + post.toString();
	}
}