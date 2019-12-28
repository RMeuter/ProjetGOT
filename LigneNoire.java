package ProjetGOT;

import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

/**
 * DESCRIPTION
 *
 * Lorsque le robot "voit" une ligne noire (grâce à son capteur de couleur), il doit alors tourner.
 */

public class LigneNoire implements Behavior{

/**
 * ATTRIBUTS	
 */
	private NavigateurRobot navRobot;
	
/**
 * CONSTRUCTEUR 
 * @param navRobot : la navigation du robot.
 */
	public LigneNoire(NavigateurRobot navRobot) {
		this.navRobot = navRobot;
	}

/**
 *  REQUETE
 */
	/**
	 * Quand le comportement doit prendre le dessus.
	 * Ici, la condition est remplie lorsque la condition "la couleur détectée est égale à du noir" est vraie.
	 */
	public boolean takeControl() { 
		return navRobot.estPasserLigneNoire(true);
	}
	
/**
 * 	COMMANDES
 */
	
	/**
	 *  L'action qui doit être faîte lors de la prise de contrôle du comportement.
	 *  Ici, le robot doit tourner pendant 3000 secondes.
	 */
	public void action() {
		navRobot.tourne();
		Delay.msDelay(3000);
	}
	
	/**
	 *  Le(s) comportement(s) supprimé(s) par le nouveau comportement. 
	 * Ici, le robot doit arrêter d'être arrêté. 
	 */
	public void suppress() {
		navRobot.pilot.stop();
	}
}
