package ProjetGOT;

import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class DetectCouleur implements Behavior{

	private EV3ColorSensor cs;
	private MovePilot pilot;
	private Carte carte;
	private boolean needVerify = false;
	
	public DetectCouleur(EV3ColorSensor cs, MovePilot pilot, Carte carte) {
		this.cs = cs;
		this.pilot = pilot;
		this.carte=carte;
	}
	
	public boolean takeControl() { 
		int color = (int) cs.getColorID();
		if (color == Color.WHITE) {
			needVerify=true;
		}
		return cs.getColorID() != Color.BLACK; //Couleur différente
	}
	
	public void suppress() {
		pilot.stop();
	}

	public void action() {
		/*
		 * On verify qu'il ne sort pas de la carte ainsi on ordonne au robot de parcourir la longueur d'une case 
		 * au max.
		 * A refaire par la suite
		 * */
		LCD.drawString(showColor(cs.getColorID()), 3, 4);
		Delay.msDelay(300);
		LCD.clear();
		if (needVerify==true) {
			pilot.travel(12);	
		} else {
			pilot.forward();
		}
		
	}
	
	public String showColor(int color) {
		switch (color) {
		case Color.BLACK :
			return "Noir";
		case Color.BLUE :
			return "Blue";
		case Color.RED :
			return "Rouge";
		case Color.ORANGE:
			return "Orange";
		case Color.WHITE:
			return "blanc";
		default :
			return "Pas de color !";
		} 
	}
}


