package ProjetGOT;

import lejos.robotics.Color;

public class Carte {

	private String [][] CarteCouleur; //
	private static float tailleCase = 12;
	private static float ligneCase = (float) 1.5;
	private int positionDynamique; //entre -180 et 180 -> la rotation
	private int [] positionHistorique = new int[2]; //sur la carte avec coordonn�es [x, y]
	private int [] goal = new int [2]; // [x, y]
	private boolean isSauvageon = true; 
	
	
	
	// ################################ Definition de la carte et but ##############################

	public Carte(Boolean Camp){
		isSauvageon = Camp;
		setGoal(1);
		positionHistorique= getDebut();
		if (Camp == true){
			//Sauvageons
			CarteCouleur = new String[][]{
			    {"rouge", "bleu", "vert", "vert", "blanc"},
				{"rien", "bleu", "vert", "vert", "vert"},
				{"rien", "bleu", "bleu", "vert", "orange"},
				{"rien", "rien", "bleu", "vert", "vert"},
				{"rien", "rien", "rien", "orange", "vert"},
				{"rien", "rien", "rien", "rien", "bleu"},
				{"rien", "rien", "rien", "rien", "bleu"}
			};
			positionDynamique = 0;
		}else {
			//Garde de nuit
			CarteCouleur = new String[][] {
				 	{"Rouge", "bleu", "rien", "rien", "rien"},
					{"vert", "bleu", "rien", "rien", "rien"},
					{"vert", "bleu", "bleu", "rien", "rien"},
					{"vert", "vert", "bleu", "rien", "rien"},
					{"vert", "orange", "orange", "orange", "rien"},
					{"vert", "vert", "vert", "rouge", "bleu"},
					{"blanc", "vert", "vert", "vert", "bleu"}
				};
				positionDynamique = 180;
		}
		positionHistorique= getDebut();
	}
	
	/*
	 * Vaut mieux le voir avec les couleur si au bout de 12 cm d'avancement il n'y a pas de ligne noir donc on est sortie
	 * Pourquoi les couleur car le robot ne tourne jamais 180 ou 90 degres reelement donc dure d�tablir des direction strict
	 * mettre des limites pour les coordonn�es (si il est en 0,0 il n'a pas le droite de revenir en arri�re 
	 * ni aller � gauche.
	public boolean estBloque(int x, int y, Direction d) {
		if (x == 0 && d == Direction.Ouest || y == 0 && d == Direction.Nord || x == tailleX-1 && d == Direction.Est || y == tailleY -1 && d == Direction.Sud) {	
			return true;
		}else {
			return false;
		}
	*/
	




	// ################################ Getter& Setter :Trouver une position dans l'espace ##############################
	
	
	public void setPositionHistorique(int[] positionHistorique) {
		this.positionHistorique = positionHistorique;
	}
	
	public int[] getPositionHistorique() {
		return positionHistorique;
	}
	

	// ################################ Trouver et tester une position dans l'espace ##############################
	
	public int[] getDebut(){
		/*
		 * true = sauvageon
		 * false = garde de nuit
		 */
		int[] debut = new int[2];
		if (this.isSauvageon){
			debut = new int [] {4, 0};
		}else {
			debut = new int [] {0, 6};
		}
		return debut;
	}
	
	
	public boolean isArriveGoal() {
		if (goal == positionHistorique) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setGoal(int step){
		/*
		 * Le but change au fur et � mesure de la partie il faut donc le red�finir � chaque fois
		 * true = sauvageon
		 * false = garde de nuit
		 */
		this.goal = new int[2];
		if (this.isSauvageon && step == 1){
			goal = new int [] {0, 0};
		}else if (!this.isSauvageon && step == 1) {
			goal = new int [] {3, 5};
		} else if (this.isSauvageon && step == 2) {
			goal = new int [] {0, 6};
		} else {
			goal = new int [] {4, 0};
		}
	}
	
	public int findNewPositionDynamique() {
		/*
		 * Return une nouvelle position dynamique en degre
		 * Cette position est calculer entre la position historique et le but.
		 * On recupere le x s'il est different de 0 sinon le y.
		 * 
		 * Pourquoi juste le x xor y ? car on ne traverse pas en diagonal !
		 * */
		int newPosition;
		if (goal[0]-positionHistorique[0]!=0) {
			if (goal[0]-positionHistorique[0]>0) {
				newPosition = 270;
				positionHistorique[0]+=1;
			} else {
				newPosition = 90;
				positionHistorique[0]-=1;
			}
		} else {
			if (goal[1]-positionHistorique[1]>0) {
				newPosition = 0;
				positionHistorique[1]+=1;
			} else {
				newPosition = 180;
				positionHistorique[1]-=1;
			}
		}
		return newPosition;
	}
	
	public int getRotate() {
		int newPosition = findNewPositionDynamique();
		int rotate = -newPosition + positionDynamique;
		if (rotate==270 || rotate ==-270) {
			rotate = -rotate/3;
		} else if (rotate==360 || rotate ==-360){
			rotate=0;
		}
		positionDynamique = newPosition;
		return rotate;
	}
	
	// ##################################### Communication ############################################
	
	// Gestion de la reception et de l'envoie par bluetooth

	//##################################### Getter quelconque ############################################
	
	
	public static float getTailleCase() {
		return tailleCase;
	}

	public static float getLigneCase() {
		return ligneCase;
	}

	
	

}

/*
 * public enum TypeCase {
	Camp, Prairie, Mur, Marecage; }
 */
