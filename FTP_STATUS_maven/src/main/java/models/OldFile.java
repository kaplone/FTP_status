package models;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;

import application.FTP_status_controller;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class OldFile {
	
	private long age;
	private String chemin;
	private long taille;
	
	
	
	public OldFile(long age, String chemin, long taille) {
		this.age = age;
		this.chemin = chemin;
		this.taille = taille;
	}
	public String getAge() {
		return String.format("%03d jours", age);
	}
	public long getAge_(){
		return this.age;
	}
	
	public void setAge(long age) {
		this.age = age;
	}
	public String getChemin() {
		return chemin;
	}
	public void setChemin(String chemin) {
		this.chemin = chemin;
	}
	public String getTaille() {
		return String.format("%03d %s",
				             taille >= 1000 ? taille/1000 : taille,
				             taille >= 1000 ? "Go" : "Mo");
	}
	
	public long getTaille_() {
		return taille;
	}
	
	public void setTaille(int taille) {
		this.taille = taille;
	}
	
	public Pane getPane(){
		
		Pane p = new Pane();
		Rectangle r = new Rectangle(25, 25, Color.GREEN);
		r.setWidth(this.taille > 5 ? this.taille : 5);
		r.setArcWidth(5);
		r.setArcHeight(5);
		
		DropShadow ds = new DropShadow();
		ds.setColor(Color.color(1f, 1f, 1f));
		
		Label l = new Label(chemin);
		l.setPadding(new Insets(5, 0, 0, 5));
		//l.setEffect(ds);
		
		if (age > FTP_status_controller.getSeuilVert() && age < FTP_status_controller.getSeuilJaune()){
			r.setFill(Color.color((age - FTP_status_controller.getSeuilVert()) / (FTP_status_controller.getSeuilJaune() - FTP_status_controller.getSeuilVert()),
					              1,
					              0));
		}
		else if (age >= FTP_status_controller.getSeuilJaune() && age < FTP_status_controller.getSeuilRouge()){
			r.setFill(Color.color(1,
					              1 - ((age - FTP_status_controller.getSeuilJaune()) 
					                   / ( FTP_status_controller.getSeuilRouge()- FTP_status_controller.getSeuilJaune())),
					              0));
		}
		else if (age >= FTP_status_controller.getSeuilRouge()){
			r.setFill(Color.RED);
		}
		
		p.getChildren().add(r);
		p.getChildren().add(l);
		
		
		return p;
		
	}

}
