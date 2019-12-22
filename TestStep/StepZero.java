<<<<<<< HEAD
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
		while(!Button.DOWN.isDown()) {
			Button.waitForAnyPress();
			System.out.println(rb.getCalibrateColor().getCalibreColor());
		}

	}
}
=======
package ProjetGOT.TestStep;

import java.util.Arrays;

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
		while(!Button.DOWN.isDown()) {
			Button.waitForAnyPress();
			System.out.println(rb.getCalibrateColor().getNewCalibreColor());
		}

	}
}
>>>>>>> 3bc8ed9ac6fc22502b0f7d0b21e845a92671e6c6
