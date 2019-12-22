package ProjetGOT;

<<<<<<< HEAD
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.navigation.MovePilot;
=======

>>>>>>> 3bc8ed9ac6fc22502b0f7d0b21e845a92671e6c6
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class DetectCouleur implements Behavior{
	/* Googlize : reconnaissance couleur robotique lejos
	 * Lien utile :
	 * - https://lego.vilvert.fr/2017/12/08/calibrage-du-capteur-de-couleur-ev3-avec-lejos/
	 * 
	 * */
	private RobotNavigator robotNav;
<<<<<<< HEAD
	private int [] etape;
	private boolean suppressed = false;
=======
>>>>>>> 3bc8ed9ac6fc22502b0f7d0b21e845a92671e6c6
	
	public DetectCouleur(RobotNavigator robotNav) {
		this.robotNav= robotNav;
	}
	
	public boolean takeControl() {
			return true;
	}
	
	public void suppress() {
<<<<<<< HEAD
		suppressed = true;
=======
>>>>>>> 3bc8ed9ac6fc22502b0f7d0b21e845a92671e6c6
		robotNav.pilot.stop();
	}

	public void action() {
		/*
		 * On verify qu'il ne sort pas de la carte ainsi on ordonne au robot de parcourir la longueur d'une case 
		 * au max.
		 * A refaire par la suite
		* */
<<<<<<< HEAD
		
		suppressed = false;		
		LCD.drawString("Comportement DC", 0, 3);
		robotNav.pilot.forward();
		Delay.msDelay(500);
		LCD.clear();
=======
		robotNav.pilot.forward();
		Delay.msDelay(1000);
>>>>>>> 3bc8ed9ac6fc22502b0f7d0b21e845a92671e6c6
	}
	
	
	
}



