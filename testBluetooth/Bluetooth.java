package ProjetGOT.testBluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.utility.Delay;

public class Bluetooth {
	String connected = "Connected";
	String waiting = "Waiting";
	BTConnector bt;
	NXTConnection btc;
	
	//https://github.com/Blukain/LejosEv3/blob/master/src/BluetoothThread.java
	
	
	public Bluetooth (boolean camp) throws IOException {
		/*
		 * On définit un camp  qui nous permet de savoir si on est donner ou recepteur du permier signal de liaison
		 * Le recepteur du signal attent lui 1000 seconde (a toi de convertir en minute, moi flemme! )
		 * Sinon l'autre recherche 
		 * 
		 * Il faut activer l'attente de connection avant la recherche
		 * 
		 * */
		this.bt = new BTConnector();
		
		if (camp){
			try {
				LCD.drawString("Attente", 0, 1);
				btc = bt.waitForConnection(100000, NXTConnection.PACKET);
				
				if (btc != null) {
					LCD.drawString(connected, 0, 2);
					Delay.msDelay(1000);
					LCD.clear();
				} else {
					LCD.drawString("Pas de connexion",0,2);
					LCD.drawString("Retentez ?",0,3);
					Button.RIGHT.waitForPressAndRelease();
					LCD.clear();
				}
			} catch (Exception e) {
				e.printStackTrace();
				exit();
				}
		} else {
			LCD.drawString("Recherche", 0, 1);
			EV3 ev = LocalEV3.get();
			LCD.drawString("--"+ev.getName()+"--", 0, 2);
			Button.waitForAnyPress();
			LCD.clear();
			try {
				//droite = 00:16:53:43:4E:26
				//gauche = 00:16:53:43:8E:49
				btc = bt.connect("00:16:53:43:EB:88", NXTConnection.PACKET);//le premier paramètre est l'adresse du récepteur affiché sur l'écra de l'émetteur après association (pair) bluetooth
				
				LCD.clear();
				LCD.drawString(connected, 0, 1);
				Delay.msDelay(1000);
				LCD.clear();
			} catch (Exception e) {
				e.printStackTrace();
				exit();
			}
		}	
	}
	
	// ################################################# Explication pour les permier envoie et recoit
	
	// ######################### Envoie de la position
	public void sendPosition (int[] position) throws IOException {
		/*
		 * Envoie des coordonnées codé sous forme 
		 * d'un tableau de deux elements soit [x, y]
		 * */
		// Ouverture des sortie de flux
		OutputStream os = btc.openOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		try {
			LCD.drawString("\n\nEnvoi", 0, 0);
			Delay.msDelay(1000);
			LCD.clear();
			
			//  Prepare à envoyer la liste donner en paramettre
			for (int x=0; x<position.length;x++) {
				dos.write(position[x]);
			}
			
			// Envoie les données
			dos.flush(); 
			
			// Verification par le LCD
			LCD.drawString("\n\nEnvoyé", 0, 0);
			Delay.msDelay(1000);
			LCD.clear();
			// Fermetture du flux de données mais pas du flux générale -> os !
			dos.close();
		} catch(Exception e) {
	         // if any I/O error occurs
	         e.printStackTrace();
	         exit();
	    } finally {
	         // releases any associated system files with this stream
	         if(dos!=null)
	            dos.close();
	    } 
	}
	
	public int[] recevePosition () throws IOException {
		/* 
		 * Retourne des coordonnées codé sous forme 
		 * d'un tableau de deux elements soit [x, y]
		 * La liste est la position de l'autre robot;
		 * */
		
		// ouverture des ports 
		InputStream is = btc.openInputStream();
		// port de donnée ouvert pour recevoir les données
		DataInputStream dis = new DataInputStream(is);
		
		// prepare une variable pour stocker les données
		int [] position = new int [2];
		//
		try {
			for (int x=0; x<position.length;x++) {
				// Lecture d'une suite de byte transformer en int par la fonction
				position[x]= (int)dis.readInt();
			}
			dis.close();
			// -----------------> Fermeture du port de données de reception mais pas total !
		} catch(Exception e) {
	         // if any I/O error occurs
	         e.printStackTrace();
	    } finally {
	         // releases any associated system files with this stream
	         if(dis!=null)
	            dis.close();
	    } 
		return position;
	}
	// ############################# Envoie de la carte #################################
//	public void sendCarte (int[][] carte) throws IOException {
//		// Trouver le moyen d'envoyer une array (la carte)
//		OutputStream os = btc.openOutputStream();
//		DataOutputStream dos = new DataOutputStream(os);
//		LCD.clear();
//		try {
//			LCD.drawString("\n\nEnvoi", 0, 0);
//			Delay.msDelay(1000);
//			LCD.clear();
//			for (int y=0; y<carte.length;y++) {
//				for (int x = 0; x<carte[0].length; x++) {
//					dos.write(carte[y][x]);
//				}
//			}
//			dos.flush(); // force l’envoi
//			LCD.drawString("\n\nEnvoyer", 0, 0);
//			Delay.msDelay(1000);
//			LCD.clear();
//			dos.close();
//			LCD.clear();
//		} catch(Exception e) {
//	         // if any I/O error occurs
//	         e.printStackTrace();
//	         exit();
//	    } finally {
//	         // releases any associated system files with this stream
//	         if(dos!=null)
//	            dos.close();
//	    }  
//	}
//	
//
//	public int[][] receveCarte() throws IOException {
//		/*
//		 * Retourne un tableau de int
//		 * 
//		 * */
//		InputStream is = btc.openInputStream();
//		DataInputStream dis = new DataInputStream(is);
//		int [][] carte = new int [7][5];
//		try {
//			for (int y=0; y<carte.length;y++) {
//				for (int x = 0; x<carte[0].length; x++) {
//					carte[y][x]= (int)dis.readInt();
//				}
//			}
//		} catch(Exception e) {
//	         // if any I/O error occurs
//	         e.printStackTrace();
//	         exit();
//	    } finally {
//	         // releases any associated system files with this stream
//	         if(dis!=null)
//	            dis.close();
//	    } 
//		return carte;
//	}
	
	public void exit() throws IOException {
		/*
		 * Ferme la connexion avec l'autre robot
		 * */
		try {
			btc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	}
	
