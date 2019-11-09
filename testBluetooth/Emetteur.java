package ProjetGOT.testBluetooth;


import java.io.DataInputStream;
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




public class Emetteur {
	public static void main(String[] args) throws Exception {
	    LCD.drawString("Connecting...", 0, 0);


	    BTConnector bt = new BTConnector();
		BTConnection btc = bt.connect("00:16:53:43:EB:88", NXTConnection.PACKET);//le premier paramètre est l'adresse du récepteur affiché sur l'écra de l'émetteur après association (pair) bluetooth


	    if (btc == null) {
	      LCD.clear();
	      LCD.drawString("Connect fail", 0, 0);
	      Button.waitForAnyPress();
	      System.exit(1);
	    }
	  
	    LCD.clear();
	    LCD.drawString("Connected", 0, 0);

	    DataInputStream dis = btc.openDataInputStream();
	    DataOutputStream dos = btc.openDataOutputStream();

	    for(int i=0;i<100;i++) {
	      try { 
	        LCD.drawInt(i*30000, 8, 0, 2);
	        dos.writeInt(i*30000);
	        dos.flush(); 
	      } catch (IOException ioe) {
	        LCD.drawString("Write Exception", 0, 0);
	      }
	    
	      try {
	        LCD.drawInt(dis.readInt(),8, 0,3);
	      } catch (IOException ioe) {
	        LCD.drawString("Read Exception ", 0, 0);
	      }
	    }
	  
	    try {
	      LCD.drawString("Closing... ", 0, 0);
	      dis.close();
	      dos.close();
	      btc.close();
	    } catch (IOException ioe) {
	      LCD.drawString("Close Exception", 0, 0);
	    }
	  
	    LCD.drawString("Finished",3, 4);
	    Button.waitForAnyPress();
	  }

}
