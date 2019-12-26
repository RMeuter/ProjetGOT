package ProjetGOT;
import java.util.ArrayList;
import java.util.LinkedList;

//DESCRIPTION //
	// Définition de l'algorithme de Dijkstra, afin de trouver le plus court chemin d'un point A à un point B.

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
		// On initialise le point de départ comme le poids 0.	
		poids[position] = 0;
		
		//Carte des prédécesseurs
		byte [] chemin = new byte[35];
		// On initialise le successeur du point de départ comme un point inexistant. Sinon le programme s'arrête au prédécesseur et ne va pas jusqu'au but.
		chemin[position] = -1;
		
		//Carte des sommets déjà recherché par l'algorithme.
		// 0 = false, 1 = true.
		byte [] dejaFait= new byte[35];
									
		//Tant que tous les sommets n'ont pas été parcouru, on recherche le poids minimum parmi les sommets qui n'ont pas été parcouru.
		byte premierNonFait = premierNonFait(dejaFait);
		while (premierNonFait != -1){

			//Point actuellement investigué. Il n'a pas déjà été investigué et a le plus petit poids.
			byte plusPetit = poidsMinimum(poids, dejaFait, premierNonFait);
						
			//Lorsque le sommet a été fait, on le passe à true.
			dejaFait[plusPetit] = 1;
	
			// Liste des voisins du point actuellement investigué.
			ArrayList<Byte> voisins = lesVoisins(dejaFait, plusPetit);
			
			//Pour chaque voisin du point, on regarde si ce point est différent de + l'infini ou si le poids est plus grand que le point actuel 
			// et le poids du voisin traité actuellement.
			for (byte x : voisins){
				if (poids[x] < 0 || poids[x] > poids[plusPetit]+carte.getPoids(x)){

					//Si c'est le cas, on actualise le poids avec le poids du point actuel et le poids du voisin.
					poids[x] = (short) (poids[plusPetit]+carte.getPoids(x));
					// On actualise les coordonées du point actuel comme prédécesseur de son voisin.
					chemin [x]= plusPetit;
				}
			}
			// On actualise la liste des points qui n'ont pas encore été parcourus.
			premierNonFait = premierNonFait(dejaFait);
		}
		
		// Création de la liste qui contiendra les directions.
		LinkedList <Short> cheminFinal = new LinkedList <Short>();
		// Le prédécesseur correspond aux coordonnées qui précède l'arrivée au goal.
		byte successeur = goal;
		byte predecesseur = chemin[goal];       
		
		
		//On transforme les coordonnées en directions.
		//Comme l'algorithme commence par le but, il faut remonter la liste. On ajoute chaque direction en premier, devant les autres.		
		//On définit le sud en haut de la carte et le nord en bas de la carte.
		// L'est est à droite de la carte et l'ouest à gauche.
		while (!(predecesseur == -1)){
			byte xSuc = (byte) (successeur % 5);
			byte ySuc = (byte) (successeur / 5);
			byte xPr = (byte) (predecesseur % 5);
			byte yPr = (byte) (predecesseur / 5);
			if (xSuc-xPr == 1){
				//Si quand on soustrait les coordonnées x du predecesseur au successeur, on obtient 1, alors on va à l'est.
				//(pex (4,0) - (3,0) = 1, pour passer de la case (3,0) = predecesseur, à la case (4,0) = successeur, il faut aller à droite = est).
				cheminFinal.addFirst(EST);
			}else if (xSuc-xPr == -1){
				//Si quand on soustrait les coordonnées x du predecesseur au successeur, on obtient -1, alors on va à l'ouest.
				//(pex (3,0) - (4,0) = -1, pour passer de la case (4,0) = predecesseur, à la case (3,0) = successeur, il faut aller à gauche = ouest).
				cheminFinal.addFirst(OUEST);
			}else if (ySuc-yPr == 1){
				//Si quand on soustrait les coordonnées y du predecesseur au successeur, on obtient 1, alors on va au sud.
				//(pex (0,4) - (0,3) = 1, pour passer de la case (0,3) = predecesseur, à la case (0,4) = successeur, il faut aller à en haut = sud).
				cheminFinal.addFirst(SUD);
			}else if (ySuc-yPr == -1){
				//Si quand on soustrait les coordonnées y du predecesseur au successeur, on obtient -1, alors on va au nord.
				//(pex (0,3) - (0,4) = -1, pour passer de la case (0,4) = predecesseur, à la case (0,3) = successeur, il faut aller à en bas = nord).
				cheminFinal.addFirst(NORD);
			}
			//On passe à la prochaine coordonnée.
			successeur = predecesseur;			
			predecesseur = chemin[successeur];
		}
		return cheminFinal;
	}
	
	// Initialisation du tableau des poids, avec tous les poids à -1 (pas faits).
	private short [] initTableauPoids (){
		short [] TableauPoids = new short [35];
		for (int x = 0; x<TableauPoids.length; x++){
			TableauPoids[x] = -1;
		}
		return TableauPoids;
	}
	
	// A partir des coordonnées d'où nous sommes actuellement, on récupère tous les voisins possibles
	//n'ayant pas déjà été explorés.
	private ArrayList<Byte> lesVoisins(byte[] dejaFait, byte plusPetit){
		// On a les coordonnées du point actuellement ayant le plus petit poids.
		byte x = (byte) (plusPetit % 5);
		byte y = (byte) (plusPetit / 5);
		ArrayList<Byte> voisins = new ArrayList<Byte>(4);
		// Pour chaque coordonnée, on verifie que la coordonnée fait partie de la carte et que le sommet n'a pas déjà été fait.
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
	
	
	//Renvoit des coordonées qui n'ont pas déjà été investigués.
	// Sinon renvoit -1.
	private byte premierNonFait(byte[] dejaFait){
		for (byte x = 0; x < dejaFait.length; x++){
				if (dejaFait[x] == 0){
					return x;
				}
			}
		return -1;
	}
	
	// Retourne la case ayant le plus petit poids parmi les cases non explorées.
	private byte poidsMinimum(short[] tableauPoids, byte [] dejaFait, byte premierNonFait){
		// On initialise avec une coordonées qu'on est sûr qu'elle n'a pas déjà été explorée.
		byte coordPetitPoids = premierNonFait;
		int plusPetitPoids = tableauPoids[coordPetitPoids];
		for (byte x =coordPetitPoids; x <tableauPoids.length; x++){
				// Si la coordonnée n'a pas déjà été faite, que le poids est différent de l'infini et qu'il est inférieur au plus petit poids.
			if (dejaFait[x] == 0 && ((plusPetitPoids < 0 && tableauPoids[x] >= 0) || (tableauPoids[x] > 0 && tableauPoids[x] < plusPetitPoids))) {
                    plusPetitPoids = tableauPoids[x];
                    coordPetitPoids = x;
                }
			}
		return coordPetitPoids;
	}
	
//	COMMANDES //

	// Création de la carte en tenant compte de la présence du robot adverse.
	protected void creationCarteAvecPresenceRobotAdverse(short robotPlace) {
		carte.redefinitionCartePourUltrason(robotPlace);
	}
	// Re-initialisation de la carte pour création de la carte avec les ultrasons.
	protected void ecraseCarteUltrason() {
		carte = new Carte(isSauvageons);
	}

}