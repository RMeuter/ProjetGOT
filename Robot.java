package ProjetGOT;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class Robot {
	
// #### Attributs ####	
		EV3ColorSensor color;
		public MovePilot pilot;
		private CalibrageColor tabColor; 
		
// #### Constructeur ####
		public Robot () {
			color = new EV3ColorSensor(SensorPort.S3);
			buildRobot();
			createPerception();
		}
		
// #### Méthodes ####	
		
		// #### Requêtes ####
		
		public MovePilot getPilot() {
			return pilot;
		}
		
		public CalibrageColor getCalibrateColor() {
			return tabColor;
		}
		
		// #### Commandes ####
		
		// Création du tableau contenant les couleurs
		private void createPerception() {
			
			tabColor = new CalibrageColor(color);
			tabColor.Calibrage();	
		}
		
		//Construction des éléments du robot
		private void buildRobot () {
			//Definition du chassis
			Wheel wheel1 = WheeledChassis.modelWheel(Motor.B, 56.).offset(-60);
			Wheel wheel2 = WheeledChassis.modelWheel(Motor.C, 56.).offset(60);
			Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, 2);	
			pilot = new MovePilot(chassis);
			
			//Commandes de changement de vitesse d'avancée et de rotation
			pilot.setLinearSpeed(40.);
			pilot.setAngularSpeed(40.);
		}
		
		// Arrêt de tous les processus du robot
		public void stopProcess () {
			pilot.stop();
			color.close();
		}

}
