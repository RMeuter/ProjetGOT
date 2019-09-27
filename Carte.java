package OBJECTIF1;

import lejos.robotics.Color;

public class Carte {

	private int [][] CarteCouleur; //
	private static float tailleCase = 12;
	private static float ligneCase = (float) 1.5;
	private int [] positionDynamique = new int[2]; //entre -1 et 1 -> la rotation
	private int [] positionHistorique = new int[2]; //sur la carte avec coordonnées
	private boolean isSauvageon = true; 
	
	
	
	public Carte(Boolean Camp){
		this.isSauvageon = Camp;
		if (Camp == true){
			//Sauvageons
			this.CarteCouleur = new int[][]{
			    {Color.RED, Color.BLUE, Color.GREEN, Color.GREEN, Color.WHITE},
				{Color.GRAY, Color.BLUE, Color.GREEN, Color.GREEN, Color.GREEN},
				{Color.GRAY, Color.BLUE, Color.BLUE, Color.GREEN, Color.ORANGE},
				{Color.GRAY, Color.GRAY, Color.BLUE, Color.GREEN, Color.GREEN},
				{Color.GRAY, Color.GRAY, Color.GRAY, Color.ORANGE, Color.GREEN},
				{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.BLUE},
				{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.BLUE}
			};
								
		}else {
			//Garde de nuit
			this.CarteCouleur = new int[][] {
				 	{Color.RED, Color.BLUE, Color.GRAY, Color.GRAY, Color.GRAY},
					{Color.GRAY, Color.BLUE, Color.GRAY, Color.GRAY, Color.GRAY},
					{Color.GRAY, Color.BLUE, Color.BLUE, Color.GRAY, Color.GRAY},
					{Color.GRAY, Color.GRAY, Color.BLUE, Color.GRAY, Color.GRAY},
					{Color.GRAY, Color.GRAY, Color.GRAY, Color.ORANGE, Color.GRAY},
					{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.BLUE},
					{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.BLUE}
				};
		}
	}
	
	/*
	public boolean estBloque(int x, int y, Direction d) {
		if (x == 0 && d == Direction.Ouest || y == 0 && d == Direction.Nord || x == tailleX-1 && d == Direction.Est || y == tailleY -1 && d == Direction.Sud) {	
			return true;
		}else {
			return false;
		}
	*/
	public int[] getGoal(){
		/*
		 * true = sauvageon
		 * false = garde de nuit
		 */
		int[] goal = new int[2];
		if (this.isSauvageon){
			goal[0] = 0;
			goal[1] = 0;
		}else {
			goal[0] = 5;
			goal[1] = 3;
		}
		return goal;
	}
	
	public int[] getDebut(){
		/*
		 * true = sauvageon
		 * false = garde de nuit
		 */
		int[] debut = new int[2];
		if (this.isSauvageon){
			debut[0] = 0;
			debut[1] = 4;
		}else {
			debut[0] = 6;
			debut[1] = 0;
		}
		return debut;
	}
	

}

/*
 * public enum TypeCase {
	Camp, Prairie, Mur, Marecage; }
 */
