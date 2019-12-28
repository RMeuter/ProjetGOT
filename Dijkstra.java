package ProjetGOT;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * DESCRIPTION 
 * Définition de l'algorithme de Dijkstra, afin de trouver le plus court chemin d'un point A à un point B.
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
 * @param typeCarte : booléen pour connaître le camp du robot (sauvageon ou garde de nuit).
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
	 * On définit -1 comme l'infini.
	 *  0 = false, 1 = true.
	 *  On définit le sud en haut de la carte et le nord en bas de la carte.
	 * L'est est à droite de la carte et l'ouest à gauche.
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
	 *  Initialisation du tableau des poids, avec tous les poids à -1 (pas faits).
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
	 *  A partir des coordonnées d'où nous sommes actuellement, on récupère tous les voisins possibles
	 *  n'ayant pas déjà été explorés.
	 * @param dejaFait : liste des points qui ont déjà été investigué.
	 * @param plusPetit : point ayant le plus petit poids.
	 * @return les voisins du plus petit poids actuellement investigué.
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
	 * Renvoit des coordonées qui n'ont pas déjà été investiguées. Sinon -1.
	 * @param dejaFait : liste des points qui ont déjà été investigués.
	 * @return la première coordonnées qui n'a pas déjà été investiguées.
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
	 *  Retourne la case ayant le plus petit poids parmi les cases non explorées.
	 * @param tableauPoids : liste des poids.
	 * @param dejaFait : liste des points qui ont déjà été investigués.
	 * @param premierNonFait : première coordonnée trouvée qui n'a pas déjà été investiguée.
	 * @return la coordonnées du point ayant le plus petit poids.
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
	 *  Création de la carte en tenant compte de la présence du robot adverse.
	 * @param robotPlace : coordonnées du robot adverse.
	 */
	protected void creationCarteAvecPresenceRobotAdverse(short robotPlace) {
		carte.redefinitionCartePourUltrason(robotPlace);
	}
	
	/**
	 * Re-initialisation de la carte pour création de la carte avec les ultrasons.
	 */
	protected void ecraseCarteUltrason() {
		carte = new Carte(isSauvageons);
	}

}