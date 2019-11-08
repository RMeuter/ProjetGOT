package ProjetGOT.TestConfig;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LCD.drawString("Commencez", 0, 0);
		Button.waitForAnyPress();
		LCD.clear();
		
		Wheel wheel1 = WheeledChassis.modelWheel(Motor.B, 56.).offset(-60);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.C, 56.).offset(60);
		Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, 2);	
		MovePilot pilot = new MovePilot(chassis);
		int Cap = 0;
		int biais = -13;
		int direction = 90;
		pilot.setLinearSpeed(40.);
		pilot.setAngularSpeed(40.);
		while (!Button.RIGHT.isDown()) {
			LCD.clear();
			Button.waitForAnyPress();
			if (Button.UP.isDown()) {
				biais+=1;
			} else if (Button.DOWN.isDown()){
				biais-=1;
			} else if (Button.LEFT.isDown()){
				direction=-direction;
			}
			LCD.drawInt(biais, 0, 0);
			pilot.rotate(getRotate(direction,Cap,biais));
		}		
		
	}
	public static int getRotate(int rot, int Cap, int biaisAngle) {
		// Définit la rotation entre -180 et 180 degres
		// Redefinit le cap à l'angle calculer
		// Donne une rotation en fonction du biais angulaire du robot
		int newCap = rot;
		int rotate = newCap - Cap;
		Cap = newCap;
		
		while (rotate>=180) rotate -= 360;
		while (rotate<=-180) rotate += 360;
		int newBiais = (rotate < 0 ? -biaisAngle: (rotate == 0 ? 0 : biaisAngle));
		newBiais = (rotate == 180 || rotate == -180? newBiais*2: newBiais);
		return rotate + newBiais;
	}

}
