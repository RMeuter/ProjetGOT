package ProjetGOT.testBluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
		 * - Lancer le second périphérique (en appuyant sur n'importe quel touche sauf gauche)
		 *  avant que les dix seconds ne s'écoulent !
		 *  - Pour envoyer des infos appuyer si n'importe quel bouton sauf gauche
		 *  - Bouton gauche pour sortir
		 *  
		 */
		BluetoothWorker bw = null;
		Button.waitForAnyPress();
		if (Button.LEFT.isDown()) {
			try {
				BTConnector bt = new BTConnector();
				NXTConnection btc = bt.waitForConnection(timeout, NXTConnection.PACKET);

				if (btc !=null) {
					LCD.clear();
					LCD.drawString("Connecter", 0, 0);
					LCD.refresh();
					bw = new BluetoothWorker(true, bt, btc);
					bw.start();
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
    		try {
    			BTConnector bt = new BTConnector();
    			BTConnection btc = bt.connect("00:16:53:43:EB:88", NXTConnection.PACKET);//le premier paramètre est l'adresse du récepteur affiché sur l'écra de l'émetteur après association (pair) bluetooth
    			LCD.clear();
    			LCD.drawString("connecter", 0, 0);
    			LCD.refresh();
    			bw = new BluetoothWorker(false, bt, btc);
    			bw.start();
    			LCD.clear();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
		while(!Button.LEFT.isDown()) {
    		System.out.println("Do you write ?");
    		Button.waitForAnyPress();
    		// Ici je redéfinis un nouvelle itinéraire !
            System.out.println("Of course Jamel ! Envoie donc !");
            try {
				bw.writePosition((int) (Math.random()*7.) , (int) (Math.random()*5.));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		bw.stop = true;
	}
	
	
 
}