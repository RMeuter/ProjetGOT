package ProjetGOT;

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
	BTConnector bt = new BTConnector();
	NXTConnection btc;
	
	/*
	 * 
	 * // Question : Pourquoi on regarde le camp du robot ? Il y a un "envoyeur" et un "receveur", cela n'a pas de rapport avec le camp ?
	// Ne faut-il pas plutôt regarder si l'utilisateur a appuyé sur bouton gauche ou droit?
	 * 
	 * Les camps sont binaire donc au lieux definir s'il faut appuyer sur un bouton, on peut utiliser 
	 * le fait que un camps implique la reception d'un "raccordement" bluetooth l'autre la demande
	 * 
	 * Ca evite les erreurs lors des essais et ça permet une mise au point plus rapide du demarrage du robot
	 * */
	
	public Bluetooth (boolean camp) {
		if (camp){
			try {
				LCD.drawString("Attente du pheriphérique", 0, 0);
				btc = bt.waitForConnection(100000, NXTConnection.PACKET);
				if (btc != null) {
					LCD.clear();
					LCD.drawString(connected, 0, 0);
					Delay.msDelay(1000);
					LCD.clear();
				} else {
					LCD.clear();
					LCD.drawString("Pas de connexion",0,0);
					Button.RIGHT.waitForPressAndRelease();
					LCD.clear();
				}
			} catch (Exception e) {
				}
		} else {
			LCD.drawString("Recherche du pheriphérique", 0, 0);
			EV3 ev = LocalEV3.get();
			LCD.drawString("--"+ev.getName()+"--", 0, 1);
			Button.waitForAnyPress();
			LCD.clear();
			try {
				//droite = 00:16:53:43:4E:26
				//gauche = 00:16:53:43:8E:49
				btc = bt.connect("00:16:53:43:EB:88", NXTConnection.PACKET);//le premier paramètre est l'adresse du récepteur affiché sur l'écra de l'émetteur après association (pair) bluetooth
				LCD.clear();
				LCD.drawString(connected, 0, 0);
				Delay.msDelay(1000);
				LCD.clear();
			} catch (Exception e) {
			}
		}	
	}
	// ######################### Envoie de la position
	public void sendPosition (int[] position) throws IOException {
		/*
		 * Envoie des coordonnées codé sous forme 
		 * d'un tableau de deux elements soit [x, y]
		 * */
		OutputStream os = btc.openOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		LCD.clear();
		try {
			LCD.drawString("\n\nEnvoi", 0, 0);
			Delay.msDelay(1000);
			LCD.clear();
			for (int x=0; x<7;x++) {
				dos.write(position[x]);
			}
			dos.flush(); // force l’envoi
			LCD.drawString("\n\nEnvoyé", 0, 0);
			Delay.msDelay(1000);
			LCD.clear();
			dos.close();
			LCD.clear();
		} catch(Exception e) {
	         // if any I/O error occurs
	         e.printStackTrace();
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
		InputStream is = btc.openInputStream();
		DataInputStream dis = new DataInputStream(is);
		int [] position = new int [2];
		try {
			for (int x=0; x<7;x++) {
				position[x]= (int)dis.readInt();
			}
			dis.close();
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
	public void sendCarte (int[][] carte) throws IOException {
		// Trouver le moyen d'envoyer une array (la carte)
		OutputStream os = btc.openOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		LCD.clear();
		try {
			LCD.drawString("\n\nEnvoi", 0, 0);
			Delay.msDelay(1000);
			LCD.clear();
			for (int y=0; y<7;y++) {
				for (int x = 0; x<5; x++) {
					dos.write(carte[y][x]);
				}
			}
			dos.flush(); // force l’envoi
			LCD.drawString("\n\nEnvoyer", 0, 0);
			Delay.msDelay(1000);
			LCD.clear();
			dos.close();
			LCD.clear();
		} catch(Exception e) {
	         // if any I/O error occurs
	         e.printStackTrace();
	    } finally {
	         // releases any associated system files with this stream
	         if(dos!=null)
	            dos.close();
	    }  
	}
	

	public int[][] receveCarte() throws IOException {
		/*
		 * Retourne un tableau de int
		 * 
		 * */
		InputStream is = btc.openInputStream();
		DataInputStream dis = new DataInputStream(is);
		int [][] carte = new int [7][5];
		try {
			for (int y=0; y<7;y++) {
				for (int x = 0; x<5; x++) {
					carte[y][x]= (int)dis.readInt();
				}
			}
		} catch(Exception e) {
	         // if any I/O error occurs
	         e.printStackTrace();
	    } finally {
	         // releases any associated system files with this stream
	         if(dis!=null)
	            dis.close();
	    } 
		return carte;
	}
	
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
	
