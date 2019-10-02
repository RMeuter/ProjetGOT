package ProjetGOT;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.utility.Delay;

public class CalibrageColor {
	private float [][] calibreColor = new float [6][3];
	private static String [] nom = new String [] {"Rouge", "Orange", "vert", "blanc", "noir", "bleu"};
	private EV3ColorSensor color;
	

	public CalibrageColor (EV3ColorSensor color) {
		this.color=color;
		Calibrage(this.calibreColor, this.nom, color);
	}
	
	public static void Calibrage (float [][] calibreColor, String[] nom, EV3ColorSensor color) {
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
	
	public static int testColorRGB(EV3ColorSensor color, float[][] calibreColor) {
		float[] sample = new float[3];
		color.getRGBMode().fetchSample(sample, 0);// 0 est le numero de la case
		float base = (float) 0.02;
		for(int i=0; i<calibreColor.length; i++) {
			if (sample[0]>-base+calibreColor[i][0] && sample[0]<base+calibreColor[i][0]
					&& sample[1]>-base+calibreColor[i][1] && sample[1]<base+calibreColor[i][1]
					&& sample[2]>-base+calibreColor[i][2] && sample[2]<base+calibreColor[i][2] ) {
				return i;
			}
		}
		return -1;
	}

}
