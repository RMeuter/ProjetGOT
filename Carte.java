package ProjetGOT;

//DESCRIPTION //
	// Déclaration des caractéristiques et méthodes de la carte

public class Carte {
	
	/**
	 * Les cases de la carte :
	 *   -2 : Camps
     *   10 : Mur
     *    1 : Champs
     *   -1 : Ville
     *    0 : Inconnu
     *    5 : Marais
	 */
	
//	ATTRIBUTS //
	private short [] carteCouleur;
	public static final byte xMaxCarte = 5;
	public static final byte yMaxCarte = 7;
	
// CONSTRUCTEURS //
	//Initialisation des valeurs de la carte.
	public Carte(boolean camp){
		carteCouleur = new short[]{
				-2, 10, 1, 1, -1,
				1, 10, 1, 1, 1,
				1, 10, 10, 1, 5,
				1, 1, 10, 1, 1,
				1, 5, 5, 5, 1,
				1, 1, 1, -2, 10,
				-1, 1, 1, 1, 10
				};		
	}
	
	// Initialisation de la carte des couleurs.
	public Carte(short[] couleurs){
		carteCouleur = couleurs;
	}

// REQUETES //
	// Retourne le poids de la case à la coordonée donnée.
	// Certaines cases ont les mêmes poids, même si elles ne sont pas du même type.
	// {camps, champs, ville, inconnu} = 1, marais = 5, mur = 10
	public short getPoids(byte coordonnees){
        if (carteCouleur[coordonnees] < 2){
            return 1;
        }else {
            return carteCouleur[coordonnees];
        }
    }
	
	
// COMMANDES //
	// Attribue une carte des couleurs.
	public void setCarteCouleur(short[] carteCouleur) {
		this.carteCouleur = carteCouleur;
	}
	
	// 
	protected void redefinitionCartePourUltrason(short positionRobotAdverse) {
		byte xPosition = (byte) (positionRobotAdverse % 5);
		byte yPosition = (byte) (positionRobotAdverse / 5);
		carteCouleur[positionRobotAdverse]=15;
		if(yPosition < yMaxCarte) carteCouleur[xPosition + (5*(yPosition+1))]=15;
		if(yPosition > 0) carteCouleur[xPosition + (5*(yPosition-1))]=15;
		if(xPosition < xMaxCarte) carteCouleur[xPosition+1 + 5*yPosition]=15;
		if(xPosition > 0) carteCouleur[xPosition-1 + 5*yPosition]=15;
	}
}
