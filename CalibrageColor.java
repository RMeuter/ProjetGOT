package ProjetGOT;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;

// https://lejosnews.wordpress.com/2015/01/28/sensor-calibration-a-bit-of-background/
public class CalibrageColor {
	private float [][] calibreColor = new float [6][3];
	private static String [] nom = new String [] {"noir", "rouge"};//, "orange", "vert", "blanc", "bleu"};
	private static EV3ColorSensor color;
	
	/*
	 * Faire un dictionnaire ou la clé est la couleur
	 *  et le nombre est l'identification de celle-ci dans la carte
	 * */
	
	public CalibrageColor (EV3ColorSensor color) {
		this.color = color;
	}
	
	public void Calibrage () {
		LCD.drawString("Calibrons", 0, 0);
		Button.waitForAnyEvent();
		LCD.clear();
		for (int i = 0;i<nom.length;i++ ) {
			LCD.drawString(("Couleur : "+nom[i]), 0, 0);
			Button.waitForAnyPress();
			if (Button.DOWN.isDown()) {
				calibreColor[i] = returnColorRGB();
			}
			LCD.clear();
		}
	}
	
	public static float [] returnColorRGB() {
		float[] sample = new float[3];
		SampleProvider meanColorPercep = new MeanFilter(color.getRGBMode(),5);// 0 est le numero de la case
		meanColorPercep.fetchSample(sample, 0);
		for(int i=0; i<=2; i++) {
				sample[i] = sample[i];
			}
		return sample;
	}
	
	public String getCalibreColor() {
		float[] sample = new float[3];
		SampleProvider meanColorPercep = new MeanFilter(color.getRGBMode(),5);// 0 est le numero de la case
		meanColorPercep.fetchSample(sample, 0);
		float base = (float) 0.035;
		for(int i=0; i<calibreColor.length; i++) {
			if (sample[0]>-base+calibreColor[i][0] && sample[0]<base+calibreColor[i][0]
					&& sample[1]>-base+calibreColor[i][1] && sample[1]<base+calibreColor[i][1]
					&& sample[2]>-base+calibreColor[i][2] && sample[2]<base+calibreColor[i][2] ) {
				return nom[i];
			}
		}
		return "Aucune couleur";
	}

}
