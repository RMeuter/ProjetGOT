package ProjetGOT;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;

public class CalibrageColor {
	private float [][] calibreColor = new float [6][3];
	private static String [] nom = new String [] {"rouge", "orange", "vert", "blanc", "bleu", "noir"};
	
	
	public void Calibrage (EV3ColorSensor color) {
			for (int i = 0;i<nom.length;i++ ) {
				LCD.drawString(("Couleur : "+nom[i]), 0, 0);
				Button.waitForAnyPress();
				if (Button.DOWN.isDown()) {
					calibreColor[i] = returnColorRGB(color);
				}
				LCD.clear();
			}
	}
	
	public static float [] returnColorRGB(EV3ColorSensor color) {
		float[] sample = new float[3];
			color.getRGBMode().fetchSample(sample, 0);// 0 est le numero de la case
			for(int i=0; i<=2; i++) {
					sample[i] = sample[i];
				}
		return sample;
	}
	
	public String getCalibreColor(EV3ColorSensor color) {
		float[] sample = new float[3];
		color.getRGBMode().fetchSample(sample, 0);// 0 est le numero de la case
		float base = (float) 0.025;
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
