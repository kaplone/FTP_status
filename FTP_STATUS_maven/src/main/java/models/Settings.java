package models;

public class Settings {
	
	static int tailleMin;
	static float seuilVert;
	static float seuilJaune;
	static float seuilRouge;
	
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
	
	

}
