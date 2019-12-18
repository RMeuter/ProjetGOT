package ProjetGOT.testBluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

public class TransmetteurBluetooth extends Thread {
	static Socket socket;
    private BTConnector btConnector = null;
    private BTConnection btConnection = null;
    private NXTConnection ntxConnection = null;
    private boolean isNTXConnexion;
    
    // IO stream
    static DataInputStream entre;
    static DataOutputStream sortie;
    
    // definition de l'entree du flux
    static short[] carte; 
    static boolean estSauvageon;
    static boolean estTest = false;
    boolean arret = false;
    static int tempsEcritureChaquePositionCarte = 500;
    
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
    public TransmetteurBluetooth(boolean Camps, BTConnector btc, BTConnection btconnection) throws IOException {
    	this.estSauvageon = Camps;
    	try {
    		this.btConnector = btc;
        	this.btConnection = btconnection;
        	isNTXConnexion = false;
    		entre = new DataInputStream(btConnection.openInputStream());
    		sortie = new DataOutputStream(btConnection.openOutputStream());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
    public TransmetteurBluetooth(boolean Camps, BTConnector btc, NXTConnection ntxConnection) throws IOException {
    	this.estSauvageon = Camps;
    	try {
	    	this.btConnector = btc;
	    	this.ntxConnection = ntxConnection;
	    	isNTXConnexion = true;
			entre = new DataInputStream(this.ntxConnection.openInputStream());
			sortie = new DataOutputStream(this.ntxConnection.openOutputStream());
	    } catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public TransmetteurBluetooth(boolean Camps, Socket socket) {
        // TODO Auto-generated constructor stub
    	this.estSauvageon = Camps;
    	this.socket = socket;
    	estTest = true;
		try {
			entre = new DataInputStream(socket.getInputStream());
	    	sortie = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    // ################################## run de la thread ############################################ 
    
    public void run() {
    	try {
	        LCD.clear();
    		carte = doCarte();
    		if (estSauvageon) {
    	        envoieCarte();
    	        LCD.clear();
    	        receptionCarte();
    	        LCD.clear();
    		} else {
    			receptionCarte();
    			LCD.clear();
    	        envoieCarte();
    	        LCD.clear();
    		}
        	LCD.drawString("Carte reçu", 0, 3);
	        int position = 32;
	        while (!arret) {
	        	if (estSauvageon && estTest) {
		        	position = receptionPosition();
		        	LCD.clear();
		        	LCD.drawString("Position reçu :" + position, 0, 3);
		            estSauvageon = false;
	        	} else if (!estSauvageon && estTest){
	        		position = (int) (Math.random()*32.);
		            envoiePosition(position);
		        	LCD.clear();
		        	LCD.drawString("Envoie position :"+ position, 0, 3);
	                estSauvageon = true;
	        	} else {
		        	position = receptionPosition();
		        	LCD.clear();
		        	LCD.drawString("Position reçu :" + position, 0, 3);
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
    		entre.close();
			sortie.close();
			if(isNTXConnexion) {
	    		ntxConnection.close();
		    	btConnector.cancel();
	    	} else if (estTest) {
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
    	if (estSauvageon) {
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
    
    private static void envoieCarte () throws IOException, InterruptedException{
    	LCD.drawString("Envoie Map", 0, 3);
    	carte = doCarte();
    	sleep(tempsEcritureChaquePositionCarte);
    	for (int i= 0; i< carte.length; i++) {
			sleep(tempsEcritureChaquePositionCarte);
			if(carte[i]==-2) sortie.writeInt(-carte[i]);
			else sortie.writeInt(carte[i]);
	    	sortie.flush();
    	}
    }
    private static void receptionCarte () throws IOException {
    	LCD.drawString("Reception Map", 0, 3);
        int r = 0;
        int j = 0;
        while(j<carte.length) {
        	r = entre.readInt();
        	if(r == 100) {
        		if (r==2) carte[j] = (short) -r;
        		else carte[j] = (short) r;
        	}
        	j++;
        	
        }
    }
 // ######################################### Position ######################################################
    
    private static int receptionPosition() throws IOException {
    	return entre.readInt();
    }
    
    protected void envoiePosition(int x) throws IOException {
    	sortie.writeInt(x);
    	sortie.flush();
    }
}
