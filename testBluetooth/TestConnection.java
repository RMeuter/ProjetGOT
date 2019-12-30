package ProjetGOT.testBluetooth;


import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;


public class TestConnection {
	private static int timeout = 10000;
	
	public static void main (String [] args) {
		/**
		 * Instruction : 
		 * - Apparailler les deux périphériques
		 * - Lancer le receveur en premier (Bouton gauche) qui est Jofred8 obligatoirement !
		 * - T'as 10 seconds !
		 * - Lancer le second périphérique (en appuyant sur n'importe quel touche sauf gauche)
		 *  avant que les dix seconds ne s'écoulent !
		 *  - Attendre que les cartes soient échanger.
		 *  
		 */
		TransmetteurBluetooth tb = null;
		Button.waitForAnyPress();
		if (Button.LEFT.isDown()) { 
			try {
				BTConnector bt = new BTConnector();
				NXTConnection btc = bt.waitForConnection(timeout, NXTConnection.PACKET);

				if (btc !=null) {
					LCD.clear();
					LCD.drawString("Connecter", 0, 0);
					LCD.refresh();
					tb = new TransmetteurBluetooth(true, bt, btc);
					tb.start();
				} else {
					LCD.drawString("Aucune connexion", 0, 0);
					LCD.clear();
				}
			} catch (Exception e) {
			}
		}
    	else { 
    		EV3 ev = LocalEV3.get();
    		LCD.drawString("--"+ev.getName()+"--", 0, 0);
    		try {
    			BTConnector bt = new BTConnector();
    			BTConnection btc = bt.connect("00:16:53:43:EB:88", NXTConnection.PACKET);//Adresse de Joffred5 :le premier paramètre est l'adresse du récepteur affiché sur l'écra de l'émetteur après association (pair) bluetooth
    			LCD.clear();
    			LCD.drawString("connecter", 0, 0);
    			LCD.refresh();
    			tb = new TransmetteurBluetooth(false, bt, btc);
    			tb.start();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
	}
	
	
}