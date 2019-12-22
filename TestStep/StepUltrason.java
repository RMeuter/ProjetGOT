package ProjetGOT.TestStep;

import ProjetGOT.ArretBouton;
import ProjetGOT.DetectCouleur;
import ProjetGOT.HitRobot;
import ProjetGOT.LigneNoire;
import ProjetGOT.Robot;
import ProjetGOT.RobotNavigator;
import lejos.hardware.Button;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class StepUltrason {
	public static void main(String[] args) {

		Button.waitForAnyPress();
//##### Initialisation de l'arbitre, du biais et du robot ####
		Arbitrator arby = null;
		int newBiaisAngle = -14;
		RobotNavigator robotNav = new RobotNavigator (newBiaisAngle);
		
//##### Définition des comportements #####
		Behavior b1 = new LigneNoire(robotNav);
		Behavior b2 = new HitRobot(robotNav);
		Behavior b3 = new DetectCouleur(robotNav);
		Behavior b5 = new ArretBouton(arby, robotNav);
		
		// du moins important au plus important
		Behavior[] bArray = {b3,b1,b2,b5}; 
		arby = new Arbitrator(bArray);
		
//##### Arrêt de l'arbitre ####
		((ArretBouton) b5).setArbitrator(arby);
		arby.go();
	}
}

