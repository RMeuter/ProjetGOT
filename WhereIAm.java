package ProjetGOT;

import java.util.Arrays;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

//Pourquoi faire ? Où veut tu l'utiliser ?

public class WhereIAm implements Behavior {
	Carte carte;
	
	public WhereIAm (Carte carte) {
		this.carte = carte;
	}

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return Button.RIGHT.isDown();
	}

	@Override
	public void action() {
		LCD.drawString(Arrays.toString(carte.getPositionHistorique()), 0, 0);
		Delay.msDelay(1000);
		LCD.clear();
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
