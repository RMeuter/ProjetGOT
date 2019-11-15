package OBJECTIF1;

import java.util.LinkedList;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class TestArbitre {
	public static void main(String[] args) {
		Button.waitForAnyPress();
		
//##### Initialisation de l'arbitre, du biais et du robot ####

		short newBiaisAngle = 10;
		RobotNavigator robotNav = new RobotNavigator (newBiaisAngle);
		byte goal = robotNav.getGoal();
		byte position = robotNav.getPosition();
		
		Dijkstra dijkstra = new Dijkstra(position, goal);
		
		System.out.println(position);
		Button.waitForAnyPress();
		System.out.println(goal);
		Button.waitForAnyPress();
		LinkedList <Short> chemin = dijkstra.dijkstra();
		
		System.out.println(chemin);
		Button.waitForAnyPress();
		
		LCD.clear();
		
		for (short x : chemin){
			LCD.drawInt(robotNav.versDirection(x), 0, 0);
			if (robotNav.versDirection(x) == 0){
				robotNav.avance(x);
			}else{
				robotNav.tourne(x);
				robotNav.avance(x);
			}
		}
		
	}
}
