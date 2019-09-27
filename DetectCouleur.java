package OBJECTIF1;

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class DetectCouleur implements Behavior{

	private EV3ColorSensor cs;
	private MovePilot pilot;
	
	public DetectCouleur(EV3ColorSensor cs, MovePilot pilot) {
		this.cs = cs;
		this.pilot = pilot;
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


