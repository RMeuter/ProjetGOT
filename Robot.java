package ProjetGOT;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class Robot {
	
	// Step of config Robot 
	// Feature robot 
	EV3ColorSensor color;
	protected MovePilot pilot;
	// Properties 
	private int biaisAngle = -13;
	// Perception 
	private CalibrageColor tabColor; 
	
	public Robot () {
		color = new EV3ColorSensor(SensorPort.S3);
		buildRobot();
		createPerception();
	}
	
	public MovePilot getPilot() {
		return pilot;
	}
	
	
	//##################################### Perception #####################################
	
	private void createPerception() {
		
		tabColor = new CalibrageColor(color);
		tabColor.Calibrage();	
	}
	
	public CalibrageColor getCalibrateColor() {
		return tabColor;
	}
	
	// ##################################### Bluid Robot ###################################

	private void buildRobot () {
		//######################## definition du chassis
		Wheel wheel1 = WheeledChassis.modelWheel(Motor.B, 56.).offset(-60);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.C, 56.).offset(60);
		Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, 2);	
		pilot = new MovePilot(chassis);
		
		//######################## Alteration de la vitesse de base
		pilot.setLinearSpeed(40.);
		pilot.setAngularSpeed(40.);
	}
	
	//####################################### Stop ##########################################
	
	public void stopProcess () {
		pilot.stop();
		color.close();
		System.out.println("C'est bon ta finit mamène ! ");
		System.exit(0);
	}

}
