package ProjetGOT.testBluetooth;

import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.utility.Delay;

public class TestConnection {
	private static int timeout = 10000;
    
	public static void main (String [] args) {
		/**
		 * Instruction : 
		 * - Apparailler les deux périphériques
		 * - Lancer le receveur en premier (Bouton gauche)
		 * - T'as 10 seconds negue !
		 * - BOOOMMM ! Allez, fais toi une crepe !
		 * - Lancer le second périphérique (en appuyant sur n'importe quel touche sauf gauche)
		 *  avant que les dix seconds ne s'écoulent !
		 * - Regarder si ça marche (ça marche quand tu dois attendre 10 seconde) 
		 *  
		 */
		
		Button.waitForAnyPress();
		BTConnector btConnector = null;
		BTConnection btConnection = null;
		try {
			btConnector = new BTConnector();
			if (Button.LEFT.isDown()) {
				btConnection = btConnector.waitForConnection(timeout, BTConnection.RAW);
				//new BluetoothWorker(false, btConnector ,btConnection);
			}
	    	else {
	    		EV3 ev = LocalEV3.get();
	    		System.out.println("--"+ev.getName()+"--");
	    		Button.RIGHT.waitForPressAndRelease();
	    		btConnection = btConnector.connect("00:16:53:43:EB:88", NXTConnection.PACKET);
	    		//new BluetoothWorker(true, btConnector, btConnection);
	    	}
			LCD.drawString("Attends 10 secondes", 0, 0);
			LCD.drawString("Mais psarteck", 0, 1);
			LCD.drawString("T'es connecter negue", 0, 2);
			Delay.msDelay(10000);
			
		} finally {
			try {
				btConnection.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			btConnector.cancel();
		}
		
	}
}