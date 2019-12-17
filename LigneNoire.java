package ProjetGOT;

import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class LigneNoire implements Behavior{

	private RobotNavigator robotNav;
	
	public LigneNoire(RobotNavigator robotNav) {
		this.robotNav= robotNav;
	}
	
	public boolean takeControl() { 
		return robotNav.verifiePasseLigneNoire(true);
	}
	
	public void suppress() {
		robotNav.pilot.stop();
	}

	public void action() {
		/*
		 * Recupere la position dynamique proposer par la carte afin que le robot puisse 
		 * tourner sur un nouvelle angle puis avancement de 12 cm pour dépasser la case
		 * 
		 * */

		robotNav.tourne();
		Delay.msDelay(3000);
	}
}
