package ev3bluetooth;

import java.io.DataInputStream;
import java.io.InputStream;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

public class Recepteur {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String connected = "Connected";
		String waiting = "Waiting";
		
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
	}

}
