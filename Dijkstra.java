package ProjetGOT;
import java.util.ArrayList;
import java.util.LinkedList;

//DESCRIPTION //
	// D�finition de l'algorithme de Dijkstra, afin de trouver le plus court chemin d'un point A � un point B.

public class Dijkstra {
	
//	ATTRIBUTS //	
	public static final short SUD = 180; 
	public static final short EST = -90;
	public static final short OUEST = 90;
	public static final short NORD = 0;

	private boolean isSauvageons;
	private Carte carte;

// CONSTRUCTEURS //	
	public Dijkstra(boolean typeCarte){
		isSauvageons = typeCarte;
		carte = new Carte(isSauvageons);
	}

// REQUETES //	
	// Algorithme de Dijkstra.
	// C'est la fonction principale.
	public LinkedList <Short> dijkstra(byte position, byte goal){
		//Carte des poids.
		// -1 = + l'infini.
		short [] poids = initTableauPoids ();
		// On initialise le point de d�part comme le poids 0.	
		poids[position] = 0;
		
		//Carte des pr�d�cesseurs
		byte [] chemin = new byte[35];
		// On initialise le successeur du point de d�part comme un point inexistant. Sinon le programme s'arr�te au pr�d�cesseur et ne va pas jusqu'au but.
		chemin[position] = -1;
		
		//Carte des sommets d�j� recherch� par l'algorithme.
		// 0 = false, 1 = true.
		byte [] dejaFait= new byte[35];
									
		//Tant que tous les sommets n'ont pas �t� parcouru, on recherche le poids minimum parmi les sommets qui n'ont pas �t� parcouru.
		byte premierNonFait = premierNonFait(dejaFait);
		while (premierNonFait != -1){

			//Point actuellement investigu�. Il n'a pas d�j� �t� investigu� et a le plus petit poids.
			byte plusPetit = poidsMinimum(poids, dejaFait, premierNonFait);
						
			//Lorsque le sommet a �t� fait, on le passe � true.
			dejaFait[plusPetit] = 1;
	
			// Liste des voisins du point actuellement investigu�.
			ArrayList<Byte> voisins = lesVoisins(dejaFait, plusPetit);
			
			//Pour chaque voisin du point, on regarde si ce point est diff�rent de + l'infini ou si le poids est plus grand que le point actuel 
			// et le poids du voisin trait� actuellement.
			for (byte x : voisins){
				if (poids[x] < 0 || poids[x] > poids[plusPetit]+carte.getPoids(x)){

					//Si c'est le cas, on actualise le poids avec le poids du point actuel et le poids du voisin.
					poids[x] = (short) (poids[plusPetit]+carte.getPoids(x));
					// On actualise les coordon�es du point actuel comme pr�d�cesseur de son voisin.
					chemin [x]= plusPetit;
				}
			}
			// On actualise la liste des points qui n'ont pas encore �t� parcourus.
			premierNonFait = premierNonFait(dejaFait);
		}
		
		// Cr�ation de la liste qui contiendra les directions.
		LinkedList <Short> cheminFinal = new LinkedList <Short>();
		// Le pr�d�cesseur correspond aux coordonn�es qui pr�c�de l'arriv�e au goal.
		byte successeur = goal;
		byte predecesseur = chemin[goal];       
		
		
		//On transforme les coordonn�es en directions.
		//Comme l'algorithme commence par le but, il faut remonter la liste. On ajoute chaque direction en premier, devant les autres.		
		//On d�finit le sud en haut de la carte et le nord en bas de la carte.
		// L'est est � droite de la carte et l'ouest � gauche.
		while (!(predecesseur == -1)){
			byte xSuc = (byte) (successeur % 5);
			byte ySuc = (byte) (successeur / 5);
			byte xPr = (byte) (predecesseur % 5);
			byte yPr = (byte) (predecesseur / 5);
			if (xSuc-xPr == 1){
				//Si quand on soustrait les coordonn�es x du predecesseur au successeur, on obtient 1, alors on va � l'est.
				//(pex (4,0) - (3,0) = 1, pour passer de la case (3,0) = predecesseur, � la case (4,0) = successeur, il faut aller � droite = est).
				cheminFinal.addFirst(EST);
			}else if (xSuc-xPr == -1){
				//Si quand on soustrait les coordonn�es x du predecesseur au successeur, on obtient -1, alors on va � l'ouest.
				//(pex (3,0) - (4,0) = -1, pour passer de la case (4,0) = predecesseur, � la case (3,0) = successeur, il faut aller � gauche = ouest).
				cheminFinal.addFirst(OUEST);
			}else if (ySuc-yPr == 1){
				//Si quand on soustrait les coordonn�es y du predecesseur au successeur, on obtient 1, alors on va au sud.
				//(pex (0,4) - (0,3) = 1, pour passer de la case (0,3) = predecesseur, � la case (0,4) = successeur, il faut aller � en haut = sud).
				cheminFinal.addFirst(SUD);
			}else if (ySuc-yPr == -1){
				//Si quand on soustrait les coordonn�es y du predecesseur au successeur, on obtient -1, alors on va au nord.
				//(pex (0,3) - (0,4) = -1, pour passer de la case (0,4) = predecesseur, � la case (0,3) = successeur, il faut aller � en bas = nord).
				cheminFinal.addFirst(NORD);
			}
			//On passe � la prochaine coordonn�e.
			successeur = predecesseur;			
			predecesseur = chemin[successeur];
		}
		return cheminFinal;
	}
	
