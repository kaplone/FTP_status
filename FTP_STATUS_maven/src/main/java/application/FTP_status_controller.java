package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import models.OldFile;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTP_status_controller implements Initializable {
	
	@FXML
	private TableView<OldFile> tableau;
	@FXML
	private Rectangle barre;
	@FXML
	private Label label;
	
	@FXML
	private TableColumn<OldFile, Pane> fichier;
	@FXML
	private TableColumn<OldFile, String> taille;
	@FXML
	private TableColumn<OldFile, String> age;
	
	String ftpAdress;
	String ftpLogin;
	String ftpPass;
	int ftpPort;
	static int tailleMin;
	static float seuilVert;
	static float seuilJaune;
	static float seuilRouge;
	
	Instant now;
	ArrayList<OldFile> oldFiles;
	ObservableList<OldFile> oldFilesTableau;
	
	public void listDirectory(FTPClient ftpClient, String parentDir,
	        String currentDir, int level) throws IOException {
	    String dirToList = parentDir;
	    if (!currentDir.equals("")) {
	        dirToList += "/" + currentDir;
	    }
	    FTPFile[] subFiles = ftpClient.listFiles(dirToList);
	    if (subFiles != null && subFiles.length > 0) {
	        for (FTPFile aFile : subFiles) {
	            String currentFileName = aFile.getName();
	            if (currentFileName.equals(".")
	                    || currentFileName.equals("..")) {
	                // skip parent directory and directory itself
	                continue;
	            }
	            for (int i = 0; i < level; i++) {
	                System.out.print("\t");
	            }
	            if (aFile.isDirectory()) {
	                System.out.println("[" + currentFileName + "]");
	                listDirectory(ftpClient, dirToList, currentFileName, level + 1);
	            } else {
	            	
	            	System.out.println(Duration.between(aFile.getTimestamp().toInstant(), now).toDays());
	            	
	            	if(Duration.between(aFile.getTimestamp().toInstant(), now).toDays() > 60){
	            		oldFiles.add(
	            				new OldFile(
	            						Duration.between(aFile.getTimestamp().toInstant(), now).toDays(),
	            						Paths.get(dirToList).resolve(currentFileName).toString(),
	            						aFile.getSize()/1024/1024 ));
	            		
	            		System.out.println(currentFileName + "  " + aFile.getSize()/1024/1024 + "Mo  " + aFile.getTimestamp().toInstant());
	            	}
	                
	            }
	        }
	    }
	}
	
	public void loadSettings(){
		
		String home =  System.getProperty("user.home");
		File settings_file = new File(home, "ftp_status.conf");
		
        FileReader fr = null;
    	
		try {
			fr = new FileReader(settings_file);

	    	BufferedReader br = new BufferedReader(fr);
	    	
	    	String s = null;

			s = br.readLine();
			
	    	while(s != null){

	    		if (s.startsWith("#") || s.trim().equals("")){
	    			s = br.readLine();
	    			continue;
	    		}
	    		
	    		String key = s.split("=")[0].trim();
	    		String value = s.split("=")[1].trim();
	    		switch (key){
	    		
	    		case "adresse" : ftpAdress = value;
	    		                break;
	    		case "port" : ftpPort = Integer.parseInt(value);
                                break;
	    		case "login" : ftpLogin = value;
	    		                break;
	    		case "pass" :  ftpPass = value;
	    			            break;
	    		case "taille_min" :  tailleMin = Integer.parseInt(value);
	                            break;
	    		case "seuil_vert" :  seuilVert = Integer.parseInt(value);
                                break;
	    		case "seuil_jaune" :  seuilJaune = Integer.parseInt(value);
	                            break;
	    		case "seuil_rouge" :  seuilRouge = Integer.parseInt(value);
	                            break;

	    		}
	    		
	    		
				s = br.readLine();
	    	}
			fr.close();
			
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
		
	}
	
	public FTPClient ftpConnect(){

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpAdress, ftpPort);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                return null;
            }
            boolean success = ftpClient.login(ftpLogin, ftpPass);
            if (!success) {
                System.out.println("Could not login to the server");
                return null;
            } else {
                System.out.println("LOGGED IN SERVER");
                return ftpClient;
            }
        } catch (IOException ex) {
            System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
            return null;
        }
	}
	
	public void affichage(){
		
		System.out.println(oldFiles.size());
		
		fichier.setCellValueFactory(
		        new PropertyValueFactory<OldFile, Pane>("pane")
		    );
		taille.setCellValueFactory(
		        new PropertyValueFactory<OldFile,String>("taille")
		    );
		age.setCellValueFactory(
		        new PropertyValueFactory<OldFile,String>("age")
		    );

		oldFilesTableau.addAll(oldFiles);
		
		tableau.setItems(oldFilesTableau);
		
		
	}

	public void initialize(URL location, ResourceBundle resources) {

		loadSettings();
		FTPClient ftpClient_ = ftpConnect();
		
		now = Instant.now();
		oldFiles = new ArrayList<>();
		oldFilesTableau = FXCollections.observableArrayList();
		
		
		try {
			listDirectory(ftpClient_, "/", "", 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		affichage();
		
	}

	public static int getTailleMin() {
		return tailleMin;
	}

	public static float getSeuilJaune() {
		return seuilJaune;
	}

	public static float getSeuilRouge() {
		return seuilRouge;
	}

	public static float getSeuilVert() {
		return seuilVert;
	}
	
	

}
