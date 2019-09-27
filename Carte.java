package ProjetGOT;

import lejos.robotics.Color;

public class Carte {
	
	private static int [][] Carte = new int[7][5];
	private static float tailleCase = 12;
	private static float ligneCase = (float) 1.5;
	private boolean isSauvageon;
	
	
	public Carte(boolean Camp) {
		this.isSauvageon=Camp;
		if (Camp==true) {
			this.Carte[0][0]=Color.RED;
			this.Carte[0][4]=Color.WHITE;
		} else {
			this.Carte[5][3]=Color.RED;
			this.Carte[6][0]=Color.WHITE;
		}
	}
	
	public int[] getGoal() {
		/*
		 * Le but est une coordonnée matricielle du camp
		 * Si c'est true la fonction retourne le but d'un sauvageons
		 * Sinon elle retourne le but de l'autre équipe
		 * */
		int [] goal= new int [2];
		if (this.isSauvageon) {
			goal[0]=5;
			goal[1]=3;			
		} else {
			goal[0]=0;
			goal[1]=4;
		}
		return goal;
	}

	public int[] getDebut() {
		/*
		 * Le but est une coordonnée matricielle du camp
		 * Si c'est true la fonction retourne le but d'un sauvageons
		 * Sinon elle retourne le but de l'autre équipe
		 * */
		int [] debut= new int [2];
		if (this.isSauvageon) {
			debut[0]=5;
			debut[1]=3;			
		} else {
			debut[0]=0;
			debut[1]=4;
		}
		return debut;
	}
	
	public int getPosition(int[] position) {
		return Carte[position[0]][position[1]];
	}

	public void setPosition(int[] position,int couleur) {
		Carte[position[0]][position[1]] = couleur;
	}
	
	
	
	}