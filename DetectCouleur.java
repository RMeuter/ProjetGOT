package ProjetGOT;

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class DetectCouleur implements Behavior{

	private EV3ColorSensor cs;
	private MovePilot pilot;
	private Carte carte;
	private boolean needVerify = false;
	
	public DetectCouleur(EV3ColorSensor cs, MovePilot pilot, Carte carte) {
		this.cs = cs;
		this.pilot = pilot;
		this.carte=carte;
	}
	
	public boolean takeControl() { 
		int color = (int) cs.getColorID();
		if (color == Color.WHITE) {
			needVerify=true;
		}
		return cs.getColorID() != Color.BLACK; //Couleur différente
	}
	
	public void suppress() {
	}

	public void action() {
		/*
		 * On verify qu'il ne sort pas de la carte ainsi on ordonne au robot de parcourir la longueur d'une case 
		 * au max.
		 * A refaire par la suite
		 * 
		 * */
		if (needVerify==true) {
			pilot.travel(12);	
		} else {
			pilot.forward();
		}
		
	}
}


