package ProjetGOT;

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class LigneNoire implements Behavior{

	private EV3ColorSensor cs;
	private MovePilot pilot;
	private Carte carte;
	
	public LigneNoire(EV3ColorSensor cs, MovePilot pilot, Carte carte) {
		this.cs = cs;
		this.pilot = pilot;
		this.carte= carte;
	}
	
	public boolean takeControl() { 
		return cs.getColorID() == Color.BLACK;
	}
	
	public void suppress() {
	}

	public void action() {
		carte.getPlusCourtChemin(); // Ici l'algo de plus court chemin
		int [] position = carte.getPositionDynamque();
		
		pilot.forward();
	}	
}
