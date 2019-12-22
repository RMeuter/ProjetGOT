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
	
	private short [] carteCouleur;
	public static final byte xMaxCarte = 5;
	public static final byte yMaxCarte = 7;
	
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
	
	public Carte(short[] couleurs){
		carteCouleur = couleurs;
	}

	public short getPoids(byte coordonnees){
        if (carteCouleur[coordonnees] < 2){
            return 1;
        }else {
            return carteCouleur[coordonnees];
        }
    }
	
	public void setCarteCouleur(short[] carteCouleur) {
		this.carteCouleur = carteCouleur;
	}
	
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
