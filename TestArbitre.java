package OBJECTIF1;

import java.util.LinkedList;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class TestArbitre {
	public static void main(String[] args) {
		Button.waitForAnyPress();
		
//##### Initialisation de l'arbitre, du biais et du robot ####

		short newBiaisAngle = -15;
		RobotNavigator robotNav = new RobotNavigator (newBiaisAngle);

		
		LinkedList <Short> chemin = robotNav.getChemin();
		

//		robotNav.addOneMoreMission();
//		robotNav.setDebut();
//		robotNav.setGoal();
//	
//		chemin = robotNav.getChemin();
//		
		
		for (short x : chemin){
			if (robotNav.versDirection(x) == 0){
				robotNav.avance(x);
				robotNav.sarreteNSeconde();
			}else{
				robotNav.tourne(x);
				robotNav.sarreteNSeconde();
			}
		}
		
	}
}
