package main.java.application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import main.java.models.Settings;
import main.java.utils.WriteConfig;

public class Serveur_controller {
	
private Stage stage;
	
	private Scene scene;
	
	private TextField nom;
	private TextField adress;
	private TextField login;
	
	private Label nomLabel;
	private Label adresseLabel;
	private Label loginLabel;
	
	private VBox vbox;
	private HBox hbox1;
	private HBox hbox2;
	private HBox hbox3;

	
	private Button enregistrer;
	

	public void initialize() {

		nomLabel = new Label("nom du serveur à afficher : ");
		nom = new TextField();
		
		hbox1 = new HBox();
		hbox1.getChildren().add(nomLabel);
		hbox1.getChildren().add(nom);
		
		adresseLabel = new Label("adresse du serveur : ");
		adress = new TextField();

		hbox2 = new HBox();
		hbox2.getChildren().add(adresseLabel);
		hbox2.getChildren().add(adress);
		
        loginLabel = new Label("login du compte : ");
        login = new TextField();

        hbox3 = new HBox();
		hbox3.getChildren().add(loginLabel);
		hbox3.getChildren().add(login);
		
		enregistrer = new Button("Enregistrer le nouveau serveur");
		
		enregistrer.setOnAction(e -> WriteConfig.addServeur(nom.getText(), adress.getText(), login.getText(), stage));
		
		HBox.setHgrow(nom, Priority.ALWAYS);
        HBox.setHgrow(adress, Priority.ALWAYS);
        HBox.setHgrow(login, Priority.ALWAYS);
		
		hbox1.setAlignment(Pos.CENTER);
		hbox1.setPadding(new Insets(5, 5, 10, 5));
		hbox2.setAlignment(Pos.CENTER);
		hbox2.setPadding(new Insets(5, 5, 10, 5));
		hbox3.setAlignment(Pos.CENTER);
		hbox3.setPadding(new Insets(5, 5, 10, 5));
		
		vbox = new VBox();
		vbox.setPadding(new Insets(5, 5, 10, 5));
		vbox.getChildren().add(hbox1);
		vbox.getChildren().add(hbox2);
		vbox.getChildren().add(hbox3);
		vbox.getChildren().add(enregistrer);
		
		scene = new Scene(vbox);
		
		stage = new Stage();
		
		stage.setScene(scene);
		stage.show();

	}

}
