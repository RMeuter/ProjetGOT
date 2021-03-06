package ProjetGOT;

import java.util.LinkedList;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class NavigateurRobot extends Robot {
	
/**
 *  ATTRIBUTS		
 */
	/**
	 * Directions possibles pour le robot.
	 */
	public static final short SUD = 180; 
	public static final short EST = -90;
	public static final short OUEST = 90;
	public static final short NORD = 0;
	
	/**
	 *  Attributs du robot.
	 */
	private boolean estSauvageon = false;
	private int biaisAngle = 0;  // Angle où le robot semble tourner correctement.
	private byte objectif = 1; // Etape = Objectif 1,2 ou 4.
	
	/**
	 *  Localisation et repères
	 */
	private byte position; // Coordonnées actuelles du robot [x, y].
	private byte positionObjectif; // But du robot [x, y].
	private short cap; 	// Direction du robot vers lequel le robot regarde.
	private Carte carte; 	// Récuperation de la carte.
	private LinkedList <Short> chemin = new LinkedList <Short>();
	private Dijkstra fctDijkstra;
	private boolean attenteNouvelleCarte = false; // attend avant de recréer une carte.
	
/**
 *  CONSTRUCTEUR
 * @param newBiaisAngle : un int qui est le biais d'ajustement du robot.
 */
	public NavigateurRobot (int newBiaisAngle) {
		definitionCamp();
		setDebut();
		fctDijkstra = new Dijkstra(estSauvageon);
		setPositionObjectif();
		this.biaisAngle = newBiaisAngle;
	}

/**
 *  REQUETES 
     * Calcul un angle permettant au robot de s'orienter vers la destination voulue.
     * @param nouveauCap: un short definissant le nouveau Cap.
     * @return un short qui est l'angle de rotation du robot actuel.
     */
	private short versDirection(short nouveauCap){ // d = SUD, OUEST,EST,NORD
		short angleRotation = (short) (cap - nouveauCap);
		cap = nouveauCap;
		while (angleRotation>=180) angleRotation -= 360;
		while (angleRotation<=-180) angleRotation += 360;
		short nouveauBiais = (short) (angleRotation < 0 ? -biaisAngle: (angleRotation == 0 ? 0 : biaisAngle));
		nouveauBiais = (short) (angleRotation == 180 || angleRotation == -180? nouveauBiais*2: nouveauBiais);
		return (short) (angleRotation + nouveauBiais);
	}
	
	 /**
     * Vérifie si le capteur couleur est sur une ligne noire.
     * (Cette fonction permet d'alléger l'écriture des autres fonctions).
     * @param ligne: un boolean definissant si le robot doit voir du noir ou l'inverse(toutes les couleurs sauf du noir).
     * @return un boolean qui signifie s'il y a du noir ou non.
     */
	protected boolean estPasserLigneNoire(boolean ligne) {
			if (ligne) return getCalibrateColor().getCalibrationCouleur() == "noir";
			else return getCalibrateColor().getCalibrationCouleur() != "noir";
		}

/**
 *  COMMANDES  
 */
	
	/**
	 * Fonction qui verifie en premier si la liste chemin 
	 * n'est pas vide si elle n'est pas : 
	 * 
		 * cet fonction d�coupe les angles de rotation du robot par 90 degres.
		 * Avant de tourner le robot avance de 3 cm 
		 * s'il tourne � gauche, sinon il recule de 3 cm
		 * puis il tourne de 90 degres plus ou moins son biais total 
		 * divis� par le nombre de rotation � 90 degres.
		 * 
		 * Cette fonction verifie egalement si le robot 
		 * doit s'arreter durant le temps que la couleur de la 
		 * case lui indique si son objectif est � 3.
		 * Enfin il verifie s'il est toujour en manoeuvre de redefinition
		 * de la carte pour contourner un obstacle. Si oui, il d�finit
		 * le boolean attenteNouvelleCarte � true.
		 * 
	 * Si la liste chemin est vide : 
	 * 
		 * le robot s'arret et attend qu'un bouton 
		 * soit presser pour passer � l'etape suivant 
		 * s'elle existe, et permettera la redefinition de l'objectif
		 * de position;
	 */
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
			while(scalaire != 0) { 
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
			repositionnementCarte(newDirection);
			if (objectif==3) {
				short temps = carte.getPoids(position);
				sarreteNSeconde(temps);
			}
			if (attenteNouvelleCarte) attenteNouvelleCarte=true;
		} else {
			System.out.println("c'est finit ! Appuyer sur le boutons pour un nouveau but");
			pilot.stop();
			Button.waitForAnyPress();
			if (objectif<3) objectif++;
			else System.out.println("Plus d'étape");
			setPositionObjectif();
		}
	}
		
	/**
	 * Le robot s'arrête pendant le temps specifié.
	 * @param temps : le poids récuperé correspondant au temps que le robot doit s'arrêter.
	 */
	private void sarreteNSeconde(short temps){
		pilot.stop();
		Delay.msDelay(1000*temps);
	}
	
	/**
	 * Redéfinit la position du robot sur la carte et supprime le premier élément de la liste chemin. 
	 * @param d : un short qui est le Cap actuel du robot.
	 */
	private void repositionnementCarte(short d) {
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
	
	/**
	 * Détermine la position de début du robot et la direction vers laquelle il regarde.
	 */
	private void setDebut(){
		if (this.estSauvageon){
			position = 4;
			cap = SUD;
		}else {
			position = 30;
			cap = NORD;
		}
	}

	/**
	 * Détermine, selon l'étape à laquelle on est, la position du robot.
	 */
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
	
	/**
	 * Détermine la position de l'obstacle lorsque le robot le détecte devant lui. 
	 * Il définit alors sa position et recule en conséquence.
	 * Avec ces variables, une liste chemin est recréer, permettant de contourner l'obstacle. 
	 * Le boolean attenteNouvelleCarte est définit à true afin que le robot puisse se 
	 * repositionner correctement sur la carte apres avoir reculer.
	 */
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
	

/**
 *  Position du robot
 */

/**
 *  REQUETES
 */
	
	/**
	 * @return la position du robot.
	 */
	public byte getPosition() {
		return position;
	}	
	
	/**
	 * @return la position d'arrivée de l'étape.
	 */
	public byte getPositionObjectif(){
		return positionObjectif;
	}
	
	/**
	 * @return le boolean d'attente de la redéfinition de la 
	 * carte lorsqu'il y a la présence d'un obstacle.
	 */
	public boolean getAttenteNouvelleCarte() {
		return attenteNouvelleCarte;
	}

/**
 *  COMMANDES  	
 */
	/**
	 * Définit le camp du robot (sauvageon ou garde de nuit), ainsi que sa carte.
	 */
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