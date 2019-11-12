package OBJECTIF1;

public class TestCarte {
	
	/**
	 * Les Couleurs de la carte :
	 *   -2 : Camps
     *   10 : Mur
     *    1 : Champs
     *   -1 : Ville
     *    0 : Inconnu
     *    5 : Marais
	 */
	private int [][] CarteCouleur;
	
	public TestCarte(){
		CarteCouleur = new int[][]{
			{-2, 10, 1, 1, -1},
			{1, 10, 1, 1, 1},
			{1, 10, 10, 1, 5},
			{1, 1, 10, 1, 1},
			{1, 5, 5, 5, 1},
			{1, 1, 1, -2, 10},
			{-1, 1, 1, 1, 10}
		};
	}
	
	public TestCarte(int[][] couleurs){
		CarteCouleur = couleurs;
	}

	public int getPoids(int[] coordonnees){
        if (CarteCouleur[coordonnees[1]][coordonnees[0]] < 2){
            return 1;
        }else {
            return CarteCouleur[coordonnees[1]][coordonnees[0]];
        }
    }
}
