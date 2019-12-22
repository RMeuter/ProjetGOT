package ProjetGOT;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;


public class MeLocaliser implements Behavior {
	
	NavigateurRobot navRobot;
	

	public MeLocaliser (NavigateurRobot navRobot) {
		this.navRobot = navRobot;
	}


	public boolean takeControl() {
		return Button.RIGHT.isDown();
	}

	public void action() {
		LCD.clear();
		LCD.drawInt(navRobot.getPosition(), 0, 3);
		Delay.msDelay(3000);
		LCD.clear();
	}

	public void suppress() {
		
	}


}