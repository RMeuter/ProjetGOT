package OBJECTIF1;

import lejos.hardware.Button;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class ArretBouton implements Behavior{
	private Arbitrator arby;
	private EV3ColorSensor cs;

	
	public ArretBouton(Arbitrator arby, EV3ColorSensor cs) {
		this.arby = arby;
		this.cs = cs;
	}

	
	public boolean takeControl(){
		return Button.RIGHT.isDown();
	}
	
	public void action(){
		arby.stop();
		cs.close();
	}
	
	public void suppress(){
		
	}
}
