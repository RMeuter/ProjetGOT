package ProjetGOT;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class Robot {
	
// #### Attributs ####	
		protected EV3ColorSensor color;
		protected MovePilot pilot;
		private CalibrageColor tabColor;
		protected EV3UltrasonicSensor ultrasonic;

		private float[] value= new float[]{(float)1.0};
		
// #### Constructeur ####
		public Robot () {
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
			this.color = new EV3ColorSensor(SensorPort.S3);
			this.ultrasonic = new EV3UltrasonicSensor(SensorPort.S4);
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
			ultrasonic.close();
		}
		
		public boolean verifyDistance() {
			ultrasonic.getDistanceMode().fetchSample(value,0);
			return value[0]<0.1;
		}
		
		public float writeDistance() {
			ultrasonic.getDistanceMode().fetchSample(value,0);
			return value[0];
		}
}
