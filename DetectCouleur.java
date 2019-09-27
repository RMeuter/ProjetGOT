package ProjetGOT;

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class DetectCouleur implements Behavior{

	private EV3ColorSensor cs;
	private MovePilot pilot;
	private Carte carte;
	
	public DetectCouleur(EV3ColorSensor cs, MovePilot pilot, Carte carte) {
		this.cs = cs;
		this.pilot = pilot;
		this.carte=carte;
	}
	
	public boolean takeControl() { 
		return cs.getColorID() != Color.BLACK; //Couleur différente
	}
	
	public void suppress() {
	}

	public void action() {
		pilot.rotate(-3);
		
		if (cs.getColorID() != Color.BLACK){
			pilot.rotate(5);
		}
	}
}


