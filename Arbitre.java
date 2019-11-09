package ProjetGOT;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class Arbitre {
	// Site pour optimisation ::
	// https://florat.net/tutorial-java/chapitre08-opti.html
	
	public static void main(String[] args) {
		Button.RIGHT.waitForPressAndRelease();
		
		//######################## Base
		// définition de l'arbitre, et de la carte
		// mise en place du contexte avec démarrage par l'étape du choix
		// et paramettrage du biais du à la rotation du robot
		
		Arbitrator arby = null;
		int newBiaisAngle = -13;
		int etapeChoix = 1;
		
		RobotNavigator robotNav =new RobotNavigator (newBiaisAngle, etapeChoix);
		
		//######################## Definition des comportements
		Behavior b1 = new ArriveGoal(robotNav);
		Behavior b2 = new DetectCouleur(robotNav);
		Behavior b3 = new LigneNoire(robotNav);
		Behavior b4 = new ArretBouton(arby, robotNav);
		Behavior b5 = new WhereIAm(robotNav);
		
		Behavior[] bArray = {b2,b3,b1,b5,b4}; // du moins prioritaire au plus prioritaire
		arby = new Arbitrator(bArray);
		
		//######################## Arret de l'arbitre
		if(b4 instanceof ArretBouton) {
			ArretBouton b = (ArretBouton)b4;
			b.setArbitrator(arby);
		}
		arby.go();
}
}
