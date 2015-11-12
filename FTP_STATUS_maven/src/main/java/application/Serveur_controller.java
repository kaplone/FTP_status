package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import models.Settings;
import utils.WriteConfig;

public class Serveur_controller {
	
private Stage stage;
	
	private Scene scene;
	
	private TextField nom;
	private TextField adress;
	private TextField login;
	private TextField pass;
	
	private Label nomLabel;
	private Label adresseLabel;
	private Label loginLabel;
	private Label passLabel;
	
	private VBox vbox;
	private HBox hbox1;
	private HBox hbox2;
	private HBox hbox3;
	private HBox hbox4;
	
	private Button enregistrer;
	

	public void initialize() {

		nomLabel = new Label("nom du serveur à afficher : ");
		nom = new TextField();
		
//		nom.textProperty().addListener(new ChangeListener<String>() {
//		    @Override
//		    public void changed(ObservableValue<? extends String> observable,
//		            String oldValue, String newValue) {
//                Settings.setTailleMin(Integer.parseInt(newValue));
//		        WriteConfig.writeConfig("taille_min", newValue);
//		        
//		    }
//		});
		
		hbox1 = new HBox();
		hbox1.getChildren().add(nomLabel);
		hbox1.getChildren().add(nom);
		
		adresseLabel = new Label("adresse du serveur : ");
		adress = new TextField();
		
//		adress.textProperty().addListener(new ChangeListener<String>() {
//		    @Override
//		    public void changed(ObservableValue<? extends String> observable,
//		            String oldValue, String newValue) {
//                Settings.setSeuilVert(Integer.parseInt(newValue));
//		        WriteConfig.writeConfig("seuil_vert", newValue);
//		        
//		    }
//		});
		
		hbox2 = new HBox();
		hbox2.getChildren().add(adresseLabel);
		hbox2.getChildren().add(adress);
		
        loginLabel = new Label("login du compte : ");
        login = new TextField();
        
        
//        login.textProperty().addListener(new ChangeListener<String>() {
//		    @Override
//		    public void changed(ObservableValue<? extends String> observable,
//		            String oldValue, String newValue) {
//                Settings.setSeuilJaune(Integer.parseInt(newValue));
//		        WriteConfig.writeConfig("seuil_jaune", newValue);
//		        
//		    }
//		});
		
        hbox3 = new HBox();
		hbox3.getChildren().add(loginLabel);
		hbox3.getChildren().add(login);
		
        passLabel = new Label("mot de passe du compte : ");
        pass = new TextField();
        
//        pass.textProperty().addListener(new ChangeListener<String>() {
//		    @Override
//		    public void changed(ObservableValue<? extends String> observable,
//		            String oldValue, String newValue) {
//                Settings.setSeuilRouge(Integer.parseInt(newValue));
//		        WriteConfig.writeConfig("seuil_rouge", newValue);
//		        
//		    }
//		});
		
        hbox4 = new HBox();
		hbox4.getChildren().add(passLabel);
		hbox4.getChildren().add(pass);
		
		enregistrer = new Button("Enregistrer le nouveau serveur");
		
		enregistrer.setOnAction(e -> WriteConfig.addServeur(nom.getText(), adress.getText(), login.getText(), pass.getText()));
		
		vbox = new VBox();
		vbox.getChildren().add(hbox1);
		vbox.getChildren().add(hbox2);
		vbox.getChildren().add(hbox3);
		vbox.getChildren().add(hbox4);
		vbox.getChildren().add(enregistrer);
		
		scene = new Scene(vbox);
		
		stage = new Stage();
		
		stage.setScene(scene);
		stage.show();

	}

}
