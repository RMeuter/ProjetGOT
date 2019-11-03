package ProjetGOT;

import lejos.hardware.Button;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class ArretBouton implements Behavior{
	private Arbitrator arby;
	private EV3ColorSensor cs;
	private MovePilot pilot;

	
	public ArretBouton(Arbitrator arby, EV3ColorSensor cs, MovePilot pilot) {
		this.arby = arby;
		this.cs = cs;
		this.pilot = pilot;
	}

	public void setArbitrator(Arbitrator arby) {
		this.arby = arby;
	}
	
	public boolean takeControl(){
		return Button.LEFT.isDown();
	}
	
	public void action(){
		pilot.stop();
		cs.close();
		arby.stop();
		System.out.println("C'est bon ta finit mamène ! ");
		System.exit(0);
	}
	
	public void suppress(){
		
	}
}
