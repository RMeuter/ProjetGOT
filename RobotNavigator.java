package OBJECTIF1;

import java.util.ArrayList;
import java.util.LinkedList;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class RobotNavigator extends Robot {
	
// #### Attributs ####		
	//Directions possibles pour le robot
	public static final int SUD = 0; 
	public static final int EST = 90;
	public static final int OUEST = -90;
	public static final int NORD = 180;
	
	// #### Attributs du robot####
	private boolean isSauvageon = false;
	
	// Angle où le robot semble tourner correctement;
	private int biaisAngle = 0; 
	
	// Le scalaire du biais angle
	private int scalaireBiaisAngle;

	// Etape = Objectif 1,2 ou 4
	private int etape = 0;
	
	// Coordonnées actuelles du robot [x, y]
	private int [] position = new int[2]; 
	
	// But du robot [x, y]
	private int [] goal = new int [2];
	
	// Direction du robot vers lequel le robot regarde
	private int Cap; 
	
	// Angle de rotation nécessaire afin de changer la direction du robot
	private int angleRotation; 
			
	// Recuperation de la carte
	private Carte carte;
		
	
// #### Constructeur ####
	public RobotNavigator (int newBiaisAngle) {
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
	public int versDirection(int d){ // d = SUD, OUEST,EST,NORD
		angleRotation = Cap - d;
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
	public void tourne(int d){ 
		pilot.rotate(angleRotation+biaisAngle);
		switch (d){
		case SUD : 
			position[1] = position[1] - 1;
			break;
		case NORD : 
			position[1] = position[1] + 1;
			break;
		case EST : 
			position[0] = position[0] + 1;
			break;
		case OUEST : 
			position[0] = position[0] - 1;
			break;
		default : 
			throw new InternalError();
		}
	}
		
	
	// Le robot avance tout droit d'une case et actualise sa position
	public void avance(int d){ 
		pilot.travel(Carte.tailleCase+Carte.ligneCase);
		switch (d){
		case SUD : 
			position[1] = position[1] - 1;
			break;
		case NORD : 
			position[1] = position[1] + 1;
			break;
		case EST : 
			position[0] = position[0] + 1;
			break;
		case OUEST : 
			position[0] = position[0] - 1;
			break;
		default : 
			throw new InternalError();// normalement impossible
		}
	}
		
	// Afin de représenter les poids de chaque case, le robot s'arrête en fonction de leurs poids.
	public void sarreteNSeconde(){
		int [][] carteCouleur = carte.getCarteCouleur();
		int temps = carteCouleur[position[0]][position[1]];
		pilot.stop();
		if (temps < 2){
			Delay.msDelay(1000);
		}else if (temps == 2){
			Delay.msDelay(5000);
		}else {
			Delay.msDelay(10000);
		}
	}
	
	
	
// ###### Définition du point de départ et du but #####
	
	//#### Commandes ####
	
	//Initialise les coordonnées du point de départ dans la position actuelle du robot
	public void setDebut(){
		// true = sauvageon,  false = garde de nuit

		if (this.isSauvageon){
			position = new int [] {4, 0};
		}else {
			position = new int [] {0, 6};
		}
	}
	
	//Initialise les coordonnées du but et donne l'étape (objectif)
	//Le but change de l'objectif 1 (= étape 1) à l'objectif 4 (= étape 3)
			//true = sauvageon,	false = garde de nuit
	public void setGoal(){
		this.goal = new int[2];
		if (this.isSauvageon && etape == 1){
			goal = new int [] {0, 0};
		}else if (!this.isSauvageon && etape == 1) {
			goal = new int [] {3, 5};
		} else if (this.isSauvageon && etape == 3) {
			goal = new int [] {0, 6};
		} else {
			goal = new int [] {4, 0};
		}
	}
	
	//#### Requêtes ####
	
	//Verifie si le robot est arrivé à destination
	public boolean isArriveGoal() {
		return goal[0] == position[0] && goal[1] == position[1];
	}

	
	public void addOneMoreMission () {
		etape += 2 ;
	}
	// Donne l'étape (=objectif)
	public int getEtape(){
		return etape;
	}
	
//##### Position du robot ######
	
	//#### Commandes ####
	public void setPosition(int[] position) {
		this.position = position;
	}
	
	//#### Requêtes ####
	public int[] getPosition() {
		return position;
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
		}else{
			//Garde de nuit
			carte = new Carte(false);
			isSauvageon = false;
		}
		Delay.msDelay(300);
		LCD.clear();
	}
	
}
