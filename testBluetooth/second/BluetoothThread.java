package ProjetGOT.testBluetooth.second;
import lejos.hardware.Bluetooth;
import lejos.hardware.Button;
import lejos.hardware.LocalBTDevice;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BluetoothThread extends Thread
{
    /** Bluetooth connection variable */
    private int NOTCONNECTED = 0; // Il est �gale � -1 s'il est emetteur
    private int SOCKETOPENED = 1;
    private int CONNECTED = 2;
    private int ERROR = 3;
    private int TERMINATE = 4;
    private int state=NOTCONNECTED;
    private int timeout = 100000;
    private BTConnector btConnector = null;
    private String message = null;
    private DataInputStream reader = null;
    private DataOutputStream writer = null;
    private int bytes;
    private int data;
    private byte[] inBuffer;
    private byte[] outBuffer;
    private BTConnection btConnection;

    /** Lcd messages */
    private String waiting = "Waiting for\n connection..";
    private String connected = "Connected";
    private String closing = "Closing connection";
    private String error = "Connection error";
    private String terminated = "Terminated";
    private String socket = "Socket opened";
    
    /** Robot */
    private boolean go = true;

    public BluetoothThread(boolean Camp) {
    	if (Camp) this.NOTCONNECTED = -1;
	}

    @Override
    public void run()
    {
        setup();
        while(go && state!=TERMINATE)
        {
            if(state==NOTCONNECTED)
            {	
            	if (NOTCONNECTED==0) btConnection = btConnector.waitForConnection(timeout, BTConnection.RAW);
            	else {
            		EV3 ev = LocalEV3.get();
            		System.out.println("--"+ev.getName()+"--");
            		Button.RIGHT.waitForPressAndRelease();
        			BTConnector bt = new BTConnector();
        			BTConnection btc = bt.connect("00:16:53:43:EB:88", NXTConnection.PACKET);
            	}
            	System.out.println(waiting);
                if(btConnection != null)
                {
                    openStream();
                }
                else {
                    System.out.println("Connection timed out: waited 100 sec");
                }
            }
            else if(state==SOCKETOPENED)
            {
                System.out.println("Waiting message");
                WaitMessage();
            }
            else if(state == CONNECTED)
            {
                System.out.println("waiting command");
                int length=0;
                length = ReadMessageLength(length);
                inBuffer = new byte[length];
                if(length>0)
                {
                    bytes = ReadMessage();
                    System.out.println("Bytes Read: "+bytes);
                    if (bytes > 0)
                    {
                       
                    }
                }
                else{
                    state = ERROR;
                }
                inBuffer = null;
            }
            else if(state == ERROR)
            {
                System.out.println(error);
                cancelConnection();
                closeStream();
                //closeConnection(btConnection);
                state = NOTCONNECTED;
            }
        }
        System.out.println(terminated);
        System.out.println("Bluetooth Thread ENDED");
    }

    private void cancelConnection()
    {
        btConnector.cancel();
    }


    private void sendSingleData(byte type, int variable)
    {
        outBuffer = new byte[2];
        outBuffer[0] = type;
        outBuffer[1] = (byte) variable;
        sendData();
    }

    private void sendMultipleData(byte type, int[] array,int length)
    {
        outBuffer = new byte[length + 1];
        outBuffer[0] = type;
        for (int i = 0; i < length; i++)
        {
            outBuffer[i + 1] = (byte) array[i];
        }
        sendData();
    }

    private void sendData()
    {
        System.out.println("Sending data");
        try
        {
            writer.writeInt(outBuffer.length);
            writer.write(outBuffer);
            writer.flush();
            System.out.println("Sent:");
            System.out.println("length: "+outBuffer.length);
            System.out.println("data: "+outBuffer);
        }
        catch (IOException e)
        {
            System.out.println("SEND ERROR: is stream open?");
        }
        outBuffer = null;
    }

    private int ReadMessage()
    {
        int bytes = -1;
        try
        {
            bytes = reader.read(inBuffer, 0, inBuffer.length);// bytes read from incoming message
        }
        catch (IOException e)
        {
            System.out.println("ERROR reading message: " + e);
            state = ERROR;
        }
        return bytes;
    }

    private int ReadMessageLength(int length)
    {
        try
        {
            length = reader.readInt(); //read amount of byte
        }
        catch (IOException e)
        {
            System.out.println("Error reading length");
            state = ERROR;
            length = 0;
        }
        return length;
    }

    private void WaitMessage()
    {
        try
        {
            while ((message = getMessage()) != null)
            {
                System.out.println("Received: " + message);
                if (message.equals("hello"))
                {
                    System.out.println("Sent: welcome");
                    writer.writeUTF("welcome");
                    writer.flush();
                    System.out.println("Activating App...");
                    state = CONNECTED;
                    System.out.println(connected);
                    break;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("ERROR: BT Socket crashed!");
            closeStream();
        }
    }

    private void closeConnection()
    {
        if (btConnection != null)
        {
            System.out.println(closing);
            try
            {
                btConnection.close();
                state = NOTCONNECTED;
                System.out.println("Closed");
            }
            catch (IOException e)
            {
                System.out.println("Error closing connection");
                e.printStackTrace();
            }
        }
    }

    public void setup() {
        LocalBTDevice localBTDevice = Bluetooth.getLocalDevice();
        if (!localBTDevice.getVisibility())
        {
            localBTDevice.setVisibility(true);
        }
        if(btConnector == null) btConnector = new BTConnector();
    }

    private void openStream()
    {
        try
        {
            System.out.println("Opening Stream");
            reader = new DataInputStream(btConnection.openInputStream());
            writer = new DataOutputStream(btConnection.openDataOutputStream());
            state = SOCKETOPENED;
            System.out.println(socket);
        }
        catch (Exception ex)
        {
            System.out.println("Error opening stream");
            ex.printStackTrace();
        }
    }

    public String getMessage(){
    	/*
    	 * Attente d'un message 
    	 * 
    	 * 
    	 * */
        try
        {
        	// Revoir la fonction  car le readUTF ne nous interesse pas 
            message = reader.readUTF();
        }
        catch (IOException e)
        {
            System.out.println("Error getting message is socket still opened?");
            state = ERROR;
        }
        return message;
    }

    public void closeStream() {
        System.out.println("Closing stream");
        try
        {
            if(reader!=null)
            {
                reader.close();
                reader = null;
            }
            if(writer!=null)
            {
                writer.close();
                writer = null;
            }
            System.out.println("Closed");
        }
        catch (IOException e)
        {
            System.out.println("Error Closing Stream");
        }
    }

    public void terminate()
    {
        go=false;
    }

}