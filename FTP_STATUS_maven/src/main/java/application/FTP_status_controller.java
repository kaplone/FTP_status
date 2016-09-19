package main.java.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import main.java.models.OldFile;
import main.java.models.Serveur;
import main.java.models.Settings;
import main.java.utils.Caporal;
import main.java.utils.LoadConfig;

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
	private ChoiceBox<Serveur> liste_choiceBox;
	@FXML
	private Button ajouter_button;
	@FXML
	private Button configurer_button;
	
	@FXML
	private TableColumn<OldFile, Pane> fichier;
	@FXML
	private TableColumn<OldFile, String> taille;
	@FXML
	private TableColumn<OldFile, String> age;
	@FXML
	private TableColumn<OldFile, Pane> delete;
	
	String ftpAdress;
	String ftpLogin;
	String ftpPass;
	String ftpRepertoire;
	int ftpPort;
	static int tailleMin;
	static float seuilVert;
	static float seuilJaune;
	static float seuilRouge;
	
	private ObservableList<Serveur> serveurs;
	private Serveur serveur;
	
	Instant now;
	ArrayList<OldFile> oldFiles;
	ObservableList<OldFile> oldFilesTableau;
	
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
		delete.setCellValueFactory(
		        new PropertyValueFactory<OldFile,Pane>("delete")
		    );

		oldFilesTableau.addAll(oldFiles);
		
		tableau.setItems(oldFilesTableau);
		
		
	}
	
	@FXML
	public void onSupprimerButton(){
		
		Instant dansUneSemaine = Instant.now().plus(Duration.ofDays(Settings.getSursis()));
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

		File rep_temp = new File(String.format("/home/ftp_trash/%s", format1.format(Date.from(dansUneSemaine))));
		
		System.out.println("création : " + rep_temp.toString());
		Caporal.CreateDir(rep_temp.toString());

		for (OldFile of : oldFiles){
			if (of.getD()){
				System.out.println(of.getChemin());
				Caporal.moveFile(of.getChemin(), rep_temp.toString());
				
			}
		}
		
		rafraichir();
	}
	
	@FXML
	public void onAjouterButton(){
		Serveur_controller nouveau_serveur = new Serveur_controller();
        nouveau_serveur.initialize();
	}

	@FXML
	public void onConfigurer_button(){
		Settings_controller settings = new Settings_controller();
		settings.initialize();
		
		serveurs = LoadConfig.loadSettings();
	}
	
	@FXML
	public void onCreate_new_button(){
		NewCompte_controller new_server = new NewCompte_controller();
		new_server.initialize();
	}

	
	public void rafraichir(){

		now = Instant.now();
		oldFiles = new ArrayList<>();
		oldFilesTableau = FXCollections.observableArrayList();
		
		serveur = liste_choiceBox.getSelectionModel().getSelectedItem();
		
		if (serveur != null){
			
			System.out.println("appel listdir() depuis le controleur");
			
			ArrayList<String> anciens = new ArrayList<>();

			String sortie = Caporal.listDir(serveur.getFtpLogin(), Settings.getChefDeProjet(), Settings.getPassChefDeProjet());
			
			BufferedReader br = new BufferedReader(new StringReader(sortie));
	    	
	    	String s;
			try {
				s = br.readLine();

		    	while(s != null){
		    		System.out.println(s);
					
					System.out.println("split : " + Integer.parseInt(s.split("-___-")[2]));
					System.out.println("split : " + s.split("-___-")[0]);
					System.out.println("split : " + s.split("-___-")[1]);
					
					oldFiles.add(
	        				new OldFile(
	        						Integer.parseInt(s.split("-___-")[2]),
	        						s.split("-___-")[0],
	        						Integer.parseInt(s.split("-___-")[1]),
	        						false ));
					
					s = br.readLine();
					System.out.println(s);
				}
			} catch (IOException e) {
				System.out.println("erreur dans le readline");
				e.printStackTrace();
			}
		    
			System.out.println("fin de readline");
			
//			ftpAdress = serveur.getFtpAdresse();
//			ftpLogin = serveur.getFtpLogin();
//			ftpPass = serveur.getFtpPass();
//			ftpPort = serveur.getFtpPort();
//			
//			FTPClient ftpClient_ = ftpConnect();
//			
//			try {
//				listDirectory(ftpClient_, "/", "", 1);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

			affichage();
		}
	}


	public void initialize(URL location, ResourceBundle resources) {

		serveurs = LoadConfig.loadSettings();
		LoadConfig.loadAdresse();
		
		System.out.println(Settings.getChefDeProjet());
		System.out.println(Settings.getAdresse_serveur());
		System.out.println(Settings.getRacine_serveur());
		
		tailleMin = Settings.getTailleMin();
		seuilVert = Settings.getSeuilVert();
		seuilJaune = Settings.getSeuilJaune();
		seuilRouge = Settings.getSeuilRouge();	
		
		liste_choiceBox.setOnMousePressed(e -> {serveurs = LoadConfig.loadSettings();
		                                        liste_choiceBox.setItems(serveurs);  
		});
		
		liste_choiceBox.setItems(serveurs);
		liste_choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Serveur>() {
			
			@Override
			public void changed(ObservableValue<? extends Serveur> observable, Serveur oldValue, Serveur newValue) {
				serveur = newValue;
				rafraichir();
				
			}
		});
		
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
