package ProjetGOT;

import java.util.Arrays;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class ArriveGoal implements Behavior{

	private RobotNavigator robotNav;
	private int[] etape;

	public ArriveGoal (RobotNavigator robotNav) {
		this.robotNav= robotNav;
	}
	
	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		// Le but est une case soit rouge (camp) soit blanc (ville)
		return  robotNav.isArriveGoal();
	}

	@Override
	public void action() {
		robotNav.pilot.stop();
		LCD.drawString("Tu es arrivée mamène", 0, 3);
		LCD.drawString("Remet moi à la ", 0, 4);
		LCD.drawString("case départ, poto", 0, 5);
		Button.waitForAnyPress();
		LCD.clear();
		
		// Redifinition du goal et de la position initial
		robotNav.addOneMoreMission();
		
		robotNav.setDebut();
		LCD.drawString("Position :"+Arrays.toString(robotNav.getPositionHistorique()), 0, 3); 
		robotNav.doRot();
		robotNav.getPilot().travel(14);
		LCD.clear();
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
