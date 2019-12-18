package ProjetGOT;

import lejos.hardware.Button;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class ArretRobot implements Behavior{
	
	private Arbitrator arbi;
	private NavigateurRobot nr;

	public ArretRobot(Arbitrator arbi, NavigateurRobot nr) {
		this.arbi = arbi;
		this.nr = nr;
	}

	public void setArbitrator(Arbitrator arby) {
		this.arbi = arby;
	}
	
	public boolean takeControl(){
		return Button.LEFT.isDown();
	}
	
	public void action(){
		nr.arretProcessus();
		arbi.stop();
	}
	
	public void suppress(){}
}