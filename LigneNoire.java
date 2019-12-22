package ProjetGOT;

import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class LigneNoire implements Behavior{

	private NavigateurRobot navRobot;
	
	public LigneNoire(NavigateurRobot navRobot) {
		this.navRobot = navRobot;
	}
	
	public boolean takeControl() { 
		return navRobot.estPasserLigneNoire(true);
	}
	
	public void suppress() {
		navRobot.pilot.stop();
	}

	public void action() {
		navRobot.tourne();
		Delay.msDelay(3000);
	}
}
