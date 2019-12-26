package ProjetGOT;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

//DESCRIPTION //
	// Déclaration des caractéristiques et méthodes du robot.

public class Robot {
	
//	ATTRIBUTS //
		protected EV3ColorSensor senseurCouleur;
		protected EV3UltrasonicSensor senseurUltrason;
		protected MovePilot pilot;
		private CalibrageCouleur tabCouleur;
		private float[] distance = new float[]{(float)1.0};
		
		
// CONSTRUCTEURS //
		// Construction du robot.
		public Robot () {
			constructionRobot();
			creationDePerception();
		}
		
		// Construction de la physique du robot.
		public MovePilot getPilot() {
			return pilot;
		}
		
		// Construction de la perception des couleurs du robot.
		public CalibrageCouleur getCalibrateColor() {
			return tabCouleur;
		}
	
//	COMMANDES //
		
		// Initialisation des capteurs.
		private void creationDePerception() {
			this.senseurCouleur = new EV3ColorSensor(SensorPort.S3);
			this.senseurUltrason = new EV3UltrasonicSensor(SensorPort.S4);
			tabCouleur = new CalibrageCouleur(senseurCouleur);
			tabCouleur.Calibrage();	
		}
		
		//Construction des éléments du robot (roues et chassis) et initialisation de la vitesse.
		private void constructionRobot () {
			Wheel wheel1 = WheeledChassis.modelWheel(Motor.B, 56.).offset(-56);
			Wheel wheel2 = WheeledChassis.modelWheel(Motor.C, 56.).offset(56);
			Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, 2);	
			pilot = new MovePilot(chassis);
			pilot.setLinearSpeed(40.);
			pilot.setAngularSpeed(40.);
		}
		
		// Arret du robot (pilote, capteurs).
		public void arretProcessus () {
			pilot.stop();
			senseurCouleur.close();
			senseurUltrason.close();
		}
		
// REQUETES //		
		// Vérifie que la distance entre le robot et un autre objet (un autre robot) est inférieure à 0.1 m.
		public boolean verifyDistance() {
			senseurUltrason.getDistanceMode().fetchSample(distance,0);
			return distance[0]<0.1;
		}
}

