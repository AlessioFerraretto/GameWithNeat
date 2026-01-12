package game;
import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

public class StageOne extends Stage {

	public StageOne(int N) {
		super();
		setStage("maps/stage01.txt");
		setEnemies("maps/enemies01.txt");

		startingX = 150;
		startingY = 150;

		if(N!=0) {
			loadArrayMario(N);
		}
		else {
			loadMario();
		}

	}



}
