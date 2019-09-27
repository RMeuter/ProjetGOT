package ProjetGOT;

import lejos.hardware.motor.Motor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class Avancer implements Behavior {
	
	private MovePilot pilot;
	
	public Avancer(MovePilot pilot) {
		this.pilot = pilot;
	}
	
	public boolean takeControl() { return true; }
	
	public void suppress() {
		pilot.stop();
		pilot.stop();
	}

	public void action() {
		pilot.forward();
		pilot.forward();
	}
}