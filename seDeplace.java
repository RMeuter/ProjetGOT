package OBJECTIF1;

import java.util.LinkedList;

import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;

public class seDeplace implements Behavior{
	
// #### Attributs ####	
	private RobotNavigator robotNav;
	private Dijkstra cheminDijkstra;

// #### Constructeur ####
	public seDeplace(RobotNavigator robotNav) {
		this.robotNav= robotNav;
	}

// #### Méthodes ####
	
	// Raison pour laquelle le comportement prend le dessus
	public boolean takeControl() {
			return true;
	}
	
	// Comportement ou action supprimé par le comportement actuel
	public void suppress() {
	}

	// Action réalisé par le comportement
	public void action() {
		
		// Plus court chemin de directions (NORD, SUD, EST, OUEST) vers le but
		LinkedList <Integer> chemin = cheminDijkstra.dijkstra();
		
		// Pour chaque direction, si le robot doit tourner, il tourne, sinon il avance
		// Dans l'objectif 4, le robot s'arrête pour rendre compte des poids de chaque case
		for (int x : chemin){
			LCD.drawInt(robotNav.versDirection(x), 3, 0);	
			if (robotNav.versDirection(x) == 0){
				robotNav.avance(x);
				if (robotNav.getEtape() == 3){
					robotNav.sarreteNSeconde();
				}
			}else{
				robotNav.tourne(x);
				if (robotNav.getEtape() == 3){
					robotNav.sarreteNSeconde();
				}
			}
		}
	}
	
	
	
}

