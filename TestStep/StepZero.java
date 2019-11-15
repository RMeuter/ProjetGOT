package ProjetGOT.TestStep;

import ProjetGOT.Robot;
import ProjetGOT.RobotNavigator;
import lejos.hardware.Button;
import lejos.robotics.subsumption.Arbitrator;

public class StepZero {

	public static void main(String[] args) {
		Button.waitForAnyPress();
		
//##### Initialisation de l'arbitre, du biais et du robot ####
		Robot rb = new Robot();
		System.out.println("Press for view color");
		Button.waitForAnyPress();
		rb.getCalibrateColor().getCalibreColor();
	}
}
