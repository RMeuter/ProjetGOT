package ProjetGOT;

import lejos.hardware.Button;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

// DESCRIPTION //
	// Le robot doit s'arr�ter lorsque l'on appuie sur le bouton gauche du robot.

public class ArretRobot implements Behavior{
	
//	ATTRIBUTS //
	private Arbitrator arbi;
	private NavigateurRobot navRobot;

// CONSTRUCTEUR //
	public ArretRobot(Arbitrator arbi, NavigateurRobot nr) {
		this.arbi = arbi;
		this.navRobot = nr;
	}
	
// REQUETES //
	// Quand le comportement doit prendre le dessus.
	// Ici, la condition est remplie lorsque le bouton gauche du robot est press�.
	public boolean takeControl(){
		return Button.LEFT.isDown();
	}
	
//	COMMANDES //
	
	// L'action qui doit �tre fa�te lors de la prise de contr�le du comportement.
	// Ici, le robot doit s'arr�ter lorsque l'on appuie sur le bouton gauche.
	public void action(){
		navRobot.arretProcessus();
		arbi.stop();
	}
	
	// Attribue un arbitre 
	public void setArbitrator(Arbitrator arby) {
		this.arbi = arby;
	}
	
	// Le(s) comportement(s) supprim�(s) par le nouveau comportement. 
	// Ici, il n'y a pas de comportements � supprimer.
	public void suppress(){}
}