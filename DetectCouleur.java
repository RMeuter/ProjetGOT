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
	private EV3ColorSensor cs;
	private MovePilot pilot;
	private Carte carte;
	private CalibrageColor colorTab;
	private boolean suppressed = false;
	
	public DetectCouleur(EV3ColorSensor cs, MovePilot pilot, Carte carte, CalibrageColor colorTab) {
		this.cs = cs;
		this.pilot = pilot;
		this.carte = carte;
		this.colorTab = colorTab;
	}
	
	public boolean takeControl() {
			return colorTab.getCalibreColor(cs) != "noir"; //Couleur différente
	}
	
	public void suppress() {
		suppressed = true;
		pilot.stop();
	}

	public void action() {
		/*
		 * On verify qu'il ne sort pas de la carte ainsi on ordonne au robot de parcourir la longueur d'une case 
		 * au max.
		 * A refaire par la suite
		* */
		
		suppressed = false;		
		LCD.drawString(colorTab.getCalibreColor(cs), 0, 3);				
		pilot.forward();
		while( pilot.isMoving() && !suppressed )
	         Thread.yield();
		LCD.clear();
	}
	
	
	
}



