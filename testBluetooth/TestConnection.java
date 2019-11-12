package ProjetGOT.testBluetooth;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
		 * - Apparailler les deux p�riph�riques
		 * - Lancer le receveur en premier (Bouton gauche)
		 * - T'as 10 seconds negue !
		 * - BOOOMMM ! Allez, fais toi une crepe !
		 * - Lancer le second p�riph�rique (en appuyant sur n'importe quel touche sauf gauche)
		 *  avant que les dix seconds ne s'�coulent !
		 * - Regarder si �a marche (�a marche quand tu dois attendre 10 seconde) 
		 *  
		 */
		
		Button.waitForAnyPress();
		if (Button.LEFT.isDown()) {
			try {
				BTConnector bt = new BTConnector();
				NXTConnection btc = bt.waitForConnection(100000, NXTConnection.PACKET);

				if (btc !=null) {
					LCD.clear();
					LCD.drawString("Connecter", 0, 0);
					LCD.refresh();
					new BluetoothWorker(true, bt, btc).start();
				} else {
					System.out.println("Pas de connexion");
					Button.RIGHT.waitForPressAndRelease();
				}
			} catch (Exception e) {
			}
		}
    	else {
    		EV3 ev = LocalEV3.get();
    		System.out.println("--"+ev.getName()+"--");
    		Button.RIGHT.waitForPressAndRelease();
    		try {
    			BTConnector bt = new BTConnector();
    			BTConnection btc = bt.connect("00:16:53:43:EB:88", NXTConnection.PACKET);//le premier param�tre est l'adresse du r�cepteur affich� sur l'�cra de l'�metteur apr�s association (pair) bluetooth
    			LCD.clear();
    			LCD.drawString("connecter", 0, 0);
    			LCD.refresh();
    			new BluetoothWorker(false, bt, btc).start();
    			LCD.clear();
    			
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
	}
}