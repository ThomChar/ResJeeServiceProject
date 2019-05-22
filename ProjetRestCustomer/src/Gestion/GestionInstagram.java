package Gestion;

public class GestionInstagram {

	private String webApiUrl;
	private GestionUser gestionUser;

	public GestionInstagram(String webApiUrl) {
		this.webApiUrl = webApiUrl;
		gestionUser = new GestionUser(this);
	}

	public String getWebApiUrl() {
		return webApiUrl;
	}

	public void setWebApiUrl(String webApiUrl) {
		this.webApiUrl = webApiUrl;
	}

	public GestionUser getGestionUser() {
		return gestionUser;
	}

	public void setGestionUser(GestionUser gestionUser) {
		this.gestionUser = gestionUser;
	}
	
	
}
