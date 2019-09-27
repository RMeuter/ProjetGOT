package OBJECTIF1;

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class LigneNoire implements Behavior{

	private EV3ColorSensor cs;
	private MovePilot pilot;
	
	public LigneNoire(EV3ColorSensor cs, MovePilot pilot) {
		this.cs = cs;
		this.pilot = pilot;
	}
	
	public boolean takeControl() { 
		return cs.getColorID() == Color.BLACK;
		}
	
	public void suppress() {
	}

	public void action() {
		pilot.forward();
	}	
}
