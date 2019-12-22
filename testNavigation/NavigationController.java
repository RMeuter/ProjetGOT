package ProjetGOT.testNavigation;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.DirectionFinder;
import lejos.robotics.DirectionFinderAdapter;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.CompassPoseProvider;
import lejos.robotics.navigation.MoveController;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;

public class NavigationController {
	private RobotController robot;
	private Navigator nav;
	
	public NavigationController (boolean isSauvageon) {
		
		robot = new RobotController();
		nav = new Navigator((MoveController) robot.getPilot());
		robot.setWhereIAm(isSauvageon);	
	}
	
	
	protected void newGoal (String goal) {
		switch (goal) {
			case "CampsSauvageons" : nav.addWaypoint(0, 0); break;
			case "CampsGarde" : nav.addWaypoint(5, 7); break;
			case "MilitaireC" : nav.addWaypoint(4, 3); break;
			case "MilitaireS" : nav.addWaypoint(3, 2); break;
		}
		
	}
	
	
	
}
