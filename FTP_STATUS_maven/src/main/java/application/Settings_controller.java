package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Settings;
import utils.WriteConfig;

public class Settings_controller{
	
	private Stage stage;
	
	private Scene scene;
	
	private TextField tailleMin;
	private TextField seuilVert;
	private TextField  seuilJaune;
	private TextField  seuilRouge;
	
	private Label tailleMinLabel;
	private Label seuilVertLabel;
	private Label seuilJauneLabel;
	private Label seuilRougeLabel;
	
	private VBox vbox;
	private HBox hbox1;
	private HBox hbox2;
	private HBox hbox3;
	private HBox hbox4;

	public void initialize() {

		tailleMinLabel = new Label("taille minimum pour etre affiché : ");
		tailleMin = new TextField(Settings.getTailleMin() + "");
		
		tailleMin.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) {
                Settings.setTailleMin(Integer.parseInt(newValue));
		        WriteConfig.writeConfig("taille_min", newValue);
		        
		    }
		});
		
		hbox1 = new HBox();
		hbox1.getChildren().add(tailleMinLabel);
		hbox1.getChildren().add(tailleMin);
		
		seuilVertLabel = new Label("valeur minimum pour le changement du vert vers le jaune : ");
		seuilVert = new TextField(Math.round(Settings.getSeuilVert()) + "");
		
		seuilVert.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) {
                Settings.setSeuilVert(Integer.parseInt(newValue));
		        WriteConfig.writeConfig("seuil_vert", newValue);
		        
		    }
		});
		
		hbox2 = new HBox();
		hbox2.getChildren().add(seuilVertLabel);
		hbox2.getChildren().add(seuilVert);
		
        seuilJauneLabel = new Label("valeur maximum pour le changement du vert vers le jaune : ");
        seuilJaune = new TextField(Math.round(Settings.getSeuilJaune()) + "");
        
        seuilJaune.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) {
                Settings.setSeuilJaune(Integer.parseInt(newValue));
		        WriteConfig.writeConfig("seuil_jaune", newValue);
		        
		    }
		});
		
        hbox3 = new HBox();
		hbox3.getChildren().add(seuilJauneLabel);
		hbox3.getChildren().add(seuilJaune);
		
        seuilRougeLabel = new Label("valeur maximum pour le changement du jaune vers le rouge : ");
        seuilRouge = new TextField(Math.round(Settings.getSeuilRouge()) + "");
        
        seuilRouge.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) {
                Settings.setSeuilRouge(Integer.parseInt(newValue));
		        WriteConfig.writeConfig("seuil_rouge", newValue);
		        
		    }
		});
		
        hbox4 = new HBox();
		hbox4.getChildren().add(seuilRougeLabel);
		hbox4.getChildren().add(seuilRouge);
		
		vbox = new VBox();
		vbox.getChildren().add(hbox1);
		vbox.getChildren().add(hbox2);
		vbox.getChildren().add(hbox3);
		vbox.getChildren().add(hbox4);
		
		scene = new Scene(vbox);
		
		stage = new Stage();
		
		stage.setScene(scene);
		stage.show();

	}

}
