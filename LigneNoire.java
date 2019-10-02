package ProjetGOT;

import java.util.Arrays;

import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class LigneNoire implements Behavior{

	private EV3ColorSensor cs;
	private MovePilot pilot;
	private Carte carte;
	private CalibrageColor colorTab;
	
	public LigneNoire(EV3ColorSensor cs, MovePilot pilot, Carte carte, CalibrageColor colorTab) {
		this.cs = cs;
		this.pilot = pilot;
		this.carte= carte;
		this.colorTab = colorTab;
	}
	
	public boolean takeControl() { 
		return colorTab.getCalibreColor(cs) == "noir";
	}
	
	public void suppress() {
		pilot.stop();
	}

	public void action() {
		/*
		 * Recupere la position dynamique proposer par la carte afin que le robot puisse 
		 * tourner sur un nouvelle angle puis avancement de 12 cm pour dépasser la case
		 * */
		//carte.getPlusCourtChemin(); // Ici l'algo de plus court chemin
		
		int newPosition = carte.findNewPositionDynamique();
		int rotate = newPosition-carte.getPositionDynamique();
		if (rotate==270 || rotate ==-270) {
			rotate = -rotate/3;
		}
		pilot.rotate(rotate);
		carte.setPositionDynamique(newPosition);
		pilot.travel(carte.getTailleCase()+carte.getLigneCase());
		
		LCD.drawString("Position :"+Arrays.toString(carte.getPositionHistorique()), 0, 0); 
		Delay.msDelay(300);
		LCD.clear();
	}
}
