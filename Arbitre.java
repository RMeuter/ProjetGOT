package ProjetGOT;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Arbitre {
	public static void main(String[] args) {
		Button.RIGHT.waitForPressAndRelease();
		
		Carte carte = new Carte(true);
		
		EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S3);
		Arbitrator arby = null;
		
		// definition du chassis
		Wheel wheel1 = WheeledChassis.modelWheel(Motor.B, 56.).offset(-60);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.C, 56.).offset(60);
		Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, 2);
		
		MovePilot pilot = new MovePilot(chassis);
	
		pilot.setLinearSpeed(20.);
		pilot.setAngularSpeed(20.);
		
		Behavior b1 = new Avancer(pilot); // Avancer
		Behavior b2 = new DetectCouleur(cs, pilot, carte);
		Behavior b3 = new LigneNoire(cs, pilot, carte);
		Behavior b4 = new ArretBouton(arby, cs);
		Behavior[] bArray = {b1, b2,b3,b4}; // du moins prioritaire au plus prioritaire
		arby = new Arbitrator(bArray);
		arby.go();
}
}
