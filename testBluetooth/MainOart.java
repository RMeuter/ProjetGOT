package ProjetGOT.testBluetooth;

import java.io.IOException;
import java.util.Arrays;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class MainOart {
	public static void main (String[] arg) throws IOException {
		// on test le bleutooth
		
		// on prepare la carte et la position dynamique
		Bluetooth bl =null;
		int[][] CarteCouleur;
		int positionDynamique;
		LCD.drawString("Haut pour rechercher et reste pour trouver le signal", 0, 0);
		Button.waitForAnyPress();
		LCD.clear();
		switch(Button.waitForAnyPress()) {
		case Button.ID_UP:
			bl = new Bluetooth(true);
			CarteCouleur = new int[][]{
			    {-2, 10, 1, 1, -1},
				{0, 10, 1, 1, 1},
				{0, 10, 10, 1, 5},
				{0, 0, 10, 1, 1},
				{0, 0, 0, 5, 1},
				{0, 0, 0, 0, 10},
				{0, 0, 0, 0, 10}
			};
			positionDynamique = 0;
			break;
		default:
			bl = new Bluetooth(false);
			CarteCouleur = new int[][] {
			 	{-2, 10, 0, 0, 0},
				{1, 10, 0, 0, 0},
				{1, 10, 10, 0, 0},
				{1, 1, 10, 0, 0},
				{1, 5, 5, 5, 0},
				{1, 1, 1, -2, 10},
				{-1, 1, 1, 1, 10}
			};
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
			} else if (Button.UP.isDown()) {
				try {
					bl.sendCarte(CarteCouleur);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					int[][] newCarte = bl.receveCarte();
					String test = Arrays.toString(newCarte);
					LCD.drawString("Carte recu :"+test,0,0);
					Button.waitForAnyPress();
					LCD.clear();
					LCD.drawString("Voir la nouvelle carte",0,0);
					Button.waitForAnyPress();
					LCD.clear();
					CarteCouleur = DoingMyMap(newCarte, CarteCouleur);
					LCD.drawString(Arrays.toString(CarteCouleur),0,0);
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
	
	public static int[][] DoingMyMap (int[][] newMap, int[][] lastMap) {
		for (int y = 0; y<lastMap.length; y++) {
			for (int x=0; x<lastMap[y].length; x++) {
				if (lastMap[y][x]==0) {
					lastMap[y][x]=newMap[y][x];
				}
			}
		}
		return lastMap;
	}
}
