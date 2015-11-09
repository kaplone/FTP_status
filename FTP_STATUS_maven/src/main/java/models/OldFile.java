package models;

import javafx.scene.control.Label;
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
		return this.taille;
	}
	
	public void setTaille(int taille) {
		this.taille = taille;
	}
	
	public Pane getPane(){
		
		Pane p = new Pane();
		Rectangle r = new Rectangle(25, 25, Color.GREEN);
		r.setWidth(taille);
		Label l = new Label(chemin);
		
		if (age > 100){
			r.setFill(Color.RED);
		}
		else if (age > 50){
			r.setFill(Color.YELLOW);
		}
		else {
			r.setFill(Color.GREEN);
		}
		
		p.getChildren().add(r);
		p.getChildren().add(l);
		
		
		return p;
		
	}

}
