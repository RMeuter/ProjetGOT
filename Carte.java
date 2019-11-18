package ProjetGOT;

public class Carte {
	
// #### Attributs ####
	private short[][] CarteCouleur; 
	
	public static final float tailleCase = 12;
	public static final float ligneCase = (float) 1.5;
	
	
// ##### Constructeur #####

	//Definition de la carte gr�ce au camp
	
	// La carte est cod� selon la valeur de ses couleurs :
	// -2: Camps, -1: Ville, 100: Inconnu, 1: Champs, 5: Marais, 10: Mur
	
	// le (0,0) est en haut � gauche
	
	// true = sauvageon, false = garde de nuit
	public Carte(boolean Camp){
		if (Camp == true){
			CarteCouleur = new short[][]{
			    {-2, 10, 1, 1, -1},
				{100, 10, 1, 1, 1},
				{100, 10, 10, 1, 5},
				{100, 100, 10, 1, 1},
				{100, 100, 100, 5, 1},
				{100, 100, 100, 100, 10},
				{100, 100, 100, 100, 10}
			};
		}else {
			CarteCouleur = new short[][] {
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

//#### M�thodes ####

	// #### Requ�tes ####
	public short[][] getCarteCouleur() {
		return CarteCouleur;
	}

	// Gr�ce aux cordonn�es donn�es, on peut obtenir le poids de la case
	public short getPoids(short x, short y){
		if (CarteCouleur[x][y] < 2){
			return 1;
		}else if (CarteCouleur[x][y] > 10) {
			return 0;
		}else {
			return CarteCouleur[x][y];
		}
	}
	
	// #### Commande ####
	public void setCarteCouleur(short [][] carteCouleur) {
		CarteCouleur = carteCouleur;
	}
	


}
