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
		
		//######################## Base
		Arbitrator arby = 0;
		Carte carte = 0;
		Bluetooth bl = 0;
		
		//######################## Definition des senseurs
		EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S3);
		
		
		//######################## Calibrage des couleurs
		LCD.drawString("Calibrons", 0, 0);
		Button.waitForAnyEvent();
		LCD.clear();
		CalibrageColor colorTab = new CalibrageColor();
		colorTab.Calibrage(cs);
		
		//######################## Definition du camp avec choix d'utiliser le bluetooth
		LCD.drawString("Choisis ton camp", 0, 0);
		LCD.drawString("H/G pour Sauvageons", 0, 1);
		LCD.drawString("B/reste pour garde de nuit", 0, 2);
		LCD.drawString("Les boutons de coté D/G sont pour le bleutooth et autre ss bluetooth ", 0, 3);
		Button.waitForAnyPress();
		
		if (Button.UP.isDown()) {
			carte = new Carte(true);
			LCD.drawString("Sauvageon", 0, 0);
		} else if (Button.DOWN.isDown()) {
			carte = new Carte(false);
			LCD.drawString("Garde de la nuit", 0, 0);
		} else if (Button.LEFT.isDown()) {
			carte = new Carte(true);
			LCD.drawString("Sauvageon", 0, 0);
			bl = new Bluetooth(true);
		} else {
			carte = new Carte(false);
			LCD.drawString("Garde de la nuit", 0, 0);
			bl = new Bluetooth(false);
		}
		Delay.msDelay(300);
		LCD.clear();
		
		//######################## definition du chassis
		Wheel wheel1 = WheeledChassis.modelWheel(Motor.B, 56.).offset(-60);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.C, 56.).offset(60);
		Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, 2);	
		MovePilot pilot = new MovePilot(chassis);
		
		//######################## Alteration de la vitesse de base
		pilot.setLinearSpeed(50.);
		pilot.setAngularSpeed(50.);
		
		
		//######################## Definition des comportements
		Behavior b1 = new ArriveGoal(cs, pilot, carte, colorTab);
		Behavior b2 = new DetectCouleur(cs, pilot, carte, colorTab);
		Behavior b3 = new LigneNoire(cs, pilot, carte, colorTab);
		Behavior b4 = new ArretBouton(arby, cs, pilot);
		Behavior b5 = new WhereIAm(carte);
		
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
