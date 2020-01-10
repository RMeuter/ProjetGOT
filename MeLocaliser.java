package ProjetGOT;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

/**
 * DESCRIPTION
 * Lorsque le bouton droit du robot est pressé, il doit afficher sa position actuelle (coordonnées).
 */

public class MeLocaliser implements Behavior {

/**
 * 	ATTRIBUTS 	
 */
	private NavigateurRobot navRobot;
	
/**
 *  CONSTRUCTEUR 
 * @param navRobot : la navigation du robot.
 */
	public MeLocaliser (NavigateurRobot navRobot) {
		this.navRobot = navRobot;
	}

/**
 *  REQUETES 
 */
	/**
	 * Quand le comportement doit prendre le dessus.
	 * Ici, la condition est remplie lorsque le bouton droit est pressé.
	 */
	public boolean takeControl() {
		return Button.RIGHT.isDown();
	}

/**
 * COMMANDES
 */
	
	/**
	 *  L'action qui doit être faîte lors de la prise de contrôle du comportement.
	 *  Ici, le robot doit afficher sa position actuelle (coordonnées) pendant 3000 secondes.
	 */
	public void action() {
		LCD.clear();
		LCD.drawInt(navRobot.getPosition(), 0, 3);
		Delay.msDelay(3000);
		LCD.clear();
	}

	/**
	 *  Le(s) comportement(s) supprimé(s) par le nouveau comportement.
	 *  Ici, il n'y a pas de comportements à supprimer.
	 */
	public void suppress() {
		
	}


}