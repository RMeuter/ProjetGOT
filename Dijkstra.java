package OBJECTIF1;

import java.util.ArrayList;
import java.util.LinkedList;


public class Dijkstra {
	
	public static final short SUD = 180; 
	public static final short EST = -90;
	public static final short OUEST = 90;
	public static final short NORD = 0;

	
	private Carte carte;

	
	public Dijkstra(boolean typeCarte){
		carte = new Carte(typeCarte);
	}

	public LinkedList <Short> dijkstra(byte position, byte goal){
		//Carte des poids
		// -1 = + l'infini
		short [] poids = initTableauPoids ();
		// On initialise le point de départ comme le poids 0	
		
		poids[position] = 0;
		
		
		//Carte des prédécesseurs
		byte [] chemin = new byte[35];
		// On initialise le successeur du point de départ comme un point inexistant. Sinon le programme s'arrête au prédécesseur et ne va pas jusqu'au but
		chemin[position] = -1;
		
		
		
		//Carte des sommets déjà recherché par dijkstra
		// 0 = false, 1 = true
		byte [] dejaFait= new byte[35];
									
		//Tant que tous les sommets n'ont pas été parcouru, on recherche le poids minimum parmi les sommets qui n'ont pas été parcouru
		
		byte premierNonFait = premierNonFait(dejaFait);
		while (premierNonFait != -1){

			//point actuellement investigué, n'ayant pas déjà été investigué et ayant le plus petit poids
			byte plusPetit = poidsMinimum(poids, dejaFait, premierNonFait);
			

			
			//Lorsque le sommet a été fait, on le passe à true
			dejaFait[plusPetit] = 1;
	
			// liste des voisins du point actuellement investigué
			ArrayList<Byte> voisins = lesVoisins(dejaFait, plusPetit);
			
			//Pour chaque voisin du point, on regarde si ce point n'est pas + l'infini ou si le poids est plus grand que le point actuel + le poids du voisin traité actuellement
			for (byte x : voisins){
				if (poids[x] < 0 || poids[x] > poids[plusPetit]+carte.getPoids(x)){

					//si c'est le cas, on actualise le poids avec le poids du point actuel et le poids du voisin
					poids[x] = (short) (poids[plusPetit]+carte.getPoids(x));
					// on actualise les coordonées du point actuel comme prédécesseur de son voisin
					chemin [x]= plusPetit;
				}
			}
			premierNonFait = premierNonFait(dejaFait);
		}
		
		
		
		
		// on crée la liste qui contiendra les directions
		LinkedList <Short> cheminFinal = new LinkedList <Short>();
		// Le prédécesseur correspond aux coordonnées qui précède l'arrivée au goal
		byte successeur = goal;
		byte predecesseur = chemin[goal];
		
//        for (byte y = 0; y < dejaFait.length; ++y) {
//			byte xch = (byte) (chemin[y] /5);
//			byte ych = (byte) (chemin[y] % 5);
//                System.out.print("(" + xch  + ", " + ych + ")  ");
//            }
//            System.out.print("\n");
//        
		
		//Comme l'algo commence par le but pour remonter , on ajoute chaque direction en premier, devant les autres
				//Ici on transforme les coordonnées en direction
				
				//on définit le sud en haut de la carte et le nord en bas de la carte
				// L'est est à droite de la carte et l'ouest à gauche
		while (!(predecesseur == -1)){
			//System.out.println("successeur"+successeur);
			//System.out.println("predecesseur"+predecesseur);
			byte xSuc = (byte) (successeur % 5);
			byte ySuc = (byte) (successeur / 5);
			byte xPr = (byte) (predecesseur % 5);
			byte yPr = (byte) (predecesseur / 5);
			if (xSuc-xPr == 1){
				//Si quand on soustrait les coordonnées x du predecesseur au successeur, on obtient 1, alors on va à l'est
				//(pex (4,0) - (3,0) = 1, pour passer de la case (3,0) = predecesseur, à la case (4,0) = successeur, il faut aller à droite = est 
				cheminFinal.addFirst(EST);
			}else if (xSuc-xPr == -1){
				//Si quand on soustrait les coordonnées x du predecesseur au successeur, on obtient -1, alors on va à l'ouest
				//(pex (3,0) - (4,0) = -1, pour passer de la case (4,0) = predecesseur, à la case (3,0) = successeur, il faut aller à gauche = ouest 
				cheminFinal.addFirst(OUEST);
			}else if (ySuc-yPr == 1){
				//Si quand on soustrait les coordonnées y du predecesseur au successeur, on obtient 1, alors on va au sud
				//(pex (0,4) - (0,3) = 1, pour passer de la case (0,3) = predecesseur, à la case (0,4) = successeur, il faut aller à en haut = sud 
				cheminFinal.addFirst(SUD);
			}else if (ySuc-yPr == -1){
				//Si quand on soustrait les coordonnées y du predecesseur au successeur, on obtient -1, alors on va au nord
				//(pex (0,3) - (0,4) = -1, pour passer de la case (0,4) = predecesseur, à la case (0,3) = successeur, il faut aller à en bas = nord 
				cheminFinal.addFirst(NORD);
			}
			//afin de passer à la prochaine coordonnées, on passe à la prochaine coordonnées
			successeur = predecesseur;			
			predecesseur = chemin[successeur];
		}
		return cheminFinal;
	}
	
	private short [] initTableauPoids (){
		short [] TableauPoids = new short [35];
		for (int x = 0; x<TableauPoids.length; x++){
			TableauPoids[x] = -1;
		}
		return TableauPoids;
	}
	
	// A partir des coordonnées d'où nous sommes actuellement, on récupère tous les voisins possibles
	//n'ayant pas déjà été explorés
	private ArrayList<Byte> lesVoisins(byte[] dejaFait, byte plusPetit){
		// on a les coordonnées du point actuellement ayant le plus petit poids
		byte x = (byte) (plusPetit % 5);
		byte y = (byte) (plusPetit / 5);
		ArrayList<Byte> voisins = new ArrayList<Byte>(4);
		// Pour chaque coordonnée, on verifie que la coordonnées fait partie de la carte et que le sommet n'a pas déjà été fait
		if (y+1 < Carte.yCarte && dejaFait[x+5*(y+1)] == 0){
			voisins.add( (byte) (x+5*(y+1)));
		}
		if (y-1 >= 0 && dejaFait[x+5*(y-1)] == 0){
			voisins.add((byte) (x+5*(y-1)));
		}
		if (x+1 < Carte.xCarte && dejaFait[x+1+5*y] == 0){
			voisins.add((byte) (x+1+5*y));		
		}
		if (x-1 >= 0 && dejaFait[x-1+5*y] == 0){
			voisins.add((byte) (x-1+5*y));
		}
		return voisins;
	}
	
	
	//Renvoi des coordonées qui n'ont pas déjà été investigué
	private byte premierNonFait(byte[] dejaFait){
		for (byte x = 0; x < dejaFait.length; x++){
				if (dejaFait[x] == 0){
					return x;
				}
			}
		return -1;
	}
	
	// Retourne la case ayant le plus petit poids parmi les cases non explorées
	private byte poidsMinimum(short[] tableauPoids, byte [] dejaFait, byte premierNonFait){
		// on initialise avec une coordonées qu'on est sûr qu'elle n'a pas déjà été explorée
		byte coordPetitPoids = premierNonFait;
		int plusPetitPoids = tableauPoids[coordPetitPoids];
		for (byte x =coordPetitPoids; x <tableauPoids.length; x++){
				// si la coordonnée n'a pas déjà été faite, que le poids est différent de l'infini et qu'il est inférieur au plus petit poids
			if (dejaFait[x] == 0 && ((plusPetitPoids < 0 && tableauPoids[x] >= 0) || (tableauPoids[x] > 0 && tableauPoids[x] < plusPetitPoids))) {
                    plusPetitPoids = tableauPoids[x];
                    coordPetitPoids = x;
                }
			}
		return coordPetitPoids;
	}
}