package ProjetGOT;

import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class Avance implements Behavior{
	
	private NavigateurRobot navRobot;
	
	public Avance(NavigateurRobot navRobot) {
		this.navRobot = navRobot;
	}
	
	public boolean takeControl() {
			return true;
	}
	
	public void suppress() {
		navRobot.pilot.stop();
	}

	public void action() {
		navRobot.pilot.forward();
		Delay.msDelay(1000);
	}
	
	
	
}



