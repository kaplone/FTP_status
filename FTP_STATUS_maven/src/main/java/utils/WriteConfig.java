package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import application.FTP_status_controller;
import javafx.stage.Stage;

public class WriteConfig {
	
	public static void writeConfig(String field, String value){
		
		String home =  System.getProperty("user.home");
		File settings_file = new File(home, "ftp_status.conf");
		File old_file = new File(home, "ftp_status_old.conf");
		
		settings_file.renameTo(old_file);
		
        FileReader fr = null;
        FileWriter fw = null;
    	
		try {
			fr = new FileReader(old_file);
			fw = new FileWriter(settings_file);

	    	BufferedReader br = new BufferedReader(fr);
	    	
	    	String s = br.readLine();
			
	    	while(s != null){
	    		
	    		if (s.startsWith(field)){
	    			fw.write(field +  " = " + value);
	    		}
	    		else {
	    			fw.write(s);
	    		}
	    		fw.write(System.getProperty("line.separator"));
	    		s = br.readLine();
	    	}
	    	fr.close();
	    	fw.flush();
	    	fw.close();
	    	
		}catch (IOException e) {
		// TODO Bloc catch généré automatiquement
		e.printStackTrace();
	    }
		
	}
	
    public static void addServeur(String nom, String adresse, String login, String pass, Stage stage){
		
		String home =  System.getProperty("user.home");
		File settings_file = new File(home, "ftp_status.conf");
		File old_file = new File(home, "ftp_status_old.conf");
		
		settings_file.renameTo(old_file);
		
        FileReader fr = null;
        FileWriter fw = null;
    	
		try {
			fr = new FileReader(old_file);
			fw = new FileWriter(settings_file);

	    	BufferedReader br = new BufferedReader(fr);
	    	
	    	String s = br.readLine();
			
	    	while(s != null){
	    		
	    		fw.write(s);
	    		fw.write(System.getProperty("line.separator"));
	    		s = br.readLine();
	    	}
	    	
	    	fw.write(System.getProperty("line.separator"));
	    	
	    	fw.write(String.format("[%s]",nom));
	    	fw.write(System.getProperty("line.separator"));
	    	fw.write(String.format("%s = %s","adresse", adresse));
	    	fw.write(System.getProperty("line.separator"));
	    	fw.write(String.format("%s = %s","port", "21"));
	    	fw.write(System.getProperty("line.separator"));
	    	fw.write(String.format("%s = %s","login", login));
	    	fw.write(System.getProperty("line.separator"));
	    	fw.write(String.format("%s = %s","pass", pass));
	    	fw.write(System.getProperty("line.separator"));
	    	fw.write(String.format("[/%s]",nom));
	    	fw.write(System.getProperty("line.separator"));
	    	
	    	fr.close();
	    	fw.flush();
	    	fw.close();
	    	
	    	stage.close();
	    	
		}catch (IOException e) {
		// TODO Bloc catch généré automatiquement
		e.printStackTrace();
	    }		
	}

}
