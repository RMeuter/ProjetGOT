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
    static int[][] carte; 
    static boolean isSauvageon;
    static boolean isTest = false;
    boolean stop = false;
    
    private static void main (String args[])  {
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
	        int temps;
	        int [] position = {2, 3};
	        
	        while (!stop) {
	        	if (isSauvageon && isTest) {
	        		System.out.println("test received");
		        	position = readPosition(); // attention une fois envoyer il ne le garde pas en mémoire donc plutot faire un
		        	// while read !
		            System.out.println("received" + Arrays.toString(position));
		            isSauvageon = false;
	        	} else if (!isSauvageon && isTest){
	        		System.out.println("write");
	        		// Ici je redéfinis un nouvelle itinéraire !
		            System.out.println("Envoie");
		            writePosition((int) (Math.random()*7.) , (int) (Math.random()*5.));
	                isSauvageon = true;
	        	} else {
	        		System.out.println("received");
		        	temps = (int) (Math.random()*20000.);
		            System.out.println("temps aléatoire générer :" + temps);
		        	sleep(temps);
		        	position = readPosition(); // attention une fois envoyer il ne le garde pas en mémoire donc plutot faire un
		        	// while read !
		            System.out.println("received" + Arrays.toString(position));
	        	}
	        }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			closeCanaux();
		} finally {
			closeCanaux();
		}
    }
    
    // ######################################### Open/Close Stream ##########################################
    
    public void closeCanaux () {
    	try {
    		input.close();
			output.close();
    	} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
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
    }
    
    // ######################################### Carte ######################################################
    private static int [][] doCarte() {
    	if (isSauvageon) {
    		return new int[][] {
    			/*
    			 * 1 les champs
    			 * 2 les camps
    			 * 3 la ville
    			 * 4 le mur
    			 * 5 les marais
    			 * */
    		 	{2, 4, 0, 0, 0},
    			{1, 4, 0, 0, 0},
    			{1, 4, 4, 0, 0},
    			{1, 1, 4, 0, 0},
    			{1, 5, 5, 5, 0},
    			{1, 1, 1, 2, 4},
    			{3, 1, 1, 1, 4}
    		};
    	} else {
    		return new int[][]{
			    {2, 4, 1, 1, 3},
				{0, 4, 1, 1, 1},
				{0, 4, 4, 1, 5},
				{0, 0, 4, 1, 1},
				{0, 0, 0, 5, 1},
				{0, 0, 0, 0, 4},
				{0, 0, 0, 0, 4}
			};
    	}
    }
    
    private static void sendCarte () throws IOException {
    	int [][] carte= doCarte();
    	String str = "";
    	for (int i= 0;i< carte.length; i++) {
    		for (int j= 0;j < carte[0].length; j++) {
    	    	str += String.valueOf(carte[i][j]);
        	}
    	}
    	output.writeUTF(str);
    	System.out.println("C'est envoyer !");
    }

    private static void readCarte () throws IOException {
    	String str = input.readUTF();
    	for (int i = 0; i < 7; i++) {
    		for (int j = 0; j < 5; j++) {
    			int numb = Character.getNumericValue (str.charAt(5*i+j));
    			if (numb != 0) carte[i][j] = Character.getNumericValue (str.charAt(5*i+j));
        	}
    	}
    }
 // ######################################### Position ######################################################
    
    private static int [] readPosition() throws IOException {
    	int position = input.readInt();
    	int [] pose = {position%5,position/5}; 
    	return pose;
    }
    
    protected void writePosition(int x, int y) throws IOException {
    	output.writeInt(y*5+x);
    	System.out.println(y*5+x);
    	System.out.println("C'est envoyer !");
    }
}
