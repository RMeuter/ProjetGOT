package ProjetGOT;

import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

/**
 * DESCRIPTION
 * 
 * Le robot doit avancer.
 */
public class Avance implements Behavior{
	
/**
 * ATTRIBUTS 	
 */
	private NavigateurRobot navRobot;
	
/**
 *  CONSTRUCTEUR
 * @param navRobot : navigation du robot.
 */
	public Avance(NavigateurRobot navRobot) {
		this.navRobot = navRobot;
	}
	
/**
 *  REQUETES
 */
	/**
	 *  Quand le comportement doit prendre le dessus.
	 *   Ici, la condition est toujours vraie.
	 *   Le comportement sera effectif lorsqu'aucun autre ne prendra le dessus.
	 */
	public boolean takeControl() {
			return true;
	}

/**
 * 	COMMANDES 
 */
	
	/**
	 *  L'action qui doit être faîte lors de la prise de contrôle du comportement.
	 * 	Ici, le robot doit avancer pendant 1000 secondes.
	 */
	public void action() {
		navRobot.pilot.forward();
		Delay.msDelay(1000);
	}
	
	/**
	 *  Le(s) comportement(s) supprimé(s) par le nouveau comportement.
	 *  Ici, le robot doit arrêter d'être arrêté.
	 */
	public void suppress() {
		navRobot.pilot.stop();
	}
	
}