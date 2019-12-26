package ProjetGOT;

import ProjetGOT.ArretRobot;
import ProjetGOT.Avance;
import ProjetGOT.HeurteRobot;
import ProjetGOT.LigneNoire;
import ProjetGOT.NavigateurRobot;
import ProjetGOT.MeLocaliser;
import lejos.hardware.Button;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class LancementDuJeu {
	
	public static void main(String[] args) {
		Button.waitForAnyPress();
		
//Initialisation de l'arbitre, du biais et du robot.
		
		Arbitrator arby = null;
		int nouveauBiaisAngle = -6;
		NavigateurRobot navRobot = new NavigateurRobot (nouveauBiaisAngle);
		
//Définition des comportements et initialisation des comportements.
		
		Behavior b1 = new LigneNoire(navRobot);
		Behavior b2 = new HeurteRobot(navRobot);
		Behavior b3 = new Avance(navRobot);
		Behavior b4 = new MeLocaliser(navRobot);
		Behavior b5 = new ArretRobot(arby, navRobot);
		
		Behavior[] bArray = {b3,b1,b2,b4,b5}; 
		arby = new Arbitrator(bArray);
		
//Arrêt de l'arbitre.
		((ArretRobot) b5).setArbitrator(arby);
		arby.go();
	}
}

