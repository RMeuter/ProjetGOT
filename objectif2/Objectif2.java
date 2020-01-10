package ProjetGOT.objectif2;


import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;


public class Objectif2 {
	private static int timeout = 10000;
	

	/**
	 * La fonction main est séparée en deux par une boucle conditionnelle répondant à l'appui du bouton :
	 * 
	 *Si on appuie sur le bouton gauche : Récepteur (uniquement Joffred5).
	 * 	Cette condition initialise la socket bluetooth et attend une connexion.
	 * 	Une fois la connexion obtenue, affichage d'une connexion,
	 * 		puis activation de la theard de l'objet TransmetteurBluetooth.
	 * 	Si la connexion n'est pas faite alors il y a un affichage "aucune connexion"
	 *		puis sortie du programme.
	 *Si on appuie sur le bouton droit : Emetteur (n'importe quel autre robot).
	 * Cette condition initialise la socket bluetooth et cherche une connexion avec Joffred5.
	 * Si elle est obtenu, il y a un affichage d'une connexion,
	 * 		puis activation de la theard de l'objet TransmetteurBluetooth.
	 * Sinon une erreur se produit.
	 * 
	 * */	
	
	public static void main (String [] args) {

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
    			/**
    			 * Adresse de Joffred5 :le premier paramètre est l'adresse du récepteur ,
    			 * affiché sur l'écran de l'émetteur après association (pairage) bluetooth.
    			 */
    			BTConnection btc = bt.connect("00:16:53:43:EB:88", NXTConnection.PACKET);
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