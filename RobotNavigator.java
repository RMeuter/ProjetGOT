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

	
// #### Algorithme du plus court chemin ####
	
	public LinkedList <Integer> dijkstra(){
		//Carte des poids
		// -1 = + l'infini
		int [][] poids = new int[][]{
								{-1, -1, -1, -1, -1},
								{-1, -1, -1, -1, -1},
								{-1, -1, -1, -1, -1},
								{-1, -1, -1, -1, -1},
								{-1, -1, -1, -1, -1},
								{-1, -1, -1, -1, -1},
								{-1, -1, -1, -1, -1}
							};
		// On initialise le point de départ comme le poids 0							};
		poids[position[1]][position[0]] = 0;
		
		//Carte des prédécesseurs
		int [][][] chemin = new int[][][]{
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}},
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}},
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}},
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}},
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}},
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}},
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}}
									};
		// On initialise le successeur du point de départ comme un point inexistant. Sinon le programme s'arrête au prédécesseur et ne va pas jusqu'au but
		chemin[position[1]][position[0]][0] = -1;
		chemin[position[1]][position[0]][1] = -1;
		
		//Carte des sommets déjà recherché par dijkstra
		boolean [][] dejaFait= new boolean[][]{
								{false, false, false, false, false},
								{false, false, false, false, false},
								{false, false, false, false, false},
								{false, false, false, false, false},
								{false, false, false, false, false},
								{false, false, false, false, false},
								{false, false, false, false, false}
									};
									
		//Tant que tous les sommets n'ont pas été parcouru, on recherche le poids minimum parmi les sommets qui n'ont pas été parcouru
		while (finiOuPas(dejaFait) == false){
			//point actuellement investigué, n'ayant pas déjà été investigué et ayant le plus petit poids
			int[] plusPetit = poidsMinimum(poids, dejaFait);
			//Lorsque le sommet a été fait, on le passe à true
			dejaFait[plusPetit[1]][plusPetit[0]] = true;
	
			// liste des voisins du point actuellement investigué
			ArrayList<int[]> voisins = lesVoisins(dejaFait, plusPetit);
			
			//Pour chaque voisin du point, on regarde si ce point n'est pas + l'infini ou si le poids est plus grand que le point actuel + le poids du voisin traité actuellement
			for (int[] x : voisins){
				if (poids[x[1]][x[0]] == -1 || poids[x[1]][x[0]] > poids[plusPetit[1]][plusPetit[0]]+carte.getPoids(x)){
					//si c'est le cas, on actualise le poids avec le poids du point actuel et le poids du voisin
					poids[x[1]][x[0]] = poids[plusPetit[1]][plusPetit[0]]+carte.getPoids(x);
					// on actualise les coordonées du point actuel comme prédécesseur de son voisin
					chemin [x[1]][x[0]][0]= plusPetit[0];
					chemin [x[1]][x[0]][1]= plusPetit[1];
				}
			}
		}
		
		// on crée la liste qui contiendra les directions
		LinkedList <Integer> cheminFinal = new LinkedList <Integer>();
		// Le prédécesseur correspond aux coordonnées qui précède l'arrivée au goal
		int[] sucesseur = goal;
		int[] predecesseur = chemin[goal[1]][goal[0]];
		
		//Comme l'algo commence par le but pour remonter , on ajoute chaque direction en premier, devant les autres
				//Ici on transforme les coordonnées en direction
				
				//on définit le sud en haut de la carte et le nord en bas de la carte
				// L'est est à droite de la carte et l'ouest à gauche
		while (!(predecesseur[0] == -1 && predecesseur[1] == -1)){
			if (sucesseur[0]-predecesseur[0] == 1){
				//Si quand on soustrait les coordonnées x du predecesseur au successeur, on obtient 1, alors on va à l'est
				//(pex (4,0) - (3,0) = 1, pour passer de la case (3,0) = predecesseur, à la case (4,0) = successeur, il faut aller à droite = est 
				cheminFinal.addFirst(EST);
			}else if (sucesseur[0]-predecesseur[0] == -1){
				//Si quand on soustrait les coordonnées x du predecesseur au successeur, on obtient -1, alors on va à l'ouest
				//(pex (3,0) - (4,0) = -1, pour passer de la case (4,0) = predecesseur, à la case (3,0) = successeur, il faut aller à gauche = ouest 
				cheminFinal.addFirst(OUEST);
			}else if (sucesseur[1]-predecesseur[1] == 1){
				//Si quand on soustrait les coordonnées y du predecesseur au successeur, on obtient 1, alors on va au sud
				//(pex (0,4) - (0,3) = 1, pour passer de la case (0,3) = predecesseur, à la case (0,4) = successeur, il faut aller à en haut = sud 
				cheminFinal.addFirst(SUD);
			}else if (sucesseur[1]-predecesseur[1] == -1){
				//Si quand on soustrait les coordonnées y du predecesseur au successeur, on obtient -1, alors on va au nord
				//(pex (0,3) - (0,4) = -1, pour passer de la case (0,4) = predecesseur, à la case (0,3) = successeur, il faut aller à en bas = nord 
				cheminFinal.addFirst(NORD);
			}
			//afin de passer à la prochaine coordonnées, on passe à la prochaine coordonnées
			sucesseur = predecesseur;			
			predecesseur = chemin[sucesseur[1]][sucesseur[0]];
		}
		return cheminFinal;
	}
	
	// A partir des coordonnées d'où nous sommes actuellement, on récupère tous les voisins possibles
	//n'ayant pas déjà été explorés
	private ArrayList<int[]> lesVoisins(boolean [][] dejaFait, int[] plusPetit){
		// on a les coordonnées du point actuellement ayant le plus petit poids
		int x = plusPetit[0];
		int y = plusPetit[1];
		ArrayList<int[]> voisins = new ArrayList<int[]>(4);
		// Pour chaque coordonnée, on verifie que la coordonnées fait partie de la carte et que le sommet n'a pas déjà été fait
		if (y+1 < dejaFait.length && dejaFait[y+1][x] == false){
			voisins.add(new int[] {x,y+1});
		}
		if (y-1 >= 0 && dejaFait[y-1][x] == false){
			voisins.add(new int[] {x,y-1});
		}
		if (x+1 < dejaFait[y].length && dejaFait[y][x+1] == false){
			voisins.add(new int[] {x+1,y});		
		}
		if (x-1 >= 0 && dejaFait[y][x-1] == false){
			voisins.add(new int[] {x-1,y});
		}
		return voisins;
	}
	
	
	// On regarde si on a exploré tous les cases de la carte
	private boolean finiOuPas(boolean[][] dejaFait){
		boolean bool = true;
		for (int y = 0; y < dejaFait.length; y++){
			for (int x = 0; x < dejaFait[y].length; x++){
			if (dejaFait[y][x] == false){
				bool = false;
				break;
				}
			}
		}
		return bool;
	}
	
	//Renvoi des coordonées qui n'ont pas déjà été investigué
	private int[] premiereNonFait(boolean [][] dejaFait){
		for (int y = 0; y < dejaFait.length; y++){
			for (int x = 0; x < dejaFait[y].length; x++){
				if (dejaFait[y][x] == false){
					return  new int[] {x, y};
				}
			}
		}
		throw new InternalError();
	}
	
	// Retourne la case ayant le plus petit poids parmi les cases non explorées
	private int[] poidsMinimum(int[][] tableauPoids, boolean [][] dejaFait){
		// on initialise avec une coordonées qu'on est sûr qu'elle n'a pas déjà été explorée
		int [] coordPetitPoids = premiereNonFait(dejaFait);
		int plusPetitPoids = tableauPoids[coordPetitPoids[1]][coordPetitPoids[0]];
		for (int y =coordPetitPoids[1]; y <tableauPoids.length; y++){
			for (int x = coordPetitPoids[0]; x < tableauPoids[x].length; x++){
				// si la coordonnée n'a pas déjà été faite, que le poids est différent de l'infini et qu'il est inférieur au plus petit poids
				if (tableauPoids[y][x] < plusPetitPoids && dejaFait[y][x] == false && tableauPoids[y][x] != -1){
					plusPetitPoids = tableauPoids[y][x];
					coordPetitPoids[0] = x;
					coordPetitPoids[1] = y;
				}
			}
		}
		return coordPetitPoids;
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
