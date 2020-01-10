package ProjetGOT;

import lejos.hardware.Button;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/** 
 * DESCRIPTION 
 * 
 * Le robot doit s'arrêter lorsque l'on appuie sur le bouton gauche du robot.
 */
public class ArretRobot implements Behavior{
	
/** 
 * ATTRIBUTS
 */
	private Arbitrator arbi;
	private NavigateurRobot navRobot;

/**
 * CONSTRUCTEUR 
 */
	
	/**	
	 * @param arbi : un objet qui initiale l'arbitre du robot.
	 * @param nr : un objet qui initialise la navigation du robot.
	 */
	public ArretRobot(Arbitrator arbi, NavigateurRobot nr) {
		this.arbi = arbi;
		this.navRobot = nr;
	}
	
/**
 *  REQUETES 
 */

	/**	
	 *  Quand le comportement doit prendre le dessus.
	 *  Ici, la condition est remplie lorsque le bouton gauche du robot est pressé. 
	 */
	public boolean takeControl(){
		return Button.LEFT.isDown();
	}
	
/**	
 * COMMANDES
 */
	
	/**
	 *  L'action qui doit être faîte lors de la prise de contrôle du comportement.
	 *  Ici, le robot doit s'arrêter lorsque l'on appuie sur le bouton gauche.
	 */
	public void action(){
		navRobot.arretProcessus();
		arbi.stop();
	}
	
	/**
	 *  Attribue un arbitre 
	 * @param arby : un objet qui est arbitre du robot.
	 */
	public void setArbitrator(Arbitrator arby) {
		this.arbi = arby;
	}
	
	/**
	 * Le(s) comportement(s) supprimé(s) par le nouveau comportement. 
	 * Ici, il n'y a pas de comportements à supprimer.
	 */
	public void suppress(){}
}