package creator;

import javax.swing.JButton;

import game.Block;
import game.BlockType;
import game.Enemy;
import game.EnemyType;

public class Button extends JButton {

	public Button(BlockType b, PanelEvent pe) {
		super(b.name());

		addActionListener(e->{
			pe.selected(new Block(0,0,b,null));
		});
	}

	public Button(EnemyType b, PanelEvent pe) {
		super(b.name());

		addActionListener(e->{
			pe.selected(new Enemy(null,0,0,b,null));
		});
	}

	public Button(ButtonType b, PanelEvent pe) {
		super(b.name());

		addActionListener(e->{
			switch (b) {
			case PRINT_STAGE:
				pe.printStage();
				break;
			case PRINT_ENEMIES:
				pe.printEnemies();
			}
		});

	}

}
