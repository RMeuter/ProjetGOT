package ProjetGOT.testBluetooth.second;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

import lejos.hardware.Bluetooth;
import lejos.hardware.Button;
import lejos.hardware.LocalBTDevice;
import lejos.hardware.RemoteBTDevice;
import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.BTConnection;

public class RechercheNamePeriph {
	public static void main(String []arg) {
		String name = "NXT";
	    LCD.drawString("Connecting...", 0, 0);
	    LocalBTDevice btrd = new LocalBTDevice();

	    
	    if (btrd == null) {
	      LCD.clear();
	      LCD.drawString("No such device", 0, 0);
	      Button.waitForAnyPress();
	      System.exit(1);
	    }

	    BTConnection btc = Bluetooth.connect(btrd);

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
	
	protected RemoteDevice getKnownDevice( String identifier ) {
		boolean inquire_address = Bluetooth.isAddress(identifier);

		ArrayList<RemoteDevice> knownDevices = Bluetooth.getKnownDevicesList();
		for( int i = 0; i < knownDevices.size(); i++ ) {
			RemoteDevice btrd = knownDevices.get(i);

			if( inquire_address ) {
				if( btrd.getBluetoothAddress().equals(identifier) ) {
					return btrd;
				}
			} else {
				if( btrd.getFriendlyName(false).equals(identifier) ) {
					return btrd;
				}
			}
		}

		// Kein passendes Gerät gefunden
		return null;
	}

}
