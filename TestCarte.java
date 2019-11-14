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
	private short [][] CarteCouleur;
	public static final byte xCarte = 5;
	public static final byte yCarte = 7;
	
	public TestCarte(){
		CarteCouleur = new short[][]{
			{-2, 10, 1, 1, -1},
			{1, 10, 1, 1, 1},
			{1, 10, 10, 1, 5},
			{1, 1, 10, 1, 1},
			{1, 5, 5, 5, 1},
			{1, 1, 1, -2, 10},
			{-1, 1, 1, 1, 10}
		};
	}
	
	public TestCarte(short[][] couleurs){
		CarteCouleur = couleurs;
	}

	public short getPoids(byte coordonnees){
		byte xCoordonnee =(byte) (coordonnees %5);
		byte yCoordonnee =(byte) (coordonnees /5);
        if (CarteCouleur[yCoordonnee][xCoordonnee] < 2){
            return 1;
        }else {
            return CarteCouleur[yCoordonnee][xCoordonnee];
        }
    }
}
