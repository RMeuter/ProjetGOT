package OBJECTIF1;

import java.util.LinkedList;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class TestArbitre {
	public static void main(String[] args) {
		Button.waitForAnyPress();
		
//##### Initialisation de l'arbitre, du biais et du robot ####
		Button.waitForAnyPress();
		int newBiaisAngle = -13;
		RobotNavigator robotNav = new RobotNavigator (newBiaisAngle);
		LCD.drawString("Robot crée", 0, 0);
		LinkedList <Integer> chemin = new LinkedList <Integer>();
		chemin.add(RobotNavigator.SUD);
		chemin.add(RobotNavigator.SUD);
		chemin.add(RobotNavigator.EST);
		chemin.add(RobotNavigator.SUD);
		chemin.add(RobotNavigator.NORD);
		
		for (int x : chemin){
			LCD.drawInt(robotNav.versDirection(x), 0, 0);
			if (robotNav.versDirection(x) == 0){
				robotNav.avance(x);
				if (robotNav.getEtape() == 3){
					robotNav.sarreteNSeconde();
				}
			}else{
				robotNav.tourne(x);
				if (robotNav.getEtape() == 3){
					robotNav.sarreteNSeconde();
				}
			}
		}
		
	}
}
