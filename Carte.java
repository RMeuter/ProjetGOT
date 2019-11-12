package OBJECTIF1;

public class Carte {
	
// #### Attributs ####
	private int [][] CarteCouleur; 
	
	public static final float tailleCase = 12;
	public static final float ligneCase = (float) 1.5;
	
	
// ##### Constructeur #####

	//Definition de la carte grâce au camp
	
	// La carte est codé selon la valeur de ses couleurs :
	// -2: Camps, -1: Ville, 100: Inconnu, 1: Champs, 5: Marais, 10: Mur
	
	// le (0,0) est en haut à gauche
	
	// true = sauvageon, false = garde de nuit
	public Carte(boolean Camp){
		if (Camp == true){
			CarteCouleur = new int[][]{
			    {-2, 10, 1, 1, -1},
				{100, 10, 1, 1, 1},
				{100, 10, 10, 1, 5},
				{100, 100, 10, 1, 1},
				{100, 100, 100, 5, 1},
				{100, 100, 100, 100, 10},
				{100, 100, 100, 100, 10}
			};
		}else {
			CarteCouleur = new int[][] {
			 	{-2, 10, 100, 100, 100},
				{1, 10, 100, 100, 100},
				{1, 10, 10, 100, 100},
				{1, 1, 10, 100, 100},
				{1, 5, 5, 5, 100},
				{1, 1, 1, -2, 10},
				{-1, 1, 1, 1, 10}
			};
		}
		
	}

//#### Méthodes ####

	// #### Requêtes ####
	public int [][] getCarteCouleur() {
		return CarteCouleur;
	}

	// Grâce aux cordonnées données, on peut obtenir le poids de la case
	public int getPoids(int[] coordonnees){
		if (CarteCouleur[coordonnees[1]][coordonnees[0]] < 2){
			return 1;
		}else {
			return CarteCouleur[coordonnees[1]][coordonnees[0]];
		}
	}
	
	// #### Commande ####
	public void setCarteCouleur(int [][] carteCouleur) {
		CarteCouleur = carteCouleur;
	}
	


}
