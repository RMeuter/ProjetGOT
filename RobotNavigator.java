package ProjetGOT;

import java.util.ArrayList;
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
	private int etape = 0; // Etape = Objectif 1,2 ou 4
	
	// #### Localisation et repere
	private int [] position = new int [2]; // Coordonnées actuelles du robot [x, y]
	private short [] goal = new short [2]; // But du robot [x, y]
	private short Cap; 	// Direction du robot vers lequel le robot regarde
	private Carte carte; 	// Recuperation de la carte
	private LinkedList <Short> chemin = new LinkedList <Short>();
		
	
// #### Constructeur ####
	public RobotNavigator (int newBiaisAngle) {
		// Definition d'un chemin
		chemin.add(EST);chemin.add(NORD);chemin.add(NORD);chemin.add(NORD);chemin.add(NORD);chemin.add(NORD);
		chemin.add(EST);chemin.add(EST);chemin.add(EST);chemin.add(NORD);chemin.add(OUEST);chemin.add(OUEST);
		chemin.add(OUEST);chemin.add(SUD);
		defineCamp();
		setDebut();
		setGoal();
		this.biaisAngle = newBiaisAngle;
	}

	
// ##### Déplacements ######
	
	// #### Requêtes ####
	
	// Recherche de la rotation nécessaire pour aller vers la prochaine direction :
	// angleRotation = angle actuel - angle futur    
	public short versDirection(short newCap){ // d = SUD, OUEST,EST,NORD
		short angleRotation = (short) (newCap - Cap);
		Cap = newCap;
		System.out.println("Cap :" + Cap);
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
			System.out.println("ND :"+newDirection);
			System.out.println("rot : "+rot);
			short scalaire = (short) (rot/(90+biaisAngle));
			while(scalaire > 0) { // On prend 110 pour le biais qui peut etre compris en 0 et 20
				pilot.travel(20);
				pilot.rotate(rot/scalaire);
				if(rot>0) {
					while (!verifiePasseLigneNoire(false)) {
						System.out.print("arriere");
						pilot.backward();
					}
				} else {
					while (!verifiePasseLigneNoire(false)) {
						System.out.print("avant");
						pilot.forward();
					}
				}
				scalaire --;
			}
			pilot.travel(30);
			replaceInCarte(newDirection);
		} else {
			chemin.add(EST);
			System.out.println("c'est finit ! Appuyer sur le boutons pour un nouveau but");
			Button.waitForAnyEvent();
		}
	}
		
	// Afin de représenter les poids de chaque case, le robot s'arrête en fonction de leurs poids.
	public void sarreteNSeconde(){
		short temps = carte.getPoids((short)1, (short) 2);
		System.out.println("temps :"+temps);
		System.out.println("Position :"+position[1]);
		System.out.println("Position :"+position[0]);
		pilot.stop();
		if (temps <= 1) Delay.msDelay(1000);
		else if (temps == 100) {} 
		else Delay.msDelay(temps*1000);
	}
	
	
	
// ###### Définition du point de départ et du but #####
	
	//#### Commandes ####
	
	//Initialise les coordonnées du point de départ dans la position actuelle du robot
	public void setDebut(){
		// true = sauvageon,  false = garde de nuit

		if (this.isSauvageon) position = new int [] {4, 0};
		else position = new int [] {0, 6};
	}
	
	//Initialise les coordonnées du but et donne l'étape (objectif)
	//Le but change de l'objectif 1 (= étape 1) à l'objectif 4 (= étape 3)
			//true = sauvageon,	false = garde de nuit
	public void setGoal(){
		this.goal = new short[2];
		if (this.isSauvageon && etape == 1) goal = new short [] {0, 0};
		else if (!this.isSauvageon && etape == 1) goal = new short [] {3, 5};
		else if (this.isSauvageon && etape == 3) goal = new short [] {0, 6};
		else goal = new short [] {4, 0};
	}
	
	//#### Requêtes ####
	
	//Verifie si le robot est arrivé à destination
	public boolean isArriveGoal() {
		return chemin.isEmpty();
	}

	
	public void addOneMoreMission () {
		etape += 2 ;
	}
	// Donne l'étape (=objectif)
	public int getEtape(){
		return etape;
	}
	
//##### Position du robot ######
	
	//#### Requêtes ####
	public int[] getPosition() {
		return position;
	}
	
	private void replaceInCarte(int d) {
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
		if (!chemin.isEmpty()) {
			chemin.remove(0);
			System.out.println("fin du chemin :"+chemin.isEmpty());
		}
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
