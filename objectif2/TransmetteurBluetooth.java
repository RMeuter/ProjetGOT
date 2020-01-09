package ProjetGOT.objectif2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

public class TransmetteurBluetooth extends Thread {
	
/**
 * 	ATTRIBUTS
 */
	private static Socket socket; 
    private BTConnector btConnector = null;
    private BTConnection btConnection = null;
    private NXTConnection ntxConnection = null;
    private boolean isNTXConnexion;
    
    /**
     * IO stream
     */
    static DataInputStream entre;
    static DataOutputStream sortie;
    
    /**
     *  Définition de l'entrée du flux
     */
    static short[] carte; 
    static boolean estSauvageon;
    static boolean estTest = false;
    boolean arret = false;
    static int tempsEcritureChaquePositionCarte = 500;	
/**
*  CONSTRUCTEURS 
*/    
    
    /**
     * Création d'un objet TransmetteurBluetooth.
     * @param Camps:  un boolean définissant le camp.
     * @param btc: un objet BTConnector.
     * @param btconnection: un objet BTConnection.
     */
    public TransmetteurBluetooth(boolean Camps, BTConnector btc, BTConnection btconnection) throws IOException {
    	TransmetteurBluetooth.estSauvageon = Camps;
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
    
    /**
     * Création d'un objet TransmetteurBluetooth. 
     * @param Camps:  un boolean définissant le camp.
     * @param btc: un objet BTConnector.
     * @param ntxconnection :un objet NXTConnection.
     */
    public TransmetteurBluetooth(boolean Camps, BTConnector btc, NXTConnection ntxConnection) throws IOException {
    	TransmetteurBluetooth.estSauvageon = Camps;
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
    
    /**
     * Création d'un objet TransmetteurBluetooth.
     * @param Camps:  un boolean définissant le camps.
     * @param socket: un objet Socket.
     */
    public TransmetteurBluetooth(boolean Camps, Socket socket) {
       	TransmetteurBluetooth.estSauvageon = Camps;
    	TransmetteurBluetooth.socket = socket;
    	estTest = true;
		try {
			entre = new DataInputStream(socket.getInputStream());
	    	sortie = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
  
/**
 * COMMANDES
 */
    /**
     * Exécution de la thread.
     */
    
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
			fermetureCannaux();
		}
    }
    
    /**
     * Open/Close Stream 
     */
    /**
     * Ferme les canaux d'entrées et de sorties (Input et output).
     * Vérifie quelle socket doit être fermée par un suite de booléen.
     */
    public void fermetureCannaux () {
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
    
    
    /**
     * Envoie des données de la carte, définie selon le booléen estSauveageon, par socket.
     */
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
    
    /**
     * Reçoit des données de carte par socket.
     */
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
    
/**
 * REQUETES
 */
    
    /**
     * Carte
     * Création d'une liste représentant la carte suivant le booléen estSauvageon. 
     */
    /**
	 * 1 : champs, 2 : camps, 3 : ville
	 * 4 : mur, 5 : marais, 100 inconnu
	 * */
    private static short [] doCarte() {
    	if (estSauvageon) {
    		return new short[] {

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
    
    /**
     * Position
     * Recois des données de position par socket
     * @return un int qui est la position de 
     * l'autre périphérique
     */
    private static int receptionPosition() throws IOException {
    	return entre.readInt();
    }
    
    /**
     * Envoie des données de position par socket.
     * @param x : un int qui est la position du robot.
     */
    protected void envoiePosition(int x) throws IOException {
    	sortie.writeInt(x);
    	sortie.flush();
    }
}
