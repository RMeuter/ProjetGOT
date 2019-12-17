package ProjetGOT.testBluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import lejos.hardware.Button;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

public class BluetoothWorker extends Thread {
	static Socket socket;
    private BTConnector btConnector = null;
    private BTConnection btConnection = null;
    private NXTConnection ntxConnection = null;
    private boolean isNTXConnexion;
    // IO stream
    static DataInputStream input;
    static DataOutputStream output;
    // definition de l'entree du flux
    static short[] carte; 
    static boolean isSauvageon;
    static boolean isTest = false;
    boolean stop = false;
    static int timeForWrite = 500;
    
//    public static void main (String args[])  {
//    	int TCP_SERVER_PORT = 9701;
//    	boolean test = false;
//    	/*
//    	 * Pour tester il faut :
//    	 * -> activer une permiere fois le main avec test = true
//    	 * -> activer une seconde fois avec test = false
//    	 * */
//    	
//    	if (test) {
//    		ServerSocket serversocket = null;
//            try {
//    			serversocket = new ServerSocket(TCP_SERVER_PORT);
//    		} catch (IOException e1) {
//    			e1.printStackTrace();
//    		}
//            while (true) {
//                Socket clientsocket;
//    			try {
//    				clientsocket = serversocket.accept();
//    				new BluetoothWorker(false, clientsocket).start();
//    			} catch (IOException e) {
//    				e.printStackTrace();
//    			}
//            } 
//    	} else {
//    		Socket socket;
//    		try {
//    			socket = new Socket("localhost", 9701);
//    	    	BluetoothWorker tc = new BluetoothWorker(true, socket);
//    	    	tc.start();
//    		} catch (IOException e) {
//    			e.printStackTrace();
//    		}   
//    	}
//    }
    
    // ################################## Constructeur ############################################ 
    public BluetoothWorker(boolean Camps, BTConnector btc, BTConnection btconnection) throws IOException {
        // TODO Auto-generated constructor stub
    	this.isSauvageon = Camps;
    	try {
    		this.btConnector = btc;
        	this.btConnection = btconnection;
        	isNTXConnexion = false;
    		input = new DataInputStream(btConnection.openInputStream());
    		output = new DataOutputStream(btConnection.openOutputStream());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
    public BluetoothWorker(boolean Camps, BTConnector btc, NXTConnection ntxConnection) throws IOException {
        // TODO Auto-generated constructor stub
    	this.isSauvageon = Camps;
    	try {
	    	this.btConnector = btc;
	    	this.ntxConnection = ntxConnection;
	    	isNTXConnexion = true;
			input = new DataInputStream(this.ntxConnection.openInputStream());
			output = new DataOutputStream(this.ntxConnection.openOutputStream());
	    } catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public BluetoothWorker(boolean Camps, Socket socket) {
        // TODO Auto-generated constructor stub
    	this.isSauvageon = Camps;
    	this.socket = socket;
    	isTest = true;
		try {
			input = new DataInputStream(socket.getInputStream());
	    	output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    // ################################## run de la thread ############################################ 
    
    public void run() {
    	try {
    		carte = doCarte();
    		System.out.println(Arrays.toString(carte));
    		if (isSauvageon) {
    	        sendCarte();
    	        readCarte();
    		} else {
    			readCarte();
    	        sendCarte();
    		}
	        System.out.println(Arrays.toString(carte));
	        
	        
	        int position = 32;
	        while (!stop) {
	        	if (isSauvageon && isTest) {
	        		System.out.println("Attente de reception");
		        	position = readPosition(); // attention une fois envoyer il ne le garde pas en mémoire donc plutot faire un
		        	// while read !
		            System.out.println("received :" + position);
		            isSauvageon = false;
	        	} else if (!isSauvageon && isTest){
	        		// Ici je redéfinis un nouvelle itinéraire !
		            writePosition((int) (Math.random()*32.));
	                isSauvageon = true;
	        	} else {
	        		System.out.println("received");
		        	position = readPosition(); // attention une fois envoyer il ne le garde pas en mémoire donc plutot faire un
		        	// while read !
		            System.out.println("received :" + position);
	        	}
	        }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeCanaux();
		}
    }
    
    // ######################################### Open/Close Stream ##########################################
    
    public void closeCanaux () {
    	try {
    		input.close();
			output.close();
			if(isNTXConnexion) {
	    		ntxConnection.close();
		    	btConnector.cancel();
	    	} else if (isTest) {
				socket.close();
	    	} else {
				btConnection.close();
		    	btConnector.cancel();
	    	}
    	} catch (IOException e) {
			e.printStackTrace();
		} 
    }
    
    // ######################################### Carte ######################################################
    private static short [] doCarte() {
    	if (isSauvageon) {
    		return new short[] {
    			/*
    			 * 1 les champs
    			 * 2 les camps
    			 * 3 la ville
    			 * 4 le mur
    			 * 5 les marais
    			 * */
    			-2, 10, 1, 1, 0,
				100, 10, 1, 1, 1,
				100, 10, 10, 1, 5,
				100, 100, 10, 1, 1,
				100, 100, 100, 5, 1,
				100, 100, 100, 100, 10,
				100, 100, 100, 100, 10
				};
    	} else {
    		return new short[]{
    				-2, 10, 100, 100, 100,
					1, 10, 100, 100, 100,
					1, 10, 10, 100, 100,
					1, 1, 10, 100, 100,
					1, 5, 5, 5, 100,
					1, 1, 1, -2, 10,
					0, 1, 1, 1, 10
					};
    	}
    }
    
    private static void sendCarte () throws IOException, InterruptedException{
		System.out.println("send map");
    	carte = doCarte();
    	sleep(timeForWrite);
    	for (int i= 0; i< carte.length; i++) {
			sleep(timeForWrite);
			if(carte[i]==-2) output.writeInt(-carte[i]);
			else output.writeInt(carte[i]);
	    	output.flush();
    	}
    }
    private static void readCarte () throws IOException{
    	System.out.println("read map");
        int r = 0;
        int j = 0;
        while(j<carte.length) {
        	r = input.readInt();
        	if(r == 100) {
        		if (r==2) carte[j] = (short) -r;
        		else carte[j] = (short) r;
        	}
        	j++;
        	
        }
    }
 // ######################################### Position ######################################################
    
    private static int readPosition() throws IOException {
    	return input.readInt();
    }
    
    protected void writePosition(int x) throws IOException {
    	output.writeInt(x);
    	output.flush();
    }
}
