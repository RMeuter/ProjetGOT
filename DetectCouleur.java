package ProjetGOT;

import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class DetectCouleur implements Behavior{
	/* Googlize : reconnaissance couleur robotique lejos
	 * Lien utile :
	 * - https://lego.vilvert.fr/2017/12/08/calibrage-du-capteur-de-couleur-ev3-avec-lejos/
	 * 
	 * */
	private RobotNavigator robotNav;
	private int [] etape;
	private boolean suppressed = false;
	
	public DetectCouleur(RobotNavigator robotNav) {
		this.robotNav= robotNav;
	}
	
	public boolean takeControl() {
			return true;
	}
	
	public void suppress() {
		suppressed = true;
		robotNav.pilot.stop();
	}

	public void action() {
		/*
		 * On verify qu'il ne sort pas de la carte ainsi on ordonne au robot de parcourir la longueur d'une case 
		 * au max.
		 * A refaire par la suite
		* */
		
		suppressed = false;		
		LCD.drawString("Comportement DC", 0, 3);
		robotNav.pilot.forward();
		Delay.msDelay(1000);
		LCD.clear();
	}
	
	
	
}



