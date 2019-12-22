<<<<<<< HEAD
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
=======
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
>>>>>>> 3bc8ed9ac6fc22502b0f7d0b21e845a92671e6c6
}