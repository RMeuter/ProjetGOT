<<<<<<< HEAD
package ProjetGOT;


import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;

public class ArriveGoal implements Behavior{
	
// #### Attributs ####
	private RobotNavigator robotNav;

	
// #### Constructeur ####	
	public ArriveGoal (RobotNavigator robotNav) {
		this.robotNav= robotNav;
	}
	
	
// #### Méthodes ####
	
	// Raison pour laquelle le comportement prend le dessus
	@Override
	public boolean takeControl() {
		// Le but est une case soit un camp soit une ville
		return  robotNav.isArriveGoal();
	}

	// Action réalisé par le comportement
	@Override
	public void action() {
		robotNav.pilot.stop();
		LCD.drawString("Arrivé !", 0, 3);
		LCD.drawString("Retour à la case départ", 0, 4);
		Button.waitForAnyPress();
		LCD.clear();
		
		//Quand il a finit l'objectif 1, on commence le prochain objectif
		if (robotNav.getEtape() == 1){
			robotNav.addOneMoreMission();
			
		}
	}

	// Comportement ou action supprimé par le comportement actuel
	@Override
	public void suppress() {
		
	}

=======
package ProjetGOT;


import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;

public class ArriveGoal implements Behavior{
	
// #### Attributs ####
	private RobotNavigator robotNav;

	
// #### Constructeur ####	
	public ArriveGoal (RobotNavigator robotNav) {
		this.robotNav= robotNav;
	}
	
	
// #### Méthodes ####
	
	// Raison pour laquelle le comportement prend le dessus
	@Override
	public boolean takeControl() {
		// Le but est une case soit un camp soit une ville
		return  robotNav.isArriveGoal();
	}

	// Action réalisé par le comportement
	@Override
	public void action() {
		robotNav.pilot.stop();
		LCD.drawString("Arrivé !", 0, 3);
		LCD.drawString("Retour à la case départ", 0, 4);
		Button.waitForAnyPress();
		LCD.clear();
		
		//Quand il a finit l'objectif 1, on commence le prochain objectif
		if (robotNav.getEtape() == 1){
			robotNav.addOneMoreMission();
			
		}
	}

	// Comportement ou action supprimé par le comportement actuel
	@Override
	public void suppress() {
		
	}

>>>>>>> 3bc8ed9ac6fc22502b0f7d0b21e845a92671e6c6
}