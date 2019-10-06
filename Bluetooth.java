package ProjetGOT;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.utility.Delay;

public class Bluetooth {
	String connected = "Connected";
	String waiting = "Waiting";
	BTConnector bt = new BTConnector();
	NXTConnection btc;
	
	public Bluetooth (boolean camp) {
		if (camp){
			try {
				btc = bt.waitForConnection(100000, NXTConnection.PACKET);
				if (btc !=null) {
					LCD.clear();
					LCD.drawString(connected, 0, 0);
					Delay.msDelay(1000);
					LCD.clear();
				} else {
					System.out.println("Pas de connexion");
					Button.RIGHT.waitForPressAndRelease();
				}
			} catch (Exception e) {
				}
		} else if(Button.UP.isDown()) {
			
			EV3 ev = LocalEV3.get();
			System.out.println("--"+ev.getName()+"--");
			Button.RIGHT.waitForPressAndRelease();
			try {
				//droite = 00:16:53:43:4E:26
				//gauche = 00:16:53:43:8E:49
				btc = bt.connect("00:16:53:43:EB:88", NXTConnection.PACKET);//le premier paramètre est l'adresse du récepteur affiché sur l'écra de l'émetteur après association (pair) bluetooth
				LCD.clear();
				LCD.drawString(connected, 0, 0);
				Delay.msDelay(1000);
				LCD.clear();
			} catch (Exception e) {
			}
		}	
	}
	
	public void send (int position) throws IOException {
		OutputStream os = btc.openOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		System.out.println("\n\nEnvoi");
		dos.write(position); // écrit une valeur dans le flux
		dos.flush(); // force l’envoi
		System.out.println("\nEnvoyé");
		dos.close();
		btc.close();
		LCD.clear();
	}
	
	public int receve() throws IOException {
		InputStream is = btc.openInputStream();
		DataInputStream dis = new DataInputStream(is);
		int position = dis.read();
		dis.close();
		btc.close();
		return position;
	}
	}
	
