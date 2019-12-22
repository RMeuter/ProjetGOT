<<<<<<< HEAD
package ProjetGOT.TestStep;

import ProjetGOT.ArretBouton;
import ProjetGOT.ArriveGoal;
import ProjetGOT.RobotNavigator;
import ProjetGOT.WhereIAm;
import lejos.hardware.Button;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class StepOnlyArbitrator {

	public static void main(String[] args) {
		Button.waitForAnyPress();
		int newBiaisAngle = -13;
		RobotNavigator robotNav = new RobotNavigator (newBiaisAngle);
		while(!Button.LEFT.isDown()) {
			System.out.print("Lancer une action");
			Button.waitForAnyPress();
			if (Button.RIGHT.isDown()) robotNav.tourne();
			else if (Button.DOWN.isDown()) System.out.println(robotNav.verifiePasseLigneNoire(false));
			else if (Button.UP.isDown()) robotNav.sarreteNSeconde();
			else if (Button.ENTER.isDown()) {
				robotNav.pilot.rotate(180/2);
				while (!robotNav.verifiePasseLigneNoire(false)) robotNav.pilot.backward();
				robotNav.pilot.rotate(180/2);
				while (!robotNav.verifiePasseLigneNoire(false)) robotNav.pilot.backward();
			}
			System.out.println("Action faite !");
		}
		robotNav.stopProcess();
	}
}
=======
package ProjetGOT.TestStep;

import ProjetGOT.ArretBouton;
import ProjetGOT.ArriveGoal;
import ProjetGOT.RobotNavigator;
import ProjetGOT.WhereIAm;
import lejos.hardware.Button;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class StepOnlyArbitrator {

	public static void main(String[] args) {
		Button.waitForAnyPress();
		int newBiaisAngle = -13;
		RobotNavigator robotNav = new RobotNavigator (newBiaisAngle);
		while(!Button.LEFT.isDown()) {
			System.out.print("Lancer une action");
			Button.waitForAnyPress();
			if (Button.RIGHT.isDown()) robotNav.tourne();
			else if (Button.DOWN.isDown()) System.out.println(robotNav.verifiePasseLigneNoire(false));
			else if (Button.UP.isDown()) robotNav.sarreteNSeconde();
			else if (Button.ENTER.isDown()) {
				System.out.println("hey");
				robotNav.pilot.rotate(180/2);
				while (!robotNav.verifiePasseLigneNoire(false)) robotNav.pilot.backward();
				robotNav.pilot.rotate(180/2);
				while (!robotNav.verifiePasseLigneNoire(false)) robotNav.pilot.backward();
			}
			System.out.println("Action faite !");
		}
		robotNav.stopProcess();
	}
}
>>>>>>> 3bc8ed9ac6fc22502b0f7d0b21e845a92671e6c6
