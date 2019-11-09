package ProjetGOT;

import lejos.hardware.Button;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class ArretBouton implements Behavior{
	private Arbitrator arby;
	private Robot rb;

	
	public ArretBouton(Arbitrator arby, Robot rb) {
		this.arby = arby;
		this.rb = rb;
	}

	public void setArbitrator(Arbitrator arby) {
		this.arby = arby;
	}
	
	public boolean takeControl(){
		return Button.LEFT.isDown();
	}
	
	public void action(){
		rb.stopProcess();
		arby.stop();
	}
	
	public void suppress(){
		
	}
}
