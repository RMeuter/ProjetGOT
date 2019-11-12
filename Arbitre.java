package OBJECTIF1;

import lejos.hardware.Button;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Arbitre {
	
	// Site pour optimisation ::
	// https://florat.net/tutorial-java/chapitre08-opti.html
	
	public static void main(String[] args) {
		Button.waitForAnyPress();
		
//##### Initialisation de l'arbitre, du biais et du robot ####
		Arbitrator arby = null;
		int newBiaisAngle = -13;
		RobotNavigator robotNav = new RobotNavigator (newBiaisAngle);
		
//##### Définition des comportements #####
		Behavior b1 = new seDeplace(robotNav);
		Behavior b2 = new ArriveGoal(robotNav);
		Behavior b4 = new WhereIAm(robotNav);
		Behavior b5 = new ArretBouton(arby, robotNav);
		
		// du moins important au plus important
		Behavior[] bArray = {b1,b2,b4,b5}; 
		arby = new Arbitrator(bArray);
		
//##### Arrêt de l'arbitre ####
		if(b4 instanceof ArretBouton) {
			ArretBouton b = (ArretBouton)b4;
			b.setArbitrator(arby);
		}
		arby.go();
}
}