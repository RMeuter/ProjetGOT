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
		// On initialise le point de départ comme le poids 0							};
		poids[position[1]][position[0]] = 0;
		
		//Carte des prédécesseurs
		int [][][] chemin = new int[][][]{
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}},
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}},
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}},
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}},
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}},
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}},
									{{0,0}, {0,0}, {0,0}, {0,0}, {0,0}}
									};
		// On initialise le successeur du point de départ comme un point inexistant. Sinon le programme s'arrête au prédécesseur et ne va pas jusqu'au but
		chemin[position[1]][position[0]][0] = -1;
		chemin[position[1]][position[0]][1] = -1;
		
		//Carte des sommets déjà recherché par dijkstra
		boolean [][] dejaFait= new boolean[][]{
								{false, false, false, false, false},
								{false, false, false, false, false},
								{false, false, false, false, false},
								{false, false, false, false, false},
								{false, false, false, false, false},
								{false, false, false, false, false},
								{false, false, false, false, false}
									};
									
		//Tant que tous les sommets n'ont pas été parcouru, on recherche le poids minimum parmi les sommets qui n'ont pas été parcouru
		while (finiOuPas(dejaFait) == false){
			//point actuellement investigué, n'ayant pas déjà été investigué et ayant le plus petit poids
			int[] plusPetit = poidsMinimum(poids, dejaFait);
			//Lorsque le sommet a été fait, on le passe à true
			dejaFait[plusPetit[1]][plusPetit[0]] = true;
	
			// liste des voisins du point actuellement investigué
			ArrayList<int[]> voisins = lesVoisins(dejaFait, plusPetit);
			
			//Pour chaque voisin du point, on regarde si ce point n'est pas + l'infini ou si le poids est plus grand que le point actuel + le poids du voisin traité actuellement
			for (int[] x : voisins){
				if (poids[x[1]][x[0]] > 0 || poids[x[1]][x[0]] > poids[plusPetit[1]][plusPetit[0]]+carte.getPoids(x)){
					//si c'est le cas, on actualise le poids avec le poids du point actuel et le poids du voisin
					poids[x[1]][x[0]] = poids[plusPetit[1]][plusPetit[0]]+carte.getPoids(x);
					// on actualise les coordonées du point actuel comme prédécesseur de son voisin
					chemin [x[1]][x[0]][0]= plusPetit[0];
					chemin [x[1]][x[0]][1]= plusPetit[1];
				}
			}
		}
		
		// on crée la liste qui contiendra les directions
		LinkedList <Integer> cheminFinal = new LinkedList <Integer>();
		// Le prédécesseur correspond aux coordonnées qui précède l'arrivée au goal
		int[] sucesseur = goal;
		int[] predecesseur = chemin[goal[1]][goal[0]];
		
		//Comme l'algo commence par le but pour remonter , on ajoute chaque direction en premier, devant les autres
				//Ici on transforme les coordonnées en direction
				
				//on définit le sud en haut de la carte et le nord en bas de la carte
				// L'est est à droite de la carte et l'ouest à gauche
		while (!(predecesseur[0] == -1 && predecesseur[1] == -1)){
			if (sucesseur[0]-predecesseur[0] == 1){
				//Si quand on soustrait les coordonnées x du predecesseur au successeur, on obtient 1, alors on va à l'est
				//(pex (4,0) - (3,0) = 1, pour passer de la case (3,0) = predecesseur, à la case (4,0) = successeur, il faut aller à droite = est 
				cheminFinal.addFirst(EST);
			}else if (sucesseur[0]-predecesseur[0] == -1){
				//Si quand on soustrait les coordonnées x du predecesseur au successeur, on obtient -1, alors on va à l'ouest
				//(pex (3,0) - (4,0) = -1, pour passer de la case (4,0) = predecesseur, à la case (3,0) = successeur, il faut aller à gauche = ouest 
				cheminFinal.addFirst(OUEST);
			}else if (sucesseur[1]-predecesseur[1] == 1){
				//Si quand on soustrait les coordonnées y du predecesseur au successeur, on obtient 1, alors on va au sud
				//(pex (0,4) - (0,3) = 1, pour passer de la case (0,3) = predecesseur, à la case (0,4) = successeur, il faut aller à en haut = sud 
				cheminFinal.addFirst(SUD);
			}else if (sucesseur[1]-predecesseur[1] == -1){
				//Si quand on soustrait les coordonnées y du predecesseur au successeur, on obtient -1, alors on va au nord
				//(pex (0,3) - (0,4) = -1, pour passer de la case (0,4) = predecesseur, à la case (0,3) = successeur, il faut aller à en bas = nord 
				cheminFinal.addFirst(NORD);
			}
			//afin de passer à la prochaine coordonnées, on passe à la prochaine coordonnées
			sucesseur = predecesseur;			
			predecesseur = chemin[sucesseur[1]][sucesseur[0]];
		}
		return cheminFinal;
	}
	
	// A partir des coordonnées d'où nous sommes actuellement, on récupère tous les voisins possibles
	//n'ayant pas déjà été explorés
	private ArrayList<int[]> lesVoisins(boolean [][] dejaFait, int[] plusPetit){
		// on a les coordonnées du point actuellement ayant le plus petit poids
		int x = plusPetit[0];
		int y = plusPetit[1];
		ArrayList<int[]> voisins = new ArrayList<int[]>(4);
		// Pour chaque coordonnée, on verifie que la coordonnées fait partie de la carte et que le sommet n'a pas déjà été fait
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
	
	
	// On regarde si on a exploré tous les cases de la carte
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
	
	//Renvoi des coordonées qui n'ont pas déjà été investigué
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
	
	// Retourne la case ayant le plus petit poids parmi les cases non explorées
	private int[] poidsMinimum(int[][] tableauPoids, boolean [][] dejaFait){
		// on initialise avec une coordonées qu'on est sûr qu'elle n'a pas déjà été explorée
		int [] coordPetitPoids = premiereNonFait(dejaFait);
		int plusPetitPoids = tableauPoids[coordPetitPoids[1]][coordPetitPoids[0]];
		for (int y =coordPetitPoids[1]; y <tableauPoids.length; y++){
			for (int x = coordPetitPoids[0]; x < tableauPoids[x].length; x++){
				// si la coordonnée n'a pas déjà été faite, que le poids est différent de l'infini et qu'il est inférieur au plus petit poids
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
