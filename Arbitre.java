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
	public static void main(String[] args) {
		Button.RIGHT.waitForPressAndRelease();
		
		// Base
		Arbitrator arby = null;
		Carte carte = null;
		
		// Definition des senseurs
		EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S3);
		
		
		// Calibrage des couleurs
		LCD.drawString("Calibrons", 0, 0);
		Button.waitForAnyEvent();
		LCD.clear();
		CalibrageColor colorTab = new CalibrageColor();
		colorTab.Calibrage(cs);
		
		// Definition du camp
		LCD.drawString("Choisis ton camp", 0, 0);
		Button.waitForAnyPress();
		if (Button.UP.isDown()) {
			carte = new Carte(true);
			LCD.drawString("Sauvageon", 0, 0);
		} else {
			carte = new Carte(false);
			LCD.drawString("Garde de la nuit", 0, 0);
		}
		Delay.msDelay(300);
		LCD.clear();
		
		// definition du chassis
		Wheel wheel1 = WheeledChassis.modelWheel(Motor.B, 56.).offset(-60);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.C, 56.).offset(60);
		Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, 2);	
		MovePilot pilot = new MovePilot(chassis);
		
		//Alteration de la vitesse de base
		pilot.setLinearSpeed(20.);
		pilot.setAngularSpeed(20.);
		
		
		// Definition des comportements
		Behavior b2 = new DetectCouleur(cs, pilot, carte, colorTab);
		Behavior b3 = new LigneNoire(cs, pilot, carte, colorTab);
		Behavior b4 = new ArretBouton(arby, cs, pilot);
		Behavior[] bArray = {b2,b3,b4}; // du moins prioritaire au plus prioritaire
		arby = new Arbitrator(bArray);
		
		// Arret de l'arbitre
		if(b4 instanceof ArretBouton) {
			ArretBouton b = (ArretBouton)b4;
			b.setArbitrator(arby);
		}
		arby.go();
}
}
