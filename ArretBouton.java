package OBJECTIF1;

import lejos.hardware.Button;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class ArretBouton implements Behavior{
	
// #### Attributs ####
	private Arbitrator arby;
	private Robot rb;

// #### Constructeur ####
	public ArretBouton(Arbitrator arby, Robot rb) {
		this.arby = arby;
		this.rb = rb;
	}

// #### Méthodes ####
	
	// Déclaration de l'arbitre
	public void setArbitrator(Arbitrator arby) {
		this.arby = arby;
	}
	
	// Raison pour laquelle le comportement prend le dessus
	public boolean takeControl(){
		return Button.LEFT.isDown();
	}
	
	// Action réalisé par le comportement
	public void action(){
		rb.stopProcess();
		arby.stop();
	}
	
	// Comportement ou action supprimé par le comportement actuel
	public void suppress(){
		
	}
}