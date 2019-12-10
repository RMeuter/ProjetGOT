package ProjetGOT;


import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class HitRobot implements Behavior{
	private RobotNavigator robotNav;
	
	public HitRobot(RobotNavigator robotNav) {
		this.robotNav= robotNav;
	}
	
	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return robotNav.verifyDistance();
	}

	@Override
	public void action() {
		System.out.println("Obstacle");
		System.out.println(robotNav.writeDistance());
		Delay.msDelay(2000);
		if (robotNav.getWaitNewCarte()) {
			robotNav.pilot.backward();
		}else {
			robotNav.getPlaceRobot();
		}
		
	}

	public void suppress() {
		robotNav.pilot.stop();
	}


	
}
