package ProjetGOT;

import java.util.Arrays;

import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class LigneNoire implements Behavior{

	RobotNavigator robotNav;
	private boolean suppressed = false;
	
	public LigneNoire(RobotNavigator robotNav) {
		this.robotNav= robotNav;
	}
	
	public boolean takeControl() { 
		return robotNav.verifiePasseLigneNoire(true);
	}
	
	public void suppress() {
		robotNav.pilot.stop();
		suppressed = true;
	}

	public void action() {
		/*
		 * Recupere la position dynamique proposer par la carte afin que le robot puisse 
		 * tourner sur un nouvelle angle puis avancement de 12 cm pour dépasser la case
		 * 
		 * */
		suppressed = false;
		LCD.drawString("Position :"+Arrays.toString(robotNav.getPosition()), 0, 3);
		LCD.drawString("Comportement LN", 0, 4);
		robotNav.doRot();
		Delay.msDelay(3000);
		robotNav.pilot.forward();
		while( robotNav.pilot.isMoving() && !suppressed ) Thread.yield();
		LCD.clear();
	}
}
