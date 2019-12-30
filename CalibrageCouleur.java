package ProjetGOT;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;

/** 
 * DESCRIPTION
 * 
 * Création des méthodes pour le calibrage de la couleur.
 */
public class CalibrageCouleur {
	/*
	 * Calcul de la distance euclidienne :
	 * https://www.robotshop.com/community/forum/t/tcs3200-color-sensor-with-k-nearest-neighbor-classification-algorithm/13554
	 * 
	 * */

/**
 * ATTRIBUTS 	
 */
	private float [][] tabCouleurCalibre = new float [6][3];
	private static String [] nomCouleur = new String [] {"noir", "rouge", "orange", "vert", "blanc", "bleu"};
	private static EV3ColorSensor senseurCouleur;
	

/**
 *  CONSTRUCTEUR 
 *  @param senseurColor : capteur de couleur.
 */

	public CalibrageCouleur (EV3ColorSensor senseurColor) {
		CalibrageCouleur.senseurCouleur = senseurColor;
	}

/**
 * 	COMMANDES
 */
	/**
	 * Fonction qui calibre chaque couleur enoncer de la liste nomCouleur
	 * en établissant les trois couleurs primaire par liste à l'iteration de la couleur
	 * énoncer de la liste nomCouleur ;
	 */
	protected void Calibrage () {
		LCD.drawString("Pret pour le calibrage ?", 0, 0);
		Button.waitForAnyPress();
		LCD.clear();
		for (int i = 0;i<nomCouleur.length;i++ ) {
			LCD.drawString(("Couleur : "+nomCouleur[i]), 0, 0);
			Button.waitForAnyPress();
			if (Button.DOWN.isDown()) {
				tabCouleurCalibre[i] = returnColorRGB();
			}
			LCD.clear();
		}
	}

/**
 *  REQUETES
 */
	
	/**
	 * Cette méthode permet d'obtenir les codes RGB de la couleur captée lors du calibrage
	 * @return : retourne la couleur en code RGB.
	 */
	private static float [] returnColorRGB() {
		float[] sample = new float[3];
		SampleProvider meanColorPercep = new MeanFilter(senseurCouleur.getRGBMode(),5);// 0 est le numero de la case
		meanColorPercep.fetchSample(sample, 0);
		for(int i=0; i<=2; i++) {
				sample[i] = sample[i];
			}
		return sample;
	}
	
	/**
	 * Determine la couleur recu par le capteur de couleur en utilisant
	 * un intervalle de seuil de plus ou moins 0.3 du code RGB des listes de couleur
	 * calibre.
	 * @return : retourne un string qui est la couleur capter par le robot;
	 * 
	 */
	protected String getCalibrationCouleur() {
		float[] sample = new float[3];
		SampleProvider meanColorPercep = new MeanFilter(senseurCouleur.getRGBMode(),5);// 0 est le numero de la case
		meanColorPercep.fetchSample(sample, 0);
		float seuil = (float) 0.03;
		for(int i=0; i<tabCouleurCalibre.length; i++) {
			if (sample[0]>-seuil+tabCouleurCalibre[i][0] && sample[0]<seuil+tabCouleurCalibre[i][0]
					&& sample[1]>-seuil+tabCouleurCalibre[i][1] && sample[1]<seuil+tabCouleurCalibre[i][1]
					&& sample[2]>-seuil+tabCouleurCalibre[i][2] && sample[2]<seuil+tabCouleurCalibre[i][2] ) {
				return nomCouleur[i];
			}
		}
		return "Aucune couleur";
	}
	
	
	/**
	 * Essaie de nouvelle fonction pour pallier les defaut du capteur :
	 * Recupere le code RGB de la couleur capter, et trouve la distance euclidienne minimal
	 * entre toutes les valeurs de couleur calibrer
	 * @return : Retourne la couleur qui a la distance euclidienne la plus minimal 
	 */
	protected String getNouvelleCalibrationCouleur() {
		float[] sample = new float[3];
		SampleProvider meanColorPercep = new MeanFilter(senseurCouleur.getRGBMode(),5);// 0 est le numero de la case
		meanColorPercep.fetchSample(sample, 0);
		
		float min = distanceEuclidienne(sample, 0);
		int iMin = 0;
		for(int i=1; i<tabCouleurCalibre.length; i++) {
			float test = distanceEuclidienne(sample, i);
			if (min > test) {
				min = test;
				iMin = i;
			}
		}
		return nomCouleur[iMin];
	}
	
	/**
	 * Calcul la distance euclidienne entre une couleur capteur et une couleur calibrer 
	 * par la formule "racineCarre(somme(Xi-Yi)²)"
	 * @return : retourne un float qui est la distance euclidienne 
	 */
	private float distanceEuclidienne (float [] sample, int i) {
		double nb = (float) (Math.pow((sample[0]-tabCouleurCalibre[i][0]),2) + 
				Math.pow((sample[1]-tabCouleurCalibre[i][1]),2) +
				Math.pow((sample[2]-tabCouleurCalibre[i][2]),2));
		return (float) Math.sqrt(nb);
	}

}