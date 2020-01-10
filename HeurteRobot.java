package ProjetGOT;


import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

/**
 * DESCRIPTION 
 * 
 * Si le robot détecte un objet/robot, il doit l'éviter.
 */
	
public class HeurteRobot implements Behavior{
	
/**
 * 	ATTRIBUTS 	
 */
	private NavigateurRobot navRobot;
	
/**
 *  CONSTRUCTEUR 
 * @param navRobot : la navigation du robot.
 */
	public HeurteRobot(NavigateurRobot navRobot) {
		this.navRobot= navRobot;
	}

/**
 *  REQUETES 
 */
	/**
	 *  Quand le comportement doit prendre le dessus.
	 *  Ici, la condition est remplie lorsque la distance avec un autre objet/robot est inférieure à 10 cm.
	 */ 
	public boolean takeControl() {
		return navRobot.verifyDistance();
	}

/**
 * 	COMMANDES 
 */
	
	/**
	 *  L'action qui doit être faîte lors de la prise de contrôle du comportement.
	 *  Ici, le robot doit afficher qu'il a détecté un objet/robot et si il a obtenu la nouvelle carte de navigation, il doit reculer.
	 *  Et s'il n'a pas obtenu la nouvelle carte, il doit en créer une nouvelle avec la place du robot ennemi. 
	 */
	public void action() {
		LCD.clear();
		LCD.drawString("Dectection obstacle", 0, 0);
		Delay.msDelay(4000);
		if (navRobot.getAttenteNouvelleCarte()) {
			navRobot.pilot.backward();
		}else {
			navRobot.getPlaceRobot();
		}
		LCD.clear();
	}
	
	/**
	 *  Le(s) comportement(s) supprimé(s) par le nouveau comportement.
	 *  Ici, le robot doit arrêter de s'arrêter. 
	 */
	public void suppress() {
		navRobot.pilot.stop();
	}
}
