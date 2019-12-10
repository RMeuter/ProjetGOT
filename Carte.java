package ProjetGOT;
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
		CarteCouleur = new short[]{-2, 10, 1, 1, -1,
				1, 10, 1, 1, 1,
				1, 10, 10, 1, 5,
				1, 1, 10, 1, 1,
				1, 5, 5, 5, 1,
				1, 1, 1, -2, 10,
				-1, 1, 1, 1, 10};
		/*
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
		*/
		
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
	
	protected void NewPoids(short robot) {
		/*Prendre les bords en comptes
		 * 
		 * */
		byte xposition = (byte) (robot % 5);
		byte yposition = (byte) (robot / 5);
		CarteCouleur[robot]=15;
		CarteCouleur[xposition + (5*(yposition+1))]=15;
		CarteCouleur[xposition + (5*(yposition-1))]=15;
		CarteCouleur[xposition+1 + 5*yposition]=15;
		CarteCouleur[xposition-1 + 5*yposition]=15;
	}

}
