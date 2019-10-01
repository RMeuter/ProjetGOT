package ProjetGOT;

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class LigneNoire implements Behavior{

	private EV3ColorSensor cs;
	private MovePilot pilot;
	private Carte carte;
	
	public LigneNoire(EV3ColorSensor cs, MovePilot pilot, Carte carte) {
		this.cs = cs;
		this.pilot = pilot;
		this.carte= carte;
	}
	
	public boolean takeControl() { 
		return cs.getColorID() == Color.BLACK;
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
		pilot.rotate(newPosition-carte.getPositionDynamique());
		carte.setPositionDynamique(newPosition);
		pilot.travel(carte.getTailleCase()+carte.getLigneCase());
	}
}
