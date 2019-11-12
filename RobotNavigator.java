package ProjetGOT;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;

public class RobotNavigator extends Robot {
	// Feature robot
	private boolean isSauvageon = false;
	private int biaisAngle = 0; // definit un angle pour lequel le robot semble tourner correctement;
	private int [] step = new int [2];
	
	/*Nos differents steps :
	 * - Le choix de l'etape
	 * - Le scalaire du biais angle
	 * */
	
	// Where I AM
	private int [] positionHistorique = new int[2]; //sur la carte avec coordonn�es [x, y]
	private int [] goal = new int [2]; // [x, y]
	private int Cap; //entre -180 et 180 -> la rotation
	
	// Recupere un robot et une carte
	private Carte carte;
	
	public RobotNavigator (int newBiaisAngle,int choixEtape) {
		step[1]=1/4;
		step[0]= choixEtape;
		defineCamp();
		setDebut();
		setGoal(step[0]);
		
		this.biaisAngle = newBiaisAngle;
	}

	
	public void doRot () {
		int rot = getRotate();
		if(rot<=90||rot>=-90) pilot.rotate(rot);
		else {
			pilot.rotate(rot/2);
			while (verifyBlack(true))
				pilot.backward();
			pilot.rotate(rot/2);
			while (verifyBlack(true))
				pilot.backward();
		}
		verifTravelRight();
	}
	
	/*
	 * Tu verifie que tu avance droit sans continuité sur la ligne noir
	 * 
	 * 
	 * 
	 * */
	
	public void verifTravelRight () {
		pilot.travel(20);
		if(verifyBlack(true)) {
			pilot.rotate(biaisAngle*step[1]);
			step[1]=-step[1]/2;
		}
	}
	
	
	
	
	
	// ################################################# Verify color ###################################
	
	public boolean verifyBlack(boolean isLigne) {
		if (isLigne) return getCalibrateColor().getCalibreColor()=="noir";
		else return getCalibrateColor().getCalibreColor()!="noir";
	}
	
	
	// ################################################# Definition des rotations #######################
	
	public int findNewPositionDynamique() {
		/*
		 * Return une nouvelle position dynamique en degre
		 * Cette position est calculer entre la position historique et le but.
		 * On recupere le x s'il est different de 0 sinon le y.
		 * 
		 * Pourquoi juste le x xor y ? car on ne traverse pas en diagonal !
		 * */
		int newCap;
		if (goal[0]-positionHistorique[0]!=0) {
			if (goal[0]-positionHistorique[0]<0 && positionHistorique[0]-1>=0) {
				newCap = 90;
				positionHistorique[0]-=1;
			} else {
				newCap = 270;
				positionHistorique[0]+=1;
			}
		} else {
			if (goal[1]-positionHistorique[1]<0 && positionHistorique[0]-1>=0) {
				newCap = 180;
				positionHistorique[1]-=1;
			} else {
				newCap = 0;
				positionHistorique[1]+=1;
			}
		}
		return newCap;
	}

	
	public int getRotate() {
		// Définit la rotation entre -180 et 180 degres
		// Redefinit le cap à l'angle calculer
		// Donne une rotation en fonction du biais angulaire du robot
		int newCap = findNewPositionDynamique();
		int rotate = newCap - Cap;
		Cap = newCap;
		
		while (rotate>=180) rotate -= 360;
		while (rotate<=-180) rotate += 360;
		int newBiais = (rotate < 0 ? -biaisAngle: (rotate == 0 ? 0 : biaisAngle));
		newBiais = (rotate == 180 || rotate == -180? newBiais*2: newBiais);
		return rotate + newBiais;
	}
	
	// ################################ Trouver et tester une position dans l'espace ##############################
	
	public void setDebut(){
		/*
		 * true = sauvageon
		 * false = garde de nuit
		 */
		if (this.isSauvageon){
			positionHistorique = new int [] {4, 0};
		}else {
			positionHistorique= new int [] {0, 6};
		}
	}
	
	
	public boolean isArriveGoal() {
		// verification du robot qui est arriver au but donnée
		return goal[0] == positionHistorique[0] && goal[1] == positionHistorique[1];
	}
	
	public void setGoal(int step){
		/*
		 * Le but change au fur et � mesure de la partie il faut donc le red�finir � chaque fois
		 * true = sauvageon
		 * false = garde de nuit
		 */
		this.goal = new int[2];
		if (this.isSauvageon && step == 1){
			goal = new int [] {0, 0};
		}else if (!this.isSauvageon && step == 1) {
			goal = new int [] {3, 5};
		} else if (this.isSauvageon && step == 2) {
			goal = new int [] {0, 6};
		} else {
			goal = new int [] {4, 0};
		}
	}
	//##################################### fonction quelconque ############################################
	
	public void setPositionHistorique(int[] positionHistorique) {
		this.positionHistorique = positionHistorique;
	}
	
	public int[] getPositionHistorique() {
		return positionHistorique;
	}
	
	public void addOneMoreMission () {
		step[0] += 1 ;
	}
	
	// ################################# Define Camps
	
	public void defineCamp () {
		LCD.drawString("Choisis ton camp", 0, 0);
		LCD.drawString("H/G pour Sauvageons", 0, 1);
		LCD.drawString("B/reste pour garde de nuit", 0, 2);
		LCD.drawString("Les boutons de coté D/G sont pour le bleutooth et autre ss bluetooth ", 0, 3);
		
		switch (Button.waitForAnyPress()) {
			case Button.ID_RIGHT:  
				carte = new Carte(false);
				LCD.drawString("Garde de la nuit", 0, 0);
				//bl = new Bluetooth(false);
				break;
			case Button.ID_DOWN:
				carte = new Carte(false);
				LCD.drawString("Garde de la nuit", 0, 0);
				break;
			case Button.ID_LEFT:
				carte = new Carte(true);
				LCD.drawString("Sauvageon", 0, 0);
				//bl = new Bluetooth(true);
				break;
			default:
				carte = new Carte(true);
				LCD.drawString("Sauvageon", 0, 0);
				break;
		}
		Delay.msDelay(300);
		LCD.clear();
	}
	


}
