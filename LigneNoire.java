package ProjetGOT;

import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

/**
 * DESCRIPTION
 *
 * Lorsque le robot "voit" une ligne noire (gr�ce � son capteur de couleur), il doit alors tourner.
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
	 * Ici, la condition est remplie lorsque la condition "la couleur d�tect�e est �gale � du noir" est vraie.
	 */
	public boolean takeControl() { 
		return navRobot.estPasserLigneNoire(true);
	}
	
/**
 * 	COMMANDES
 */
	
	/**
	 *  L'action qui doit �tre fa�te lors de la prise de contr�le du comportement.
	 *  Ici, le robot doit tourner pendant 3000 secondes.
	 */
	public void action() {
		navRobot.tourne();
		Delay.msDelay(3000);
	}
	
	/**
	 *  Le(s) comportement(s) supprim�(s) par le nouveau comportement. 
	 * Ici, le robot doit arr�ter d'�tre arr�t�. 
	 */
	public void suppress() {
		navRobot.pilot.stop();
	}
}
