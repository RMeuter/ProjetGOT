<<<<<<< HEAD
package ProjetGOT;


import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class HeurteRobot implements Behavior{
	private NavigateurRobot navRobot;
	
	public HeurteRobot(NavigateurRobot navRobot) {
		this.navRobot= navRobot;
	}
	
	public boolean takeControl() {
		return navRobot.verifyDistance();
	}

	public void action() {
		LCD.clear();
		LCD.drawString("Dectection obstacle", 0, 0);
		Delay.msDelay(4000);
		if (navRobot.getAttenteNouvelleCarte()) {
			navRobot.pilot.backward();
		}else {
			navRobot.getPlaceRobot();
		}
		LCD.clear();
	}

	public void suppress() {
		navRobot.pilot.stop();
	}
}
=======
package ProjetGOT;


import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class HeurteRobot implements Behavior{
	private NavigateurRobot navRobot;
	
	public HeurteRobot(NavigateurRobot navRobot) {
		this.navRobot= navRobot;
	}
	
	public boolean takeControl() {
		return navRobot.verifyDistance();
	}

	public void action() {
		LCD.clear();
		LCD.drawString("Dectection obstacle", 0, 0);
		Delay.msDelay(4000);
		if (navRobot.getAttenteNouvelleCarte()) {
			navRobot.pilot.backward();
		}else {
			navRobot.getPlaceRobot();
		}
		LCD.clear();
	}

	public void suppress() {
		navRobot.pilot.stop();
	}
}
>>>>>>> 3bc8ed9ac6fc22502b0f7d0b21e845a92671e6c6
