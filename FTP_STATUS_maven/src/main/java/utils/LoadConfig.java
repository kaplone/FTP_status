package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Serveur;
import models.Settings;

public class LoadConfig {
	
	static ObservableList<Serveur> serveurs;
	static Serveur serveur;
	static String nom;
	
    public static ObservableList<Serveur> loadSettings(){
    	
    	serveurs = FXCollections.observableArrayList();
    	
		String home =  System.getProperty("user.home");
		File settings_file = new File(home, "ftp_status.conf");
		
        FileReader fr = null;
    	
		try {
			fr = new FileReader(settings_file);

	    	BufferedReader br = new BufferedReader(fr);
	    	
	    	String s = br.readLine();
			
	    	while(s != null){
	    		
	    		if (s.startsWith("#") || s.trim().equals("")){
	    		}
	    		
                else if (s.startsWith("[/")){
	    			
	    			if (serveur != null){
	    				serveurs.add(serveur);
	    			}
	    			
	    			nom = null;

	    		}
	    		
	    		else if (s.startsWith("[")){

	    			nom = s.substring(1, s.trim().length() -1);
	    			
	    			System.out.println("nom : " + nom);
	    			serveur = new Serveur(nom);

	    		}
	
	    		else {
	    		
		    		String key = s.split("=")[0].trim();
		    		String value = s.split("=")[1].trim();
		    		
		    		switch (key){
		    		
		    		case "adresse" : serveur.setFtpAdresse(value);
		    		                break;
		    		case "port"    : serveur.setFtpPort(Integer.parseInt(value));
	                                break;
		    		case "login"   : serveur.setFtpLogin(value);
		    		                break;
		    		case "pass"    :  serveur.setFtpPass(value);
		    			            break;
		    		case "taille_min" :  Settings.setTailleMin(Integer.parseInt(value));
		                            break;
		    		case "seuil_vert" :  Settings.setSeuilVert(Integer.parseInt(value));
	                                break;
		    		case "seuil_jaune" :  Settings.setSeuilJaune(Integer.parseInt(value));
		                            break;
		    		case "seuil_rouge" :  Settings.setSeuilRouge(Integer.parseInt(value));
		                            break;
	
		    		}
	    		}

				s = br.readLine();
	    	}
			fr.close();
			
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
		
		return serveurs;
		
	}

}
