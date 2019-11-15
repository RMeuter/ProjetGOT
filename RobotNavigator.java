package OBJECTIF1;

import OBJECTIF1.Robot;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class RobotNavigator extends Robot {
	
// #### Attributs ####		
	//Directions possibles pour le robot
	public static final short SUD = 180; 
	public static final short EST = -90;
	public static final short OUEST = 90;
	public static final short NORD = 0;
	
	// #### Attributs du robot####
	private boolean isSauvageon;
	
	// Angle où le robot semble tourner correctement;
	private short biaisAngle = 0; 
	
	// Le scalaire du biais angle
	private int scalaireBiaisAngle;

	// Etape = Objectif 1,2 ou 4
	private byte etape = 0;
	
	// Coordonnées actuelles du robot [x, y]
	private byte position; 
	
	// But du robot [x, y]
	private byte goal;
	
	// Direction du robot vers lequel le robot regarde
	private short Cap; 
	
	// Angle de rotation nécessaire afin de changer la direction du robot
	private short angleRotation; 
			
	// Recuperation de la carte
	private Carte carte;
		
	
// #### Constructeur ####
	public RobotNavigator (short newBiaisAngle) {
		scalaireBiaisAngle =1/4;
		defineCamp();
		setDebut();
		setGoal();
		this.biaisAngle = newBiaisAngle;
	}

	
// ##### Déplacements ######
	
	// #### Requêtes ####
	
	// Recherche de la rotation nécessaire pour aller vers la prochaine direction :
	// angleRotation = angle actuel - angle futur    
	public short versDirection(short d){ // d = SUD, OUEST,EST,NORD
		angleRotation = (short) (Cap - d);
		return angleRotation;
	}
	
	// Permet de savoir si le robot a passé la ligne noire
	public boolean verifiePasseLigneNoire(boolean ligne) {
			if (ligne) return getCalibrateColor().getCalibreColor()=="noir";
			else return getCalibrateColor().getCalibreColor()!="noir";
		}
	
	// #### Commandes ####
	
	//Tourne le robot selon la nouvelle direction et un biais de rotation
	// En tournant, le robot change de case, on change donc la position vers la nouvelle
	public void tourne(short d){ 
		pilot.rotate(angleRotation+biaisAngle);
		Cap = d;
		byte xposition = (byte) (position % 5);
		byte yposition = (byte) (position / 5);
		switch (d){
		case SUD : 
			position = (byte) (xposition + (5*yposition-1));
			break;
		case NORD : 
			position = (byte) (xposition + (5*yposition+1));
			break;
		case EST : 
			position = (byte) (xposition+1 + 5*yposition);
			break;
		case OUEST : 
			position = (byte) (xposition-1 + 5*yposition);
			break;
		default : 
			throw new InternalError();
		}
	}
		
	
	// Le robot avance tout droit d'une case et actualise sa position
	public void avance(short d){ 
		pilot.travel(Carte.tailleCase*10+Carte.ligneCase*10);
		byte xposition = (byte) (position % 5);
		byte yposition = (byte) (position / 5);
		switch (d){
		case SUD : 
			position = (byte) (xposition + (5*yposition-1));
			break;
		case NORD : 
			position = (byte) (xposition + (5*yposition+1));
			break;
		case EST : 
			position = (byte) (xposition+1 + 5*yposition);
			break;
		case OUEST : 
			position = (byte) (xposition-1 + 5*yposition);
			break;
		default : 
			throw new InternalError();// normalement impossible
		}
	}
		
	// Afin de représenter les poids de chaque case, le robot s'arrête en fonction de leurs poids.
	/*
	public void sarreteNSeconde(){
		short [] carteCouleur = carte.getCarteCouleur();
		int temps = carteCouleur[position];
		pilot.stop();
		if (temps < 2){
			Delay.msDelay(1000);
		}else if (temps == 2){
			Delay.msDelay(5000);
		}else {
			Delay.msDelay(10000);
		}
	}
	*/
	
	
// ###### Définition du point de départ et du but #####
	
	//#### Commandes ####
	
	//Initialise les coordonnées du point de départ dans la position actuelle du robot
	public void setDebut(){
		// true = sauvageon,  false = garde de nuit

		if (this.isSauvageon){
			position = 4;
		}else {
			position = 30;
		}
	}
	
	//Initialise les coordonnées du but et donne l'étape (objectif)
	//Le but change de l'objectif 1 (= étape 1) à l'objectif 4 (= étape 3)
			//true = sauvageon,	false = garde de nuit
	public void setGoal(){
		if (isSauvageon == true && etape == 1){
			goal = 0;
		}else if (isSauvageon ==false && etape == 1) {
			goal = 28;
		} else if (isSauvageon == true && etape == 3) {
			goal = 30;
		} else if (isSauvageon == false && etape == 3) {
			goal = 4;
		}
	}
	
	//#### Requêtes ####
	
	//Verifie si le robot est arrivé à destination
	public boolean isArriveGoal() {
		return goal == position;
	}

	
	public void addOneMoreMission () {
		etape += 2 ;
	}
	// Donne l'étape (=objectif)
	public byte getEtape(){
		return etape;
	}
	
//##### Position du robot ######
	
	//#### Commandes ####
	public void setPosition(byte position) {
		this.position = position;
	}
	
	//#### Requêtes ####
	public byte getPosition() {
		return position;
	}
	
	public byte getGoal(){
		return goal;
	}
	
// ###### Definition des camps
	
	//L'utilisateur doit choisir le camp du robot :
	// sauvageon ou garde de nuit
	public void defineCamp () {
		LCD.drawString("Choisis ton camp", 0, 0);
		LCD.drawString("H : Sauvageons", 0, 1);
		LCD.drawString("B : Garde de nuit", 0, 2);
		Button.waitForAnyPress();
		if (Button.UP.isDown()){
			//Sauvageons
			carte = new Carte(true);
			isSauvageon = true;
		}else if (Button.DOWN.isDown()){
			//Garde de nuit
			carte = new Carte(false);
			isSauvageon = false;
		}
		Delay.msDelay(300);
		LCD.clear();
	}
	
}
