package ProjetGOT.testBluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;

public class BluetoothWorker extends Thread {
	static Socket socket;
    private BTConnector btConnector = null;
    private BTConnection btConnection = null;
    private boolean isSocketConnexion;
    // IO stream
    static DataInputStream input;
    static DataOutputStream output;
    // definition de l'entree du flux
    static int[][] carte; 
    static boolean isSauvageon;
    
    public static void main (String args[])  {
    	int TCP_SERVER_PORT = 9701;
    	boolean test = false;
    	/*
    	 * Pour tester il faut :
    	 * -> activer une permiere fois le main avec test = true
    	 * -> activer une seconde fois avec test = false
    	 * */
    	
    	if (test) {
    		ServerSocket serversocket = null;
            try {
    			serversocket = new ServerSocket(TCP_SERVER_PORT);
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
            while (true) {
                Socket clientsocket;
    			try {
    				clientsocket = serversocket.accept();
    				new BluetoothWorker(false, clientsocket).start();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
            } 
    	} else {
    		Socket socket;
    		try {
    			socket = new Socket("localhost", 9701);
    	    	BluetoothWorker tc = new BluetoothWorker(true, socket);
    	    	tc.start();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}   
    	}
    }
    
    // ################################## Constructeur ############################################ 
    public BluetoothWorker(boolean Camps, Socket socket) {
        // TODO Auto-generated constructor stub
    	this.isSauvageon = Camps;
    	this.socket = socket;
    	isSocketConnexion = true;
		try {
			input = new DataInputStream(socket.getInputStream());
	    	output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public BluetoothWorker(boolean Camps, BTConnector btc, BTConnection btconnection) {
        // TODO Auto-generated constructor stub
    	this.isSauvageon = Camps;
    	this.btConnector = btc;
    	this.btConnection = btconnection;
    	isSocketConnexion = false;
		try {
			input = new DataInputStream(socket.getInputStream());
	    	output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void run() {
    	try {
    		carte = doCarte();
    		if (isSauvageon) {
    			System.out.println("send map");
    	        sendCarte();
    	        System.out.println("read map");
    	        readCarte();	
    		} else {
    	        System.out.println("read map");
    	        readCarte();
    			System.out.println("send map");
    	        sendCarte();
    		}
	        for (int i = 0; i<carte.length;i++) {
	        	System.out.println(Arrays.toString(carte[i]));
	        }
	        
	        boolean stop = false;
	        int temps;
	        int [] position = {2, 3};
	        
	        while (!stop) {
	        	if (isSauvageon) {
		        	temps = (int) (Math.random()*20000.);
		            System.out.println("temps aléatoire générer :" + temps);
		        	sleep(temps);
		        	position = readPosition();
		            System.out.println("received" + Arrays.toString(position));
		            isSauvageon = false;
	        	} else {
	                // Ici je redéfinis un nouvelle itinéraire !
		            System.out.println("Envoie");
		            writePosition((int) (Math.random()*7.) , (int) (Math.random()*5.));
	                isSauvageon = true;
	        	}
	        }
	        socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// ###### Fermeture des flux et de la socket
	    	try {
	    		input.close();
				output.close();
	    	} catch (IOException e) {
				e.printStackTrace();
			} finally {
				closeCanaux ();	
			}
		}
    }
    
    // ######################################### Open/Close Stream ##########################################
    
    public void closeCanaux () {
    	if(isSocketConnexion) {
    		try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	} else {
    		try {
				btConnection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    		btConnector.cancel();
    	}
    }
    
    // ######################################### Carte ######################################################
    private static int [][] doCarte() {
    	if (isSauvageon) {
    		return new int[][] {
    		 	{-2, 10, 0, 0, 0},
    			{1, 10, 0, 0, 0},
    			{1, 10, 10, 0, 0},
    			{1, 1, 10, 0, 0},
    			{1, 5, 5, 5, 0},
    			{1, 1, 1, -2, 10},
    			{-1, 1, 1, 1, 10}
    		};
    	} else {
    		return new int[][]{
			    {-2, 10, 1, 1, -1},
				{0, 10, 1, 1, 1},
				{0, 10, 10, 1, 5},
				{0, 0, 10, 1, 1},
				{0, 0, 0, 5, 1},
				{0, 0, 0, 0, 10},
				{0, 0, 0, 0, 10}
			};
    	}
    }
    
    private static void sendCarte () throws IOException {
    	int [][] carte= doCarte();
    	output.writeInt(carte.length);
    	output.writeInt(carte[0].length);
    	for (int i= 0;i< carte.length; i++) {
    		for (int j= 0;j < carte[0].length; j++) {
    	    	output.writeInt(carte[i][j]);
        	}
    	}
    }

    private static void readCarte () throws IOException {
    	int line = input.readInt();
    	int inLine = input.readInt();
    	System.out.println(line+inLine);
    	int [][] carte1 = new int[line][inLine];
    	for (int i = 0; i < line; i++) {
    		for (int j = 0; j < inLine; j++) {
    	    	carte1[i][j] = input.readInt();
        	}
    	}
    	madeNewCarte(carte1);
    }
    
    private static void madeNewCarte (int [][] carte1) {
    	for (int i = 0; i < carte1.length; i++) {
    		for (int j = 0; j < carte1[0].length; j++) {
    			if (carte[i][j]==0) {
    				carte[i][j] = carte1[i][j];
    			}
        	}
    	}
    }
    
 // ######################################### Position ######################################################
    
    private static int [] readPosition() throws IOException {
    	int position = input.readInt();
    	int [] pose = {position%5,position/5}; 
    	return pose;
    }
    
    protected static void writePosition(int x, int y) throws IOException {
    	output.writeInt(y*5+x);
    	System.out.println(y*5+x);
    }
}


