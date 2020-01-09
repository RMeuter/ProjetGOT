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
	
	/*
	 * Fonction main est séparer en deux partie par une boucle conditionnel répondant à l'appuie du bouton :
	 * 
	 *Si appuye sur le bouton gauche 
	 * 	Partie récepteur qui est uniquement Joffred5
	 * 	Cette condition initialise la socket bluetooth et attend une connexion
	 * 	Une fois connexion obtenu affichage d'une connexion,
	 * 		puis activiation de la theard de l'objet TransmetteurBluetooth
	 * 	Si la connexion n'est pas faite il y a un affichage d'aucune connexion
	 *		puis sorti du programme
	 *Si appuye sur le bouton droit
	 * Partie émetteur qui peut etre éffectuer par n'importe qu'elle robot
	 * Cette condition initialise la socket bluetooth et cherche une connexion avec Joffred5
	 * Si elle est obtenu il y a un affichage d'une connexion,
	 * 		puis activiation de la theard de l'objet TransmetteurBluetooth
	 * Sinon une erreur est établis.
	 * 
	 * */	
	public static void main (String [] args) {

		// Initialisation de la classe TransmetteurBluetooth
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
    			e.printStackTrace();
			}
		}
    	else { 
    		EV3 ev = LocalEV3.get();
    		LCD.drawString("--"+ev.getName()+"--", 0, 0);
    		try {
    			BTConnector bt = new BTConnector();
    			BTConnection btc = bt.connect("00:16:53:43:EB:88", NXTConnection.PACKET);//Adresse de Joffred5 :le premier paramï¿½tre est l'adresse du rï¿½cepteur affichï¿½ sur l'ï¿½cra de l'ï¿½metteur aprï¿½s association (pair) bluetooth
    			LCD.clear();
    			LCD.drawString("connecter", 0, 0);
    			LCD.refresh();
    			tb = new TransmetteurBluetooth(false, bt, btc);
    			tb.start();
    		} catch (Exception e) {
    		}
    	}
	}
	
	
}