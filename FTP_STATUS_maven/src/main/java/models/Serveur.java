package main.java.models;

public class Serveur {
	
	private String nom;
	private String ftpAdresse;
	private String ftpLogin;
	private String ftpPass;
	private int ftpPort;
	
	public Serveur(){
		this(null, null, null, null, 21);
	}
	public Serveur(String nom){
		this(nom, null, null, null,21);
	}
	
	public Serveur(String nom, String adresse, String login, String pass, int port) {
		this.nom = nom;
		this.ftpAdresse = adresse;
		this.ftpLogin = login;
		this.ftpPass = pass;
	}
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getFtpAdresse() {
		return ftpAdresse;
	}
	public void setFtpAdresse(String adresse) {
		this.ftpAdresse = adresse;
	}
	public String getFtpLogin() {
		return ftpLogin;
	}
	public void setFtpLogin(String login) {
		this.ftpLogin = login;
	}
	public String getFtpPass() {
		return ftpPass;
	}
	public void setFtpPass(String pass) {
		this.ftpPass = pass;
	}
	public int getFtpPort() {
		return ftpPort;
	}
	public void setFtpPort(int ftpPort) {
		this.ftpPort = ftpPort;
	}
	
	@Override
	public String toString(){
		return nom;
	}

}
