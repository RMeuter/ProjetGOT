package OBJECTIF1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

public class Bluetooth {

	public static void main(String[] args) {
		
		String connected = "Connected";
		String waiting = "Waiting";
	
		if (Button.LEFT.isDown()){
			try {
				//LCD.drawString(waiting, 0, 0);
				//LCD.refresh();

				BTConnector bt = new BTConnector();
				NXTConnection btc = bt.waitForConnection(100000, NXTConnection.PACKET);

				if (btc !=null) {
				LCD.clear();
				LCD.drawString(connected, 0, 0);
				LCD.refresh();

				InputStream is = btc.openInputStream();
				//OutputStream os = btc.openOutputStream();
				DataInputStream dis = new DataInputStream(is);
				//DataOutputStream dos = new DataOutputStream(os);

				int valeur = dis.read();
				

				dis.close();
				//dos.close();
				btc.close();
				System.out.println(valeur);
				Button.RIGHT.waitForPressAndRelease();
				LCD.clear();
				} else {
					System.out.println("Pas de connexion");
					Button.RIGHT.waitForPressAndRelease();
				}
			} catch (Exception e) {
			}
		}else if(Button.UP.isDown()) {
			EV3 ev = LocalEV3.get();
			System.out.println("--"+ev.getName()+"--");
			Button.RIGHT.waitForPressAndRelease();
			try {
				
				//LCD.drawString(waiting, 0, 0);
				//LCD.refresh();
				//droite = 00:16:53:43:4E:26
				//gauche = 00:16:53:43:8E:49
				BTConnector bt = new BTConnector();
				BTConnection btc = bt.connect("00:16:53:43:EB:88", NXTConnection.PACKET);//le premier paramètre est l'adresse du récepteur affiché sur l'écra de l'émetteur après association (pair) bluetooth


				LCD.clear();
				LCD.drawString(connected, 0, 0);
				LCD.refresh();

				//InputStream is = btc.openInputStream();
				OutputStream os = btc.openOutputStream();
				//DataInputStream dis = new DataInputStream(is);
				DataOutputStream dos = new DataOutputStream(os);
				System.out.println("\n\nEnvoi");
				dos.write(12); // écrit une valeur dans le flux
				dos.flush(); // force l’envoi
				System.out.println("\nEnvoyé");
				//dis.close();
				dos.close();
				btc.close();
				LCD.clear();
				
			} catch (Exception e) {
			}
		}
			
			
		}
	
	
	}
	
