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

public class Arbitre {
	public static void main(String[] args) {
		Button.RIGHT.waitForPressAndRelease();
		// Besoin en premier lieu
		Arbitrator arby = null;
		Carte carte = null;
		
		Button.waitForAnyEvent();
		if (Button.UP.isDown()) {
			carte = new Carte(true);
			LCD.drawString("Tu es un sauvageon", 3, 4);
		} else if (Button.DOWN.isDown()) {
			carte = new Carte(false);
			LCD.drawString("Tu n'es pas un sauvageon", 3, 4);
		}
		
		
		// Definition des senseurs
		EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S3);
		
		
		// definition du chassis
		Wheel wheel1 = WheeledChassis.modelWheel(Motor.B, 56.).offset(-60);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.C, 56.).offset(60);
		Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, 2);	
		MovePilot pilot = new MovePilot(chassis);
		
		//Alteration de la vitesse de base
		pilot.setLinearSpeed(20.);
		pilot.setAngularSpeed(20.);
		
		Behavior b2 = new DetectCouleur(cs, pilot, carte);
		Behavior b3 = new LigneNoire(cs, pilot, carte);
		Behavior b4 = new ArretBouton(arby, cs, pilot);
		Behavior[] bArray = {b2,b3,b4}; // du moins prioritaire au plus prioritaire
		arby = new Arbitrator(bArray);
		
		if(b4 instanceof ArretBouton) {
			ArretBouton b = (ArretBouton)b4;
			b.setArbitrator(arby);
		}
		arby.go();
}
}
