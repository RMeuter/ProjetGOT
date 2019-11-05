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
		Carte carte = null;
		int etape = 1;
		int biaisAngle = -10;
		//---------------->le biais doit etre superieur ou égale à 0 et établit pour un angle de 90 (multiplication prevu tkt)
		
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
		
		switch (Button.waitForAnyPress()) {
			case Button.ID_RIGHT:  
				carte = new Carte(false, biaisAngle, etape);
				LCD.drawString("Garde de la nuit", 0, 0);
				//bl = new Bluetooth(false);
				break;
			case Button.ID_DOWN:
				carte = new Carte(false, biaisAngle, etape);
				LCD.drawString("Garde de la nuit", 0, 0);
				break;
			case Button.ID_LEFT:
				carte = new Carte(true, biaisAngle, etape);
				LCD.drawString("Sauvageon", 0, 0);
				//bl = new Bluetooth(true);
				break;
			default:
				carte = new Carte(true, biaisAngle, etape);
				LCD.drawString("Sauvageon", 0, 0);
				break;
		}
		Delay.msDelay(300);
		LCD.clear();
		
		//######################## definition du chassis
		Wheel wheel1 = WheeledChassis.modelWheel(Motor.B, 56.).offset(-60);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.C, 56.).offset(60);
		Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, 2);	
		MovePilot pilot = new MovePilot(chassis);
		
		//######################## Alteration de la vitesse de base
		pilot.setLinearSpeed(40.);
		pilot.setAngularSpeed(40.);
		
		
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
