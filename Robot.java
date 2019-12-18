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
	

		protected EV3ColorSensor senseurCoulor;
		protected EV3UltrasonicSensor senseurUltrason;
		protected MovePilot pilot;
		private CalibrageCouleur tabColor;
		private float[] distance = new float[]{(float)1.0};
		
		// ############################### Constructeur ############################

		public Robot () {
			constructionRobot();
			creationDePerception();
		}
		
		// ############################### Construction physique/sensorielle du robot ############################
		
		public MovePilot getPilot() {
			return pilot;
		}
		
		public CalibrageCouleur getCalibrateColor() {
			return tabColor;
		}
		
		private void creationDePerception() {
			this.senseurCoulor = new EV3ColorSensor(SensorPort.S3);
			this.senseurUltrason = new EV3UltrasonicSensor(SensorPort.S4);
			tabColor = new CalibrageCouleur(senseurCoulor);
			tabColor.Calibrage();	
		}
		
		//Construction des éléments du robot
		private void constructionRobot () {
			Wheel wheel1 = WheeledChassis.modelWheel(Motor.B, 56.).offset(-56);
			Wheel wheel2 = WheeledChassis.modelWheel(Motor.C, 56.).offset(56);
			Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, 2);	
			pilot = new MovePilot(chassis);
			pilot.setLinearSpeed(40.);
			pilot.setAngularSpeed(40.);
		}
		
		// ############################### Distance ############################
		
		public boolean verifyDistance() {
			senseurUltrason.getDistanceMode().fetchSample(distance,0);
			return distance[0]<0.1;
		}
		
		
		// ############################### Arret ############################
		
		public void arretProcessus () {
			pilot.stop();
			senseurCoulor.close();
			senseurUltrason.close();
		}
		

}
