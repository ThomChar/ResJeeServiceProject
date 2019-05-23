package Gestion;

import Model.User;

public class GestionInstagram {

	private static String pathGestionInstagram;
	private static GestionUser gestionUser;
	private static GestionPost gestionPost;
	private String token;
	private User userConnected;

	public GestionInstagram(String webApiUrl) {
		setPathGestionInstagram(webApiUrl + "rest");
		gestionUser = new GestionUser(this);
		gestionPost= new GestionPost(this);
		
		token = null;
		userConnected = null;
	}

	public GestionUser getGestionUser() {
		return gestionUser;
	}

	public static String getPathGestionInstagram() {
		return pathGestionInstagram;
	}

	public static void setPathGestionInstagram(String pathGestionInstagram) {
		GestionInstagram.pathGestionInstagram = pathGestionInstagram;
	}

	public static void setGestionUser(GestionUser gestionUser) {
		GestionInstagram.gestionUser = gestionUser;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUserConnected() {
		return userConnected;
	}

	public void setUserConnected(User userConnected) {
		this.userConnected = userConnected;
	}

	public static GestionPost getGestionPost() {
		return gestionPost;
	}

	public static void setGestionPost(GestionPost gestionPost) {
		GestionInstagram.gestionPost = gestionPost;
	}
	
	public boolean userIsConnected() {
		if(token != null && userConnected != null)
			return true;
		else {
			System.out.println("Vous n'êtes pas connecté !");
			return false;
		}
	}
	
}
