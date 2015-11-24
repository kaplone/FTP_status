package application;

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
import models.Settings;
import utils.Caporal;
import utils.WriteConfig;

public class NewCompte_controller {
	
private Stage stage;
	
	private Scene scene;
	
	private TextField login;
	private TextField pass;
	
	private Label loginLabel;
	private Label passLabel;
	
	private VBox vbox;
	private HBox hbox1;
	private HBox hbox2;
	
	private Button enregistrer;
	

	public void initialize() {
		
		loginLabel = new Label("login du compte à créer : ");
        login = new TextField();

        hbox1 = new HBox();
		hbox1.getChildren().add(loginLabel);
		hbox1.getChildren().add(login);
		
        passLabel = new Label("mot de passe du compte à créer : ");
        pass = new TextField();
		
        hbox2 = new HBox();
		hbox2.getChildren().add(passLabel);
		hbox2.getChildren().add(pass);
		
		enregistrer = new Button("Créer le nouveau compte");

		enregistrer.setOnAction(e -> { System.out.println("avant condition");
			                           if(! Caporal.checkUserExists(login.getText())){
			                               System.out.println("appel script de création");
			                               Caporal.createUser(login.getText(), pass.getText());
			                               Caporal.addReadme(login.getText(), pass.getText());
			                               Caporal.setUserPasswd(login.getText(), pass.getText(), stage);
			                               
		                                }
		                              });
		
        HBox.setHgrow(login, Priority.ALWAYS);
        HBox.setHgrow(pass, Priority.ALWAYS);
		
		hbox1.setAlignment(Pos.CENTER);
		hbox1.setPadding(new Insets(5, 5, 10, 5));
		hbox2.setAlignment(Pos.CENTER);
		hbox2.setPadding(new Insets(5, 5, 10, 5));
		
		vbox = new VBox();
		vbox.setPadding(new Insets(5, 5, 10, 5));
		vbox.getChildren().add(hbox1);
		vbox.getChildren().add(hbox2);
		vbox.getChildren().add(enregistrer);
		
		scene = new Scene(vbox);
		
		stage = new Stage();
		
		stage.setScene(scene);
		stage.show();

	}

}
