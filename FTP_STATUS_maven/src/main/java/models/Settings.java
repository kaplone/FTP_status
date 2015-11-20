package models;

public class Settings {
	
	private static int tailleMin;
	
	private static float seuilVert;
	private static float seuilJaune;
	private static float seuilRouge;
	
	private static String chefDeProjet;
	private static String passChefDeProjet;
	
	private static String adresse_serveur;
	private static String pass_serveur;
	private static String racine_serveur;
	
	public static int getTailleMin() {
		return tailleMin;
	}
	public static void setTailleMin(int tailleMin) {
		Settings.tailleMin = tailleMin;
	}
	public static float getSeuilVert() {
		return seuilVert;
	}
	public static void setSeuilVert(float seuilVert) {
		Settings.seuilVert = seuilVert;
	}
	public static float getSeuilJaune() {
		return seuilJaune;
	}
	public static void setSeuilJaune(float seuilJaune) {
		Settings.seuilJaune = seuilJaune;
	}
	public static float getSeuilRouge() {
		return seuilRouge;
	}
	public static void setSeuilRouge(float seuilRouge) {
		Settings.seuilRouge = seuilRouge;
	}
	public static String getChefDeProjet() {
		return chefDeProjet;
	}
	public static void setChefDeProjet(String chefDeProjet) {
		Settings.chefDeProjet = chefDeProjet;
	}
	public static String getAdresse_serveur() {
		return adresse_serveur;
	}
	public static void setAdresse_serveur(String adresse_serveur) {
		Settings.adresse_serveur = adresse_serveur;
	}
	public static String getPass_serveur() {
		return pass_serveur;
	}
	public static void setPass_serveur(String pass_serveur) {
		Settings.pass_serveur = pass_serveur;
	}
	public static String getRacine_serveur() {
		return racine_serveur;
	}
	public static void setRacine_serveur(String racine_serveur) {
		Settings.racine_serveur = racine_serveur;
	}
	public static String getPassChefDeProjet() {
		return passChefDeProjet;
	}
	public static void setPassChefDeProjet(String passChefDeProjet) {
		Settings.passChefDeProjet = passChefDeProjet;
	}
	
}
