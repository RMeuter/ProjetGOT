package ProjetGOT;

import java.util.Arrays;

import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class LigneNoire implements Behavior{

	private EV3ColorSensor cs;
	private MovePilot pilot;
	private Carte carte;
	private CalibrageColor colorTab;
	
	public LigneNoire(EV3ColorSensor cs, MovePilot pilot, Carte carte, CalibrageColor colorTab) {
		this.cs = cs;
		this.pilot = pilot;
		this.carte= carte;
		this.colorTab = colorTab;
	}
	
	public boolean takeControl() { 
		return colorTab.getCalibreColor(cs) == "noir";
	}
	
	public void suppress() {
	}

	public void action() {
		/*
		 * Recupere la position dynamique proposer par la carte afin que le robot puisse 
		 * tourner sur un nouvelle angle puis avancement de 12 cm pour dépasser la case
		 * */
		try {
			LCD.drawString("Position :"+Arrays.toString(carte.getPositionHistorique()), 0, 3);
			pilot.rotate(carte.getRotate());
			pilot.travel(Carte.getTailleCase()+Carte.getLigneCase());
			//Delay.msDelay(3000);
			LCD.clear();
		} catch (Exception e ) {
			e.printStackTrace();
			cs.close();
		}
	}
}
