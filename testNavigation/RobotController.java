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
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Pose;

public class RobotController {
	private MoveProvider pilot;
	private PoseProvider pp;

	
	public RobotController () {
		// Caratéristique Robot
		Wheel wheel1 = WheeledChassis.modelWheel(Motor.B, 56.).offset(-60);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.C, 56.).offset(60);
		Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, 2);	
		pilot = new MovePilot(chassis);
		// Definition de ces repere dans l'espace par 
		// un Compas utilisant le capteur de lumiere
		EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S3);
		DirectionFinder  df = new DirectionFinderAdapter(cs.getRGBMode());
		pp = new CompassPoseProvider(pilot, df);
	}

	public MoveProvider getPilot() {
		return pilot;
	}

	public PoseProvider getPp() {
		return pp;
	}
	
	public void setWhereIAm(boolean isSauvageon) {
		if(isSauvageon) {
			pp.setPose(new Pose(0,0,0));
		} else {
			pp.setPose(new Pose(5,7,180));
		}
	}
}
