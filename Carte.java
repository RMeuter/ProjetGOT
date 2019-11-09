package ProjetGOT;

public class Carte {

	private int [][] CarteCouleur; // -2: Camps, 10: Mur, 1: Champs, -1: Ville, 0: Inconnu, 5: Marais
	private static float tailleCase = 12;
	private static float ligneCase = (float) 1.5;
	// Kind of carte
	private boolean isSauvageon; 
	
	
	//---------------->le biais doit etre superieur à 0 !
	
	
	// ################################ Definition de la carte et but ##############################

	public Carte(boolean Camp){
		// Definition de la carte au robot, 
		// du biais d'angle dont il fait face
		// du but qui est une coordonnée dont il doit se diriger
		// de la position à laquel il est affecter sur le plateau
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
		}
		
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


	
	

}
