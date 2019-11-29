package ProjetGOT;

import java.util.Arrays;

import lejos.hardware.lcd.LCD;
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
	}

	public void action() {
		/*
		 * Recupere la position dynamique proposer par la carte afin que le robot puisse 
		 * tourner sur un nouvelle angle puis avancement de 12 cm pour dépasser la case
		 * 
		 * */
		System.out.println("Comportement LN");
		robotNav.tourne();
		Delay.msDelay(3000);
		LCD.clear();
	}
}
