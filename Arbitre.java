package ProjetGOT;

import ProjetGOT.ArretBouton;
import ProjetGOT.DetectCouleur;
import ProjetGOT.HitRobot;
import ProjetGOT.LigneNoire;
import ProjetGOT.RobotNavigator;
import ProjetGOT.WhereIAm;
import lejos.hardware.Button;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Arbitre {
	
	// Site pour optimisation ::
	// https://florat.net/tutorial-java/chapitre08-opti.html
	
	public static void main(String[] args) {
		Button.waitForAnyPress();
<<<<<<< HEAD
		
=======
>>>>>>> 3bc8ed9ac6fc22502b0f7d0b21e845a92671e6c6
//##### Initialisation de l'arbitre, du biais et du robot ####
		Arbitrator arby = null;
		int newBiaisAngle = -13;
		RobotNavigator robotNav = new RobotNavigator (newBiaisAngle);
		
//##### D�finition des comportements #####
		Behavior b1 = new LigneNoire(robotNav);
<<<<<<< HEAD
		Behavior b2 = new ArriveGoal(robotNav);
=======
		Behavior b2 = new HitRobot(robotNav);
		Behavior b3 = new DetectCouleur(robotNav);
>>>>>>> 3bc8ed9ac6fc22502b0f7d0b21e845a92671e6c6
		Behavior b4 = new WhereIAm(robotNav);
		Behavior b5 = new ArretBouton(arby, robotNav);
		
		// du moins important au plus important
<<<<<<< HEAD
		Behavior[] bArray = {b1,b2,b4,b5}; 
		arby = new Arbitrator(bArray);
		
//##### Arr�t de l'arbitre ####
		if(b4 instanceof ArretBouton) {
			ArretBouton b = (ArretBouton)b4;
			b.setArbitrator(arby);
		}
		arby.go();
}
}
=======
		Behavior[] bArray = {b3,b1,b2,b4,b5}; 
		arby = new Arbitrator(bArray);
		
//##### Arr�t de l'arbitre ####
		((ArretBouton) b5).setArbitrator(arby);
		arby.go();
	}
}
>>>>>>> 3bc8ed9ac6fc22502b0f7d0b21e845a92671e6c6
