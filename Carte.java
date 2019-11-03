package ProjetGOT;

public class Carte {

	private int [][] CarteCouleur; // -2: Camps, 10: Mur, 1: Champs, -1: Ville, 0: Inconnu, 5: Marais
	private static float tailleCase = 12;
	private static float ligneCase = (float) 1.5;
	private int positionDynamique; //entre -180 et 180 -> la rotation
	private int [] positionHistorique = new int[2]; //sur la carte avec coordonn�es [x, y]
	private int [] goal = new int [2]; // [x, y]
	private boolean isSauvageon; 
	
	
	
	// ################################ Definition de la carte et but ##############################

	public Carte(Boolean Camp){
		isSauvageon = Camp;
		setGoal(1);
		positionHistorique= getDebut();
		if (Camp == true){
			//Sauvageons
			CarteCouleur = new int[][]{
			    {-2, 10, 1, 1, -1},
				{0, 10, 1, 1, 1},
				{0, 10, 10, 1, 5},
				{0, 0, 10, 1, 1},
				{0, 0, 0, 5, 1},
				{0, 0, 0, 0, 10},
				{0, 0, 0, 0, 10}
			};
			positionDynamique = 0;
		}else {
			//Garde de nuit
			CarteCouleur = new int[][] {
			 	{-2, 10, 0, 0, 0},
				{1, 10, 0, 0, 0},
				{1, 10, 10, 0, 0},
				{1, 1, 10, 0, 0},
				{1, 5, 5, 5, 0},
				{1, 1, 1, -2, 10},
				{-1, 1, 1, 1, 10}
			};
			positionDynamique = 180;
		}
		positionHistorique= getDebut();
	}

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
		return goal[0] == positionHistorique[0] && goal[1] == positionHistorique[1];
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
			if (goal[0]-positionHistorique[0]<0 && positionHistorique[0]-1>=0) {
				newPosition = 80;
				positionHistorique[0]-=1;
			} else {
				newPosition = 240;
				positionHistorique[0]+=1;
			}
		} else {
			if (goal[1]-positionHistorique[1]<0 && positionHistorique[0]-1>=0) {
				newPosition = 160;
				positionHistorique[1]-=1;
			} else {
				newPosition = 0;
				positionHistorique[1]+=1;
			}
		}
		return newPosition;
	}
	
	public int getRotate() {
		int newPosition = findNewPositionDynamique();
		int rotate = newPosition - positionDynamique;
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

	public int [][] getCarteCouleur() {
		return CarteCouleur;
	}

	public void setCarteCouleur(int [][] carteCouleur) {
		CarteCouleur = carteCouleur;
	}

	//##################################### fonction quelconque ############################################
	
	public boolean estBloque(int [] position) {
		if (position [0] == 0 || position [1]==0 || position [0] > 5 || position[1] > 7 ) {	
			return true;
		}else {
			return false;
		}
		}
	
	

}
