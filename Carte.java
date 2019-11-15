package OBJECTIF1;

public class Carte {
	
	/**
	 * Les Couleurs de la carte :
	 *   -2 : Camps
     *   10 : Mur
     *    1 : Champs
     *   -1 : Ville
     *    0 : Inconnu
     *    5 : Marais
	 */
	private short [] CarteCouleur;
	public static final byte xCarte = 5;
	public static final byte yCarte = 7;
	public static final float tailleCase = 12;
	public static final float ligneCase = (float) 1.5;
	
	public Carte(boolean camp){
		if (camp == true){
			CarteCouleur = new short[]{-2, 10, 1, 1, -1,
									100, 10, 1, 1, 1,
									100, 10, 10, 1, 5,
									100, 100, 10, 1, 1,
									100, 100, 100, 5, 1,
									100, 100, 100, 100, 10,
									100, 100, 100, 100, 10};
		}else {
			CarteCouleur = new short[] {-2, 10, 100, 100, 100,
										1, 10, 100, 100, 100,
										1, 10, 10, 100, 100,
										1, 1, 10, 100, 100,
										1, 5, 5, 5, 100,
										1, 1, 1, -2, 10,
										-1, 1, 1, 1, 10};
		}
		
	}
	
	public Carte(short[] couleurs){
		CarteCouleur = couleurs;
	}

	public short getPoids(byte coordonnees){
        if (CarteCouleur[coordonnees] < 2){
            return 1;
        }else {
            return CarteCouleur[coordonnees];
        }
    }
	
	// #### Commande ####
	public void setCarteCouleur(short[] carteCouleur) {
		CarteCouleur = carteCouleur;
	}
}
