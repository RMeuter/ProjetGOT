package ProjetGOT;

import lejos.robotics.Color;

public class Carte {

	private int [][] CarteCouleur; //
	private static float tailleCase = 12;
	private static float ligneCase = (float) 1.5;
	private int positionDynamique; //entre -1 et 1 -> la rotation
	private int [] positionHistorique = new int[2]; //sur la carte avec coordonn�es
	private int [] goal = new int [2];
	private boolean isSauvageon = true; 
	
	
	
	// ################################ Definition de la carte et but ##############################

	public Carte(Boolean Camp){
		this.isSauvageon = Camp;
		setGoal();
		if (Camp == true){
			//Sauvageons
			this.CarteCouleur = new int[][]{
			    {Color.RED, Color.BLUE, Color.GREEN, Color.GREEN, Color.WHITE},
				{Color.GRAY, Color.BLUE, Color.GREEN, Color.GREEN, Color.GREEN},
				{Color.GRAY, Color.BLUE, Color.BLUE, Color.GREEN, Color.ORANGE},
				{Color.GRAY, Color.GRAY, Color.BLUE, Color.GREEN, Color.GREEN},
				{Color.GRAY, Color.GRAY, Color.GRAY, Color.ORANGE, Color.GREEN},
				{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.BLUE},
				{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.BLUE}
			};
			this.positionDynamique = 0;
		}else {
			//Garde de nuit
			this.CarteCouleur = new int[][] {
				 	{Color.RED, Color.BLUE, Color.GRAY, Color.GRAY, Color.GRAY},
					{Color.GREEN, Color.BLUE, Color.GRAY, Color.GRAY, Color.GRAY},
					{Color.GREEN, Color.BLUE, Color.BLUE, Color.GRAY, Color.GRAY},
					{Color.GREEN, Color.GREEN, Color.BLUE, Color.GRAY, Color.GRAY},
					{Color.GREEN, Color.ORANGE, Color.ORANGE, Color.ORANGE, Color.GRAY},
					{Color.GREEN, Color.GREEN, Color.GREEN, Color.RED, Color.BLUE},
					{Color.WHITE, Color.GREEN, Color.GREEN, Color.GREEN, Color.BLUE}
				};
				this.positionDynamique = 180;
		}
		
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
	
	public void setGoal(){
		/*
		 * Le but change au fur et � mesure de la partie il faut donc le red�finir � chaque fois
		 * true = sauvageon
		 * false = garde de nuit
		 */
		this.goal = new int[2];
		if (this.isSauvageon){
			goal[0] = 0;
			goal[1] = 0;
		}else {
			goal[0] = 5;
			goal[1] = 3;
		}
	}
	
	public int[] getDebut(){
		/*
		 * true = sauvageon
		 * false = garde de nuit
		 */
		int[] debut = new int[2];
		if (this.isSauvageon){
			debut[0] = 0;
			debut[1] = 4;
		}else {
			debut[0] = 6;
			debut[1] = 0;
		}
		return debut;
	}
	
	// ################################ Trouver une position dans l'espace ##############################
	
	public int getPositionDynamique() {
		return positionDynamique;
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
		if (this.goal[0]-this.positionHistorique[0]!=0) {
			if (this.goal[0]-this.positionHistorique[0]>0) {
				newPosition = 270;
				this.positionHistorique[0]+=1;
			} else {
				newPosition = 90;
				this.positionHistorique[0]-=1;
			}
		} else {
			if (this.goal[1]-this.positionHistorique[1]>0) {
				newPosition = 0;
				this.positionHistorique[1]+=1;
			} else {
				newPosition = 180;
				this.positionHistorique[1]-=1;
			}
		}
		return newPosition;
	}
	
	
	public void setPositionDynamique(int positionDynamique) {
		this.positionDynamique = positionDynamique;
	}

	public int[] getPositionHistorique() {
		return positionHistorique;
	}

	public void setPositionHistorique(int[] positionHistorique) {
		this.positionHistorique = positionHistorique;
	}

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
