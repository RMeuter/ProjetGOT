package ProjetGOT;

import lejos.hardware.Button;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;


public class WhereIAm implements Behavior {
	
// #### Attributs ####
	RobotNavigator robotNav;
	
// #### Constructeur ####	
	public WhereIAm (RobotNavigator robotNav) {
		this.robotNav= robotNav;
	}

// #### Méthodes ####	
	
	// Raison pour laquelle le comportement prend le dessus
	@Override
	public boolean takeControl() {
		return Button.RIGHT.isDown();
	}

	// Action réalisé par le comportement
	@Override
	public void action() {
		System.out.println(robotNav.getPosition());
		Delay.msDelay(1000);
	}

	// Comportement ou action supprimé par le comportement actuel
	@Override
	public void suppress() {
		
	}

}