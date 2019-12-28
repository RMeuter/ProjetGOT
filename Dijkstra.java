package ProjetGOT;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * DESCRIPTION 
 * D�finition de l'algorithme de Dijkstra, afin de trouver le plus court chemin d'un point A � un point B.
 */
	
public class Dijkstra {
	
/**
 * 	ATTRIBUTS	
 */
	public static final short SUD = 180; 
	public static final short EST = -90;
	public static final short OUEST = 90;
	public static final short NORD = 0;

	private boolean isSauvageons;
	private Carte carte;

/**
 *  CONSTRUCTEURS 	
 * @param typeCarte : bool�en pour conna�tre le camp du robot (sauvageon ou garde de nuit).
 */
	public Dijkstra(boolean typeCarte){
		isSauvageons = typeCarte;
		carte = new Carte(isSauvageons);
	}

/**
 *  REQUETES	
 */
	/**
	 *  Algorithme de Dijkstra. C'est la fonction principale.
	 * @param position : la position actuelle du robot.
	 * @param goal : le but du robot.
	 * @return le chemin sous forme d'une liste de directions
	 * On d�finit -1 comme l'infini.
	 *  0 = false, 1 = true.
	 *  On d�finit le sud en haut de la carte et le nord en bas de la carte.
	 * L'est est � droite de la carte et l'ouest � gauche.
	 */ 
	public LinkedList <Short> dijkstra(byte position, byte goal){
		
		short [] poids = initTableauPoids ();
		poids[position] = 0;
		
		byte [] chemin = new byte[35];
		chemin[position] = -1;
		
		byte [] dejaFait= new byte[35];
									
		byte premierNonFait = premierNonFait(dejaFait);
		while (premierNonFait != -1){

			byte plusPetit = poidsMinimum(poids, dejaFait, premierNonFait);
						
			dejaFait[plusPetit] = 1;
	
			ArrayList<Byte> voisins = lesVoisins(dejaFait, plusPetit);
			
			for (byte le_voisin : voisins){
				if (poids[le_voisin] < 0 || poids[le_voisin] > poids[plusPetit]+carte.getPoids(le_voisin)){
					poids[le_voisin] = (short) (poids[plusPetit]+carte.getPoids(le_voisin));
					chemin [le_voisin]= plusPetit;
				}
			}
			premierNonFait = premierNonFait(dejaFait);
		}
		
		LinkedList <Short> cheminFinal = new LinkedList <Short>();
		byte successeur = goal;
		byte predecesseur = chemin[goal];       
		
		while (!(predecesseur == -1)){
			byte xSuc = (byte) (successeur % 5);
			byte ySuc = (byte) (successeur / 5);
			byte xPr = (byte) (predecesseur % 5);
			byte yPr = (byte) (predecesseur / 5);
			if (xSuc-xPr == 1){
				cheminFinal.addFirst(EST);
			}else if (xSuc-xPr == -1){
				cheminFinal.addFirst(OUEST);
			}else if (ySuc-yPr == 1){
				cheminFinal.addFirst(SUD);
			}else if (ySuc-yPr == -1){
				cheminFinal.addFirst(NORD);
			}
			successeur = predecesseur;			
			predecesseur = chemin[successeur];
		}
		return cheminFinal;
	}
	
	/**
	 *  Initialisation du tableau des poids, avec tous les poids � -1 (pas faits).
	 * @return le tableau des poids.
	 */
	private short [] initTableauPoids (){
		short [] TableauPoids = new short [35];
		for (int poids = 0; poids <TableauPoids.length; poids++){
			TableauPoids[poids] = -1;
		}
		return TableauPoids;
	}
	
	/**
	 *  A partir des coordonn�es d'o� nous sommes actuellement, on r�cup�re tous les voisins possibles
	 *  n'ayant pas d�j� �t� explor�s.
	 * @param dejaFait : liste des points qui ont d�j� �t� investigu�.
	 * @param plusPetit : point ayant le plus petit poids.
	 * @return les voisins du plus petit poids actuellement investigu�.
	 */
	private ArrayList<Byte> lesVoisins(byte[] dejaFait, byte plusPetit){
		byte cord_x = (byte) (plusPetit % 5);
		byte cord_y = (byte) (plusPetit / 5);
		ArrayList<Byte> voisins = new ArrayList<Byte>(4);
		if (cord_y+1 < Carte.yMaxCarte && dejaFait[cord_x+5*(cord_y+1)] == 0){
			voisins.add( (byte) (cord_x+5*(cord_y+1)));
		}
		if (cord_y-1 >= 0 && dejaFait[cord_x+5*(cord_y-1)] == 0){
			voisins.add((byte) (cord_x+5*(cord_y-1)));
		}
		if (cord_x+1 < Carte.xMaxCarte && dejaFait[cord_x+1+5*cord_y] == 0){
			voisins.add((byte) (cord_x+1+5*cord_y));		
		}
		if (cord_x-1 >= 0 && dejaFait[cord_x-1+5*cord_y] == 0){
			voisins.add((byte) (cord_x-1+5*cord_y));
		}
		return voisins;
	}
	
	
	/**
	 * Renvoit des coordon�es qui n'ont pas d�j� �t� investigu�es. Sinon -1.
	 * @param dejaFait : liste des points qui ont d�j� �t� investigu�s.
	 * @return la premi�re coordonn�es qui n'a pas d�j� �t� investigu�es.
	 */
	private byte premierNonFait(byte[] dejaFait){
		for (byte cord = 0; cord < dejaFait.length; cord++){
				if (dejaFait[cord] == 0){
					return cord;
				}
			}
		return -1;
	}
	
	/**
	 *  Retourne la case ayant le plus petit poids parmi les cases non explor�es.
	 * @param tableauPoids : liste des poids.
	 * @param dejaFait : liste des points qui ont d�j� �t� investigu�s.
	 * @param premierNonFait : premi�re coordonn�e trouv�e qui n'a pas d�j� �t� investigu�e.
	 * @return la coordonn�es du point ayant le plus petit poids.
	 */
	private byte poidsMinimum(short[] tableauPoids, byte [] dejaFait, byte premierNonFait){
		byte coordPetitPoids = premierNonFait;
		int plusPetitPoids = tableauPoids[coordPetitPoids];
		for (byte cord = coordPetitPoids; cord <tableauPoids.length; cord++){
			if (dejaFait[cord] == 0 && ((plusPetitPoids < 0 && tableauPoids[cord] >= 0) || (tableauPoids[cord] > 0 && tableauPoids[cord] < plusPetitPoids))) {
                    plusPetitPoids = tableauPoids[cord];
                    coordPetitPoids = cord;
                }
			}
		return coordPetitPoids;
	}
	
/**
 * 	COMMANDES
 */

	/**
	 *  Cr�ation de la carte en tenant compte de la pr�sence du robot adverse.
	 * @param robotPlace : coordonn�es du robot adverse.
	 */
	protected void creationCarteAvecPresenceRobotAdverse(short robotPlace) {
		carte.redefinitionCartePourUltrason(robotPlace);
	}
	
	/**
	 * Re-initialisation de la carte pour cr�ation de la carte avec les ultrasons.
	 */
	protected void ecraseCarteUltrason() {
		carte = new Carte(isSauvageons);
	}

}