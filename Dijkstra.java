package OBJECTIF1;

import java.util.ArrayList;
import java.util.LinkedList;

public class Dijkstra {
	
	public static final int SUD = 0; 
	public static final int EST = 90;
	public static final int OUEST = -90;
	public static final int NORD = 180;
	
	private int [] position = new int[2]; 
	
	private int [] goal = new int [2];
	
	private Carte carte;
	

	
	public LinkedList <Integer> dijkstra(){
		//Carte des poids
		// -1 = + l'infini
		int [][] poids = new int[][]{
								{-1, -1, -1, -1, -1},
								{-1, -1, -1, -1, -1},
								{-1, -1, -1, -1, -1},
								{-1, -1, -1, -1, -1},
								{-1, -1, -1, -1, -1},
								{-1, -1, -1, -1, -1},
								{-1, -1, -1, -1, -1}
							};
		// On initialise le point de d�part comme le poids 0							};
		poids[position[1]][position[0]] = 0;
		
		//Carte des pr�d�cesseurs
		int [][][] chemin = new int[][][]{
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}},
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}},
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}},
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}},
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}},
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}},
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}}
									};
		// On initialise le successeur du point de d�part comme un point inexistant. Sinon le programme s'arr�te au pr�d�cesseur et ne va pas jusqu'au but
		chemin[position[1]][position[0]][0] = -1;
		chemin[position[1]][position[0]][1] = -1;
		
		//Carte des sommets d�j� recherch� par dijkstra
		boolean [][] dejaFait= new boolean[][]{
								{false, false, false, false, false},
								{false, false, false, false, false},
								{false, false, false, false, false},
								{false, false, false, false, false},
								{false, false, false, false, false},
								{false, false, false, false, false},
								{false, false, false, false, false}
									};
									
		//Tant que tous les sommets n'ont pas �t� parcouru, on recherche le poids minimum parmi les sommets qui n'ont pas �t� parcouru
		while (finiOuPas(dejaFait) == false){
			//point actuellement investigu�, n'ayant pas d�j� �t� investigu� et ayant le plus petit poids
			int[] plusPetit = poidsMinimum(poids, dejaFait);
			//Lorsque le sommet a �t� fait, on le passe � true
			dejaFait[plusPetit[1]][plusPetit[0]] = true;
	
			// liste des voisins du point actuellement investigu�
			ArrayList<int[]> voisins = lesVoisins(dejaFait, plusPetit);
			
			//Pour chaque voisin du point, on regarde si ce point n'est pas + l'infini ou si le poids est plus grand que le point actuel + le poids du voisin trait� actuellement
			for (int[] x : voisins){
				if (poids[x[1]][x[0]] > 0 || poids[x[1]][x[0]] > poids[plusPetit[1]][plusPetit[0]]+carte.getPoids(x)){
					//si c'est le cas, on actualise le poids avec le poids du point actuel et le poids du voisin
					poids[x[1]][x[0]] = poids[plusPetit[1]][plusPetit[0]]+carte.getPoids(x);
					// on actualise les coordon�es du point actuel comme pr�d�cesseur de son voisin
					chemin [x[1]][x[0]][0]= plusPetit[0];
					chemin [x[1]][x[0]][1]= plusPetit[1];
				}
			}
		}
		
		// on cr�e la liste qui contiendra les directions
		LinkedList <Integer> cheminFinal = new LinkedList <Integer>();
		// Le pr�d�cesseur correspond aux coordonn�es qui pr�c�de l'arriv�e au goal
		int[] sucesseur = goal;
		int[] predecesseur = chemin[goal[1]][goal[0]];
		
		//Comme l'algo commence par le but pour remonter , on ajoute chaque direction en premier, devant les autres
				//Ici on transforme les coordonn�es en direction
				
				//on d�finit le sud en haut de la carte et le nord en bas de la carte
				// L'est est � droite de la carte et l'ouest � gauche
		while (!(predecesseur[0] == -1 && predecesseur[1] == -1)){
			if (sucesseur[0]-predecesseur[0] == 1){
				//Si quand on soustrait les coordonn�es x du predecesseur au successeur, on obtient 1, alors on va � l'est
				//(pex (4,0) - (3,0) = 1, pour passer de la case (3,0) = predecesseur, � la case (4,0) = successeur, il faut aller � droite = est 
				cheminFinal.addFirst(EST);
			}else if (sucesseur[0]-predecesseur[0] == -1){
				//Si quand on soustrait les coordonn�es x du predecesseur au successeur, on obtient -1, alors on va � l'ouest
				//(pex (3,0) - (4,0) = -1, pour passer de la case (4,0) = predecesseur, � la case (3,0) = successeur, il faut aller � gauche = ouest 
				cheminFinal.addFirst(OUEST);
			}else if (sucesseur[1]-predecesseur[1] == 1){
				//Si quand on soustrait les coordonn�es y du predecesseur au successeur, on obtient 1, alors on va au sud
				//(pex (0,4) - (0,3) = 1, pour passer de la case (0,3) = predecesseur, � la case (0,4) = successeur, il faut aller � en haut = sud 
				cheminFinal.addFirst(SUD);
			}else if (sucesseur[1]-predecesseur[1] == -1){
				//Si quand on soustrait les coordonn�es y du predecesseur au successeur, on obtient -1, alors on va au nord
				//(pex (0,3) - (0,4) = -1, pour passer de la case (0,4) = predecesseur, � la case (0,3) = successeur, il faut aller � en bas = nord 
				cheminFinal.addFirst(NORD);
			}
			//afin de passer � la prochaine coordonn�es, on passe � la prochaine coordonn�es
			sucesseur = predecesseur;			
			predecesseur = chemin[sucesseur[1]][sucesseur[0]];
		}
		return cheminFinal;
	}
	
	// A partir des coordonn�es d'o� nous sommes actuellement, on r�cup�re tous les voisins possibles
	//n'ayant pas d�j� �t� explor�s
	private ArrayList<int[]> lesVoisins(boolean [][] dejaFait, int[] plusPetit){
		// on a les coordonn�es du point actuellement ayant le plus petit poids
		int x = plusPetit[0];
		int y = plusPetit[1];
		ArrayList<int[]> voisins = new ArrayList<int[]>(4);
		// Pour chaque coordonn�e, on verifie que la coordonn�es fait partie de la carte et que le sommet n'a pas d�j� �t� fait
		if (y+1 < dejaFait.length && dejaFait[y+1][x] == false){
			voisins.add(new int[] {x,y+1});
		}
		if (y-1 >= 0 && dejaFait[y-1][x] == false){
			voisins.add(new int[] {x,y-1});
		}
		if (x+1 < dejaFait[y].length && dejaFait[y][x+1] == false){
			voisins.add(new int[] {x+1,y});		
		}
		if (x-1 >= 0 && dejaFait[y][x-1] == false){
			voisins.add(new int[] {x-1,y});
		}
		return voisins;
	}
	
	
	// On regarde si on a explor� tous les cases de la carte
	private boolean finiOuPas(boolean[][] dejaFait){
		for (int y = 0; y < dejaFait.length; y++){
			for (int x = 0; x < dejaFait[y].length; x++){
			if (dejaFait[y][x] == false){
				return false;
				}
			}
		}
		return true;
	}
	
	//Renvoi des coordon�es qui n'ont pas d�j� �t� investigu�
	private int[] premiereNonFait(boolean [][] dejaFait){
		for (int y = 0; y < dejaFait.length; y++){
			for (int x = 0; x < dejaFait[y].length; x++){
				if (dejaFait[y][x] == false){
					return  new int[] {x, y};
				}
			}
		}
		throw new InternalError();
	}
	
	// Retourne la case ayant le plus petit poids parmi les cases non explor�es
	private int[] poidsMinimum(int[][] tableauPoids, boolean [][] dejaFait){
		// on initialise avec une coordon�es qu'on est s�r qu'elle n'a pas d�j� �t� explor�e
		int [] coordPetitPoids = premiereNonFait(dejaFait);
		int plusPetitPoids = tableauPoids[coordPetitPoids[1]][coordPetitPoids[0]];
		for (int y =coordPetitPoids[1]; y <tableauPoids.length; y++){
			for (int x = coordPetitPoids[0]; x < tableauPoids[x].length; x++){
				// si la coordonn�e n'a pas d�j� �t� faite, que le poids est diff�rent de l'infini et qu'il est inf�rieur au plus petit poids
				if (!dejaFait[y][x] && ((plusPetitPoids < 0 && tableauPoids[y][x] >= 0) || (tableauPoids[y][x] > 0 && tableauPoids[y][x] < plusPetitPoids))) {
                    plusPetitPoids = tableauPoids[y][x];
                    coordPetitPoids[0] = x;
                    coordPetitPoids[1] = y;
                }
			}
		}
		return coordPetitPoids;
	}
}
