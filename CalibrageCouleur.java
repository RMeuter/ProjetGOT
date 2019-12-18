package ProjetGOT;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;

public class CalibrageCouleur {
	/*
	 * Calcul de la distance euclidienne :
	 * https://www.robotshop.com/community/forum/t/tcs3200-color-sensor-with-k-nearest-neighbor-classification-algorithm/13554
	 * 
	 * */

	private float [][] tabCouleurCalibre = new float [6][3];
	private static String [] nomCouleur = new String [] {"noir", "rouge", "orange", "vert", "blanc", "bleu"};
	private static EV3ColorSensor senseurCouleur;
	

	public CalibrageCouleur (EV3ColorSensor senseurColor) {
		CalibrageCouleur.senseurCouleur = senseurColor;
	}


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
	
	// Cette méthode permet d'obtenir les codes RGB de la couleur captée lors du calibrage
	private static float [] returnColorRGB() {
		float[] sample = new float[3];
		SampleProvider meanColorPercep = new MeanFilter(senseurCouleur.getRGBMode(),5);// 0 est le numero de la case
		meanColorPercep.fetchSample(sample, 0);
		for(int i=0; i<=2; i++) {
				sample[i] = sample[i];
			}
		return sample;
	}
	

	protected String getCalibreColor() {
		float[] sample = new float[3];
		SampleProvider meanColorPercep = new MeanFilter(senseurCouleur.getRGBMode(),5);// 0 est le numero de la case
		meanColorPercep.fetchSample(sample, 0);
		float base = (float) 0.03;
		for(int i=0; i<tabCouleurCalibre.length; i++) {
			if (sample[0]>-base+tabCouleurCalibre[i][0] && sample[0]<base+tabCouleurCalibre[i][0]
					&& sample[1]>-base+tabCouleurCalibre[i][1] && sample[1]<base+tabCouleurCalibre[i][1]
					&& sample[2]>-base+tabCouleurCalibre[i][2] && sample[2]<base+tabCouleurCalibre[i][2] ) {
				return nomCouleur[i];
			}
		}
		return "Aucune couleur";
	}
	
	//########################### Nouvelle méthode #######################################
	
	protected String getNewCalibreColor() {
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
	
	private float distanceEuclidienne (float [] sample, int i) {
		double nb = (float) (Math.pow((sample[0]-tabCouleurCalibre[i][0]),2) + 
				Math.pow((sample[1]-tabCouleurCalibre[i][1]),2) +
				Math.pow((sample[2]-tabCouleurCalibre[i][2]),2));
		return (float) Math.sqrt(nb);
	}

}