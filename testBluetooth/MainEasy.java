package ProjetGOT.testBluetooth;

import java.io.IOException;
import java.util.Arrays;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class MainEasy {
	public static void main (String[] arg) throws IOException {
		// on test le bleutooth
		
		// on prepare la carte et la position dynamique
		Bluetooth bl =null;
		int positionDynamique;
		
		LCD.drawString("Haut pour rechercher et reste pour trouver le signal", 0, 0);
		Button.waitForAnyPress();
		LCD.clear();
		
		switch(Button.waitForAnyPress()) {
		case Button.ID_UP:
			bl = new Bluetooth(true);
			positionDynamique = 0;
			break;
		default:
			bl = new Bluetooth(false);
			positionDynamique = 180;
		}
		
		LCD.drawString("Down button for exit!", 0, 0);
		Button.waitForAnyEvent();
		LCD.clear();
		
		// ################### On teste l'envoie des données
		int [] testPosition = new int [] {0,0};
		while(!Button.DOWN.isDown() && bl.btc != null) {
			Button.waitForAnyPress();
			if(Button.LEFT.isDown()) {
				try {
					bl.sendPosition(testPosition);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (Button.RIGHT.isDown()) {
				try {
					String test = Arrays.toString(bl.recevePosition());
					LCD.drawString(test,0,0);
					Button.waitForAnyPress();
					LCD.clear();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
		}
		try {
			bl.exit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
