package creator;

import javax.swing.JButton;

import game.Block;
import game.BlockType;
import game.Enemy;
import game.EnemyType;

public class Button extends JButton {

	public Button(BlockType b, SelectEvent se) {
		super(b.name());
		
		addActionListener(e->{
			se.selected(new Block(0,0,b,null));
		});
	}
	
	public Button(EnemyType b, SelectEvent se) {
		super(b.name());
		
		addActionListener(e->{
			se.selected(new Enemy(null,0,0,b,null));
		});
	}
}
