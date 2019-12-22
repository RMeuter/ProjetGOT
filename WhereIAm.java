<<<<<<< HEAD
package ProjetGOT;

import java.util.Arrays;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
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
		LCD.drawString(Arrays.toString(robotNav.getPosition()), 0, 0);
		Delay.msDelay(1000);
		LCD.clear();
	}

	// Comportement ou action supprimé par le comportement actuel
	@Override
	public void suppress() {
		
	}

=======
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

>>>>>>> 3bc8ed9ac6fc22502b0f7d0b21e845a92671e6c6
}