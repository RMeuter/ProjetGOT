package ProjetGOT;
import java.util.ArrayList;
import java.util.LinkedList;


public class Dijkstra {
	
	public static final short SUD = 180; 
	public static final short EST = -90;
	public static final short OUEST = 90;
	public static final short NORD = 0;
	
	private Carte carte;
	
	public Dijkstra() {
		carte = new Carte(true);
	}
	
	

	public LinkedList <Short> dijkstra(byte position, byte goal){
		short [] poids = initTableauPoids (); // On initialise le point de départ comme le poids 0	
		poids[position] = 0;
		byte [] chemin = new byte[35]; //Carte des prédécesseurs
		chemin[position] = -1;
		byte [] dejaFait= new byte[35];
		byte premierNonFait = premierNonFait(dejaFait);
		while (premierNonFait != -1){
			byte plusPetit = poidsMinimum(poids, dejaFait, premierNonFait);
			dejaFait[plusPetit] = 1;
			ArrayList<Byte> voisins = lesVoisins(dejaFait, plusPetit);
			for (byte x : voisins){
				if (poids[x] < 0 || poids[x] > poids[plusPetit]+carte.getPoids(x)){
					poids[x] = (short) (poids[plusPetit]+carte.getPoids(x));
					chemin [x]= plusPetit;
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
	
	private short [] initTableauPoids (){
		short [] TableauPoids = new short [35];
		for (int x = 0; x<TableauPoids.length; x++){
			TableauPoids[x] = -1;
		}
		return TableauPoids;
	}
	
	private ArrayList<Byte> lesVoisins(byte[] dejaFait, byte plusPetit){
		byte x = (byte) (plusPetit % 5);
		byte y = (byte) (plusPetit / 5);
		ArrayList<Byte> voisins = new ArrayList<Byte>(4);
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
	
	
	private byte premierNonFait(byte[] dejaFait){
		for (byte x = 0; x < dejaFait.length; x++){
				if (dejaFait[x] == 0){
					return x;
				}
			}
		return -1;
	}
	
	private byte poidsMinimum(short[] tableauPoids, byte [] dejaFait, byte premierNonFait){
		byte coordPetitPoids = premierNonFait;
		int plusPetitPoids = tableauPoids[coordPetitPoids];
		for (byte x =coordPetitPoids; x <tableauPoids.length; x++){
			if (dejaFait[x] == 0 && ((plusPetitPoids < 0 && tableauPoids[x] >= 0) || (tableauPoids[x] > 0 && tableauPoids[x] < plusPetitPoids))) {
                    plusPetitPoids = tableauPoids[x];
                    coordPetitPoids = x;
                }
			}
		return coordPetitPoids;
	}
}