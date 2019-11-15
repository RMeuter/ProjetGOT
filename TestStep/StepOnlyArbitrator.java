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
		Button.waitForAnyEvent();
		while(Button.LEFT.isDown()) {
			Button.waitForAnyEvent();
			if (Button.RIGHT.isDown()) robotNav.tourne();
			else if (Button.DOWN.isDown()) robotNav.verifiePasseLigneNoire(true);
			else if (Button.UP.isDown()) robotNav.sarreteNSeconde();
			System.out.println("Action faite !");
		}
		robotNav.stopProcess();
	}
}
