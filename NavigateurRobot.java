package ProjetGOT;

import java.util.LinkedList;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class NavigateurRobot extends Robot {
	
// ATTRIBUTS //		
	//Directions possibles pour le robot
	public static final short SUD = 180; 
	public static final short EST = -90;
	public static final short OUEST = 90;
	public static final short NORD = 0;
	
	// Attributs du robot
	private boolean estSauvageon = false;
	private int biaisAngle = 0;  // Angle où le robot semble tourner correctement;
	private byte objectif = 1; // Etape = Objectif 1,2 ou 4
	
	// Localisation et repères
	private byte position; // Coordonnées actuelles du robot [x, y]
	private byte positionObjectif; // But du robot [x, y]
	private short cap; 	// Direction du robot vers lequel le robot regarde
	private Carte carte; 	// Recuperation de la carte
	private LinkedList <Short> chemin = new LinkedList <Short>();
	private Dijkstra fctDijkstra;
	private boolean attenteNouvelleCarte = false; // attend avant de recréer une carte
	
// CONSTRUCTEUR //
	public NavigateurRobot (int newBiaisAngle) {
		definitionCamp();
		setDebut();
		fctDijkstra = new Dijkstra(estSauvageon);
		setPositionObjectif();
		this.biaisAngle = newBiaisAngle;
	}

// REQUETES // 
	private short versDirection(short nouveauCap){ // d = SUD, OUEST,EST,NORD
		short angleRotation = (short) (cap - nouveauCap);
		cap = nouveauCap;
		while (angleRotation>=180) angleRotation -= 360;
		while (angleRotation<=-180) angleRotation += 360;
		short nouveauBiais = (short) (angleRotation < 0 ? -biaisAngle: (angleRotation == 0 ? 0 : biaisAngle));
		nouveauBiais = (short) (angleRotation == 180 || angleRotation == -180? nouveauBiais*2: nouveauBiais);
		return (short) (angleRotation + nouveauBiais);
	}
	
	protected boolean estPasserLigneNoire(boolean ligne) {
			if (ligne) return getCalibrateColor().getCalibreColor() == "noir";
			else return getCalibrateColor().getCalibreColor() != "noir";
		}

// COMMANDES // 

	protected void tourne(){ 
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
				while (!estPasserLigneNoire(false)) {
					pilot.backward();
				}
				pilot.rotate(rot, false);
				if(scalaire > 0) scalaire --;
				else if(scalaire < 0) scalaire ++;
			}
			pilot.travel(80);
			replaceInCarte(newDirection);
			if (objectif==3) {
				short temps = carte.getPoids(position);// Sortie pour Forcer a obtenir la position avec le delay
				sarreteNSeconde(temps);
			}
			if (attenteNouvelleCarte) attenteNouvelleCarte=true;
		} else {
			System.out.println("c'est finit ! Appuyer sur le boutons pour un nouveau but");
			pilot.stop();
			Button.waitForAnyPress();
			if (objectif<3) objectif++;
			else System.out.println("Plus d'Ã©tape");
			setPositionObjectif();
		}
	}
		
	private void sarreteNSeconde(short temps){
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
	
	private void setDebut(){
		if (this.estSauvageon){
			position = 4;
			cap = SUD;
		}else {
			position = 30;
			cap = NORD;
		}
	}

	private void setPositionObjectif(){
		if (estSauvageon == true && objectif == 1){
			positionObjectif = 0;
		}else if (estSauvageon == false && objectif == 1) {
			positionObjectif = 28;

		} else if (estSauvageon == true && objectif == 2) {
			positionObjectif = 4;

		} else if (estSauvageon == false && objectif == 2) {
			positionObjectif = 30;
			
		} else if (estSauvageon == true && objectif == 3) {
			positionObjectif = 30;

		} else if (estSauvageon == false && objectif == 3) {
			positionObjectif = 4;	
		}
		chemin = fctDijkstra.dijkstra(getPosition(), getPositionObjectif());
	}
	
	protected void getPlaceRobot() {
		byte xPosition = (byte) (position % 5);
		byte yPosition = (byte) (position / 5);
		byte positionRobotAdverse;
		byte positionApresRecule;
		switch (cap){
			case SUD : 
				positionRobotAdverse = (byte) (xPosition + (5*(yPosition+1)));
				positionApresRecule = (byte) (xPosition + (5*(yPosition-1)));
				break;
			case NORD : 
				positionRobotAdverse = (byte) (xPosition + (5*(yPosition-1)));
				positionApresRecule = (byte) (xPosition + (5*(yPosition+1)));
				break;
			case EST : 
				positionRobotAdverse = (byte) (xPosition+1 + 5*yPosition);
				positionApresRecule = (byte) (xPosition-1 + 5*yPosition);
				break;
			case OUEST : 
				positionRobotAdverse = (byte) (xPosition-1 + 5*yPosition);
				positionApresRecule = (byte) (xPosition+1 + 5*yPosition);
				break;
			default : 
				throw new InternalError();
		}
		fctDijkstra.creationCarteAvecPresenceRobotAdverse(positionRobotAdverse);
		chemin = fctDijkstra.dijkstra(positionApresRecule, getPositionObjectif());
		fctDijkstra.ecraseCarteUltrason();
		attenteNouvelleCarte = true;
	}
	

// Position du robot

// REQUETES //
	public byte getPosition() {
		return position;
	}	
	public byte getPositionObjectif(){
		return positionObjectif;
	}
	public boolean getAttenteNouvelleCarte() {
		return attenteNouvelleCarte;
	}

// COMMANDES // 	
	private void definitionCamp () {
		LCD.drawString("Choisis ton camp", 0, 0);
		LCD.drawString("H : Sauvageons", 0, 1);
		LCD.drawString("B : Garde de nuit", 0, 2);
		Button.waitForAnyPress();
		if (Button.UP.isDown()){
			carte = new Carte(true);
			estSauvageon = true;
		}else if (Button.DOWN.isDown()){
			carte = new Carte(false);
			estSauvageon = false;
		}
		Delay.msDelay(300);
		LCD.clear();
	}
	
}