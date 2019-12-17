package ProjetGOT;

import java.util.LinkedList;

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
	private boolean isSauvageon = false;
	private int biaisAngle = 0;  // Angle où le robot semble tourner correctement;
	private byte etape = 3; // Etape = Objectif 1,2 ou 4
	
	// #### Localisation et repere
	private byte position; // Coordonnées actuelles du robot [x, y]
	private byte goal; // But du robot [x, y]
	private short Cap; 	// Direction du robot vers lequel le robot regarde
	private Carte carte; 	// Recuperation de la carte
	private LinkedList <Short> chemin = new LinkedList <Short>();
	private Dijkstra fctDijkstra;
	private boolean waitNewCarte = false; // attend avant de recreer une carte
	
// #### Constructeur ####
	public RobotNavigator (int newBiaisAngle) {
		// Definition d'un chemin
		// Ordre important !!
		defineCamp();
		setDebut();
		fctDijkstra = new Dijkstra(isSauvageon);
		setGoal();
		this.biaisAngle = newBiaisAngle;
	}

	
// ##### Déplacements ######
	
	// #### Requêtes ####
	
	// Recherche de la rotation nécessaire pour aller vers la prochaine direction :
	// angleRotation = angle actuel - angle futur    
	public short versDirection(short newCap){ // d = SUD, OUEST,EST,NORD
		short angleRotation = (short) (Cap - newCap);
		Cap = newCap;
		while (angleRotation>=180) angleRotation -= 360;
		while (angleRotation<=-180) angleRotation += 360;
		short newBiais = (short) (angleRotation < 0 ? -biaisAngle: (angleRotation == 0 ? 0 : biaisAngle));
		newBiais = (short) (angleRotation == 180 || angleRotation == -180? newBiais*2: newBiais);
		return (short) (angleRotation + newBiais);
	}
	
	// Permet de savoir si le robot a passé la ligne noire
	public boolean verifiePasseLigneNoire(boolean ligne) {
			if (ligne) return getCalibrateColor().getCalibreColor() == "noir";
			else return getCalibrateColor().getCalibreColor() != "noir";
		}
	
	// #### Commandes ####
	
	//Tourne le robot selon la nouvelle direction et un biais de rotation
	// En tournant, le robot change de case, on change donc la position vers la nouvelle
	public void tourne(){ 
		if (!chemin.isEmpty()) {
			short newDirection = chemin.getFirst();
			short rot = versDirection(newDirection);
			short scalaire = (short) (rot/(90+biaisAngle));
			if (scalaire>0) rot = (short) (rot/scalaire);
			else if (scalaire<0) {
				scalaire = (short) -scalaire;
				rot = (short) (rot/scalaire);
			}
			while(scalaire != 0) { // On prend 110 pour le biais qui peut etre compris en 0 et 20
				if(rot>0) {
					pilot.travel(30);
				} else {
					pilot.travel(-30);
				}
				while (!verifiePasseLigneNoire(false)) {
					pilot.backward();
				}
				pilot.rotate(rot, false);
				if(scalaire>0) scalaire --;
				else if(scalaire<0) scalaire ++;
			}
			pilot.travel(80);
			replaceInCarte(newDirection);
//			if (etape==3) {
//				short temps = carte.getPoids(position);
//				sarreteNSeconde(temps);
//			}
			if (waitNewCarte) waitNewCarte=true;
		} else {
			System.out.println("c'est finit ! Appuyer sur le boutons pour un nouveau but");
			pilot.stop();
			Button.waitForAnyPress();
			if (etape<3) etape++;
			else System.out.println("Plus d'étape");
			setGoal();
		}
	}
		
	// Afin de représenter les poids de chaque case, le robot s'arrête en fonction de leurs poids.
	public void sarreteNSeconde(short temps){
		pilot.stop();
		Delay.msDelay(1000*temps);
	}
	
	private void replaceInCarte(short d) {
		byte xposition = (byte) (position % 5);
		byte yposition = (byte) (position / 5);
		switch (d){
			case SUD : 
				position = (byte) (xposition + (5*(yposition+1)));
				break;
			case NORD : 
				position = (byte) (xposition + (5*(yposition-1)));
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
		if (!chemin.isEmpty()) {
			chemin.remove(0);
			
		}
	}
	
	
	
// ###### Définition du point de départ et du but #####
	
	//#### Commandes ####
	
	//Initialise les coordonnées du point de départ dans la position actuelle du robot
	public void setDebut(){
		// true = sauvageon,  false = garde de nuit

		if (this.isSauvageon){
			position = 4;
			Cap = SUD;
		}else {
			position = 30;
			Cap = NORD;
		}
	}
	
	//Initialise les coordonnées du but et donne l'étape (objectif)
	//Le but change de l'objectif 1 (= étape 1) à l'objectif 4 (= étape 3)
			//true = sauvageon,	false = garde de nuit
	public void setGoal(){
		if (isSauvageon == true && etape == 1){
			goal = 0;
		}else if (isSauvageon == false && etape == 1) {
			goal = 28;

		} else if (isSauvageon == true && etape == 2) {
			goal = 4;

		} else if (isSauvageon == false && etape == 2) {
			goal = 30;
			
		} else if (isSauvageon == true && etape == 3) {
			goal = 30;

		} else if (isSauvageon == false && etape == 3) {
			goal = 4;
			
		}

		chemin = fctDijkstra.dijkstra(getPosition(), getGoal());
	}
	
	protected void getPlaceRobot() {
		byte xposition = (byte) (position % 5);
		byte yposition = (byte) (position / 5);
		byte robotPlace;
		byte base;
		switch (Cap){
			case SUD : 
				robotPlace= (byte) (xposition + (5*(yposition+1)));
				base = (byte) (xposition + (5*(yposition-1)));
				break;
			case NORD : 
				robotPlace= (byte) (xposition + (5*(yposition-1)));
				base = (byte) (xposition + (5*(yposition+1)));
				break;
			case EST : 
				robotPlace= (byte) (xposition+1 + 5*yposition);
				base = (byte) (xposition-1 + 5*yposition);
				break;
			case OUEST : 
				robotPlace= (byte) (xposition-1 + 5*yposition);
				base = (byte) (xposition+1 + 5*yposition);
				break;
			default : 
				throw new InternalError();
		}
		fctDijkstra.changePoidRobot(robotPlace);
		chemin = fctDijkstra.dijkstra(base, getGoal());
		fctDijkstra.raiseCarte();
		waitNewCarte = true;
	}
	
	
	
	//#### Requêtes ####
	
	//Verifie si le robot est arrivé à destination
	public boolean isArriveGoal() {
		return goal == position;
	}

	
	public void addOneMoreMission () {
		etape = 3;
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
	
	
	public LinkedList<Short> getChemin(){
		return chemin;
	}
	public boolean getWaitNewCarte() {
		return waitNewCarte;
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