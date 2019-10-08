package ProjetGOT;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class ArriveGoal implements Behavior{

	private EV3ColorSensor cs;
	private MovePilot pilot;
	private Carte carte;
	private CalibrageColor colorTab;

	public ArriveGoal (EV3ColorSensor cs, MovePilot pilot, Carte carte, CalibrageColor colorTab) {
		this.cs = cs;
		this.pilot = pilot;
		this.carte= carte;
		this.colorTab = colorTab;
	}
	
	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		// Le but est une case soit rouge (camp) soit blanc (ville)
		return  carte.isArriveGoal(); //(colorTab.getCalibreColor(cs) == "rouge"||colorTab.getCalibreColor(cs) == "blanc") &&
	}

	@Override
	public void action() {
		pilot.stop();
		LCD.drawString("Tu es arrivée mamène", 0, 3);
		LCD.drawString("Remet moi à la case départ, poto", 0, 4);
		Button.waitForAnyPress();
		LCD.clear();
		// Redifinition du goal et de la position initial
		carte.setGoal(2);
		carte.setPositionHistorique(carte.getDebut());
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
