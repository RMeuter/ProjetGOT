package ProjetGOT;

public class Carte {

	private int [][] CarteCouleur; // -2: Camps, 10: Mur, 1: Champs, -1: Ville, 0: Inconnu, 5: Marais
	private static float tailleCase = 12;
	private static float ligneCase = (float) 1.5;
	
	private int Cap; //entre -180 et 180 -> la rotation
	
	private int [] positionHistorique = new int[2]; //sur la carte avec coordonn�es [x, y]
	private int [] goal = new int [2]; // [x, y]
	
	private boolean isSauvageon; 
	private int biaisAngle = 0; // definit un angle pour lequel le robot semble tourner correctement;
	//---------------->le biais doit etre superieur à 0 !
	
	
	// ################################ Definition de la carte et but ##############################

	public Carte(Boolean Camp, int newBiaisAngle, int etape){
		// Definition de la carte au robot, 
		// du biais d'angle dont il fait face
		// du but qui est une coordonnée dont il doit se diriger
		// de la position à laquel il est affecter sur le plateau
		
		this.biaisAngle = newBiaisAngle;
		isSauvageon = Camp;
		setGoal(etape);
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
			Cap = 0;
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
			Cap = 180;
		}
		
	}

	// ################################ Trouver et tester une position dans l'espace ##############################
	
	public int[] getDebut(){
		/*
		 * true = sauvageon
		 * false = garde de nuit
		 */
		int[] debut;
		if (this.isSauvageon){
			debut = new int [] {4, 0};
		}else {
			debut = new int [] {0, 6};
		}
		return debut;
	}
	
	
	public boolean isArriveGoal() {
		// verification du robot qui est arriver au but donnée
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
	
	// ################################################# Definition des rotations #######################
	
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
				newPosition = 90;
				positionHistorique[0]-=1;
			} else {
				newPosition = 270;
				positionHistorique[0]+=1;
			}
		} else {
			if (goal[1]-positionHistorique[1]<0 && positionHistorique[0]-1>=0) {
				newPosition = 180;
				positionHistorique[1]-=1;
			} else {
				newPosition = 0;
				positionHistorique[1]+=1;
			}
		}
		return newPosition;
	}
	
	public int getRotate() {
		// Définit la rotation entre -180 et 180 degres
		// Redefinit le cap à l'angle calculer
		// Donne une rotation en fonction du biais angulaire du robot
		int newPosition = findNewPositionDynamique();
		int rotate = newPosition - Cap;
		Cap = newPosition;
		
		while (rotate>=180) rotate -= 360;
		while (rotate<=-180) rotate += 360;
		int newBiais = (rotate < 0 ? -biaisAngle: (rotate == 0 ? 0 : biaisAngle));
		return rotate + newBiais;
	}
	

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

	public void setPositionHistorique(int[] positionHistorique) {
		this.positionHistorique = positionHistorique;
	}
	
	public int[] getPositionHistorique() {
		return positionHistorique;
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
