package ProjetGOT.testBluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.utility.Delay;

public class Recepteur {


	public static void main(String [] args) throws Exception {
		LCD.drawString("Se connecter Appuyer", 0, 0);
		Button.waitForAnyPress();
		LCD.clear();
	    String connected = "Connected";
	    String waiting = "Waiting...";
	    String closing = "Closing...";
	      BTConnector bt = new BTConnector();
	    while (true && !Button.LEFT.isDown()) {
	      LCD.drawString(waiting,0,0);

	      NXTConnection connection = bt.waitForConnection(100000, NXTConnection.PACKET); 
	      Delay.msDelay(100000);
	      LCD.clear();
	      LCD.drawString(connected,0,0);

	      DataInputStream dis = connection.openDataInputStream();
	      DataOutputStream dos = connection.openDataOutputStream();

	      for(int i=0;i<100;i++) {
	        int n = dis.readInt();
	        LCD.drawInt(n,7,0,1);
	        dos.writeInt(-n);
	        dos.flush();
	      }
	      dis.close();
	      dos.close();

	      LCD.clear();
	      LCD.drawString(closing,0,0);

	      connection.close();
	      LCD.clear();
	    }
	  } 
}
