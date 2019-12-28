package ProjetGOT;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

/**
 * DESCRIPTION
 * Lorsque le bouton droit du robot est press�, il doit afficher sa position actuelle (coordonn�es).
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
	 * Ici, la condition est remplie lorsque le bouton droit est press�.
	 */
	public boolean takeControl() {
		return Button.RIGHT.isDown();
	}

/**
 * COMMANDES
 */
	
	/**
	 *  L'action qui doit �tre fa�te lors de la prise de contr�le du comportement.
	 *  Ici, le robot doit afficher sa position actuelle (coordonn�es) pendant 3000 secondes.
	 */
	public void action() {
		LCD.clear();
		LCD.drawInt(navRobot.getPosition(), 0, 3);
		Delay.msDelay(3000);
		LCD.clear();
	}

	/**
	 *  Le(s) comportement(s) supprim�(s) par le nouveau comportement.
	 *  Ici, il n'y a pas de comportements � supprimer.
	 */
	public void suppress() {
		
	}


}