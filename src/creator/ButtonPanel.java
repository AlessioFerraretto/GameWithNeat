package creator;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JPanel;

import game.BaseActor;
import game.BlockType;
import game.EnemyType;
import game.RefreshHandler;
import game.Stage;
import game.StageOne;

public class ButtonPanel extends JPanel {

	private Button[] buttonsBlock, buttonsEnemies;

	public ButtonPanel(Panel p) {
		super();
		
		setMaximumSize(new Dimension(1000,200));
		buttonsBlock = new Button[BlockType.values().length];
		buttonsEnemies = new Button[EnemyType.values().length];

		for(int i=0;i<buttonsBlock.length;i++) {
			buttonsBlock[i] = new Button(BlockType.values()[i], p);
			add(buttonsBlock[i]);
		}

		for(int i=0;i<buttonsEnemies.length;i++) {
			buttonsEnemies[i] = new Button(EnemyType.values()[i], p);
			add(buttonsEnemies[i]);
		}
	}

}