	// Initialisation du tableau des poids, avec tous les poids � -1 (pas faits).
	private short [] initTableauPoids (){
		short [] TableauPoids = new short [35];
		for (int x = 0; x<TableauPoids.length; x++){
			TableauPoids[x] = -1;
		}
		return TableauPoids;
	}
	
	// A partir des coordonn�es d'o� nous sommes actuellement, on r�cup�re tous les voisins possibles
	//n'ayant pas d�j� �t� explor�s.
	private ArrayList<Byte> lesVoisins(byte[] dejaFait, byte plusPetit){
		// On a les coordonn�es du point actuellement ayant le plus petit poids.
		byte x = (byte) (plusPetit % 5);
		byte y = (byte) (plusPetit / 5);
		ArrayList<Byte> voisins = new ArrayList<Byte>(4);
		// Pour chaque coordonn�e, on verifie que la coordonn�e fait partie de la carte et que le sommet n'a pas d�j� �t� fait.
		if (y+1 < Carte.yMaxCarte && dejaFait[x+5*(y+1)] == 0){
			voisins.add( (byte) (x+5*(y+1)));
		}
		if (y-1 >= 0 && dejaFait[x+5*(y-1)] == 0){
			voisins.add((byte) (x+5*(y-1)));
		}
		if (x+1 < Carte.xMaxCarte && dejaFait[x+1+5*y] == 0){
			voisins.add((byte) (x+1+5*y));		
		}
		if (x-1 >= 0 && dejaFait[x-1+5*y] == 0){
			voisins.add((byte) (x-1+5*y));
		}
		return voisins;
	}
	
	
	//Renvoit des coordon�es qui n'ont pas d�j� �t� investigu�s.
	// Sinon renvoit -1.
	private byte premierNonFait(byte[] dejaFait){
		for (byte x = 0; x < dejaFait.length; x++){
				if (dejaFait[x] == 0){
					return x;
				}
			}
		return -1;
	}
	
	// Retourne la case ayant le plus petit poids parmi les cases non explor�es.
	private byte poidsMinimum(short[] tableauPoids, byte [] dejaFait, byte premierNonFait){
		// On initialise avec une coordon�es qu'on est s�r qu'elle n'a pas d�j� �t� explor�e.
		byte coordPetitPoids = premierNonFait;
		int plusPetitPoids = tableauPoids[coordPetitPoids];
		for (byte x =coordPetitPoids; x <tableauPoids.length; x++){
				// Si la coordonn�e n'a pas d�j� �t� faite, que le poids est diff�rent de l'infini et qu'il est inf�rieur au plus petit poids.
			if (dejaFait[x] == 0 && ((plusPetitPoids < 0 && tableauPoids[x] >= 0) || (tableauPoids[x] > 0 && tableauPoids[x] < plusPetitPoids))) {
                    plusPetitPoids = tableauPoids[x];
                    coordPetitPoids = x;
                }
			}
		return coordPetitPoids;
	}
	
//	COMMANDES //

	// Cr�ation de la carte en tenant compte de la pr�sence du robot adverse.
	protected void creationCarteAvecPresenceRobotAdverse(short robotPlace) {
		carte.redefinitionCartePourUltrason(robotPlace);
	}
	// Re-initialisation de la carte pour cr�ation de la carte avec les ultrasons.
	protected void ecraseCarteUltrason() {
		carte = new Carte(isSauvageons);
	}

}