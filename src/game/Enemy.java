package game;
import java.awt.Graphics;

import javax.swing.ImageIcon;

import neat.Input;

public class Enemy extends Actor {
	
	protected EnemyMovement enemyMovement;
	int time = 0;

	public Enemy(Stage s, int x, int y, EnemyType et, EnemyMovement em) {
		this(s, et.getImg(), x, y, et, et.getTouch(), em);
	}

	public Enemy(Stage s, String img, int x, int y, EnemyType et, TouchType tt, EnemyMovement em) {
		setStage(s);

		setTouchType(tt);

		setImgRight(new ImageIcon(img));
		setX(x);

		setWidth(et.getWidth());
		setHeight(et.getHeight());

		setY(y + Block.BLOCK_SIZE - et.getHeight());

		setMovementSpeedX(0);
		setMovementSpeedY(0);
		enemyMovement = em;

	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(getImage(), getX(), getY(), getWidth(), getHeight(), null);

	}

	public void tick() {
		if(enemyMovement!=null) {
			enemyMovement.move(time);
		}
	}

	@Override
	public void incrementImageIdex() {
		// TODO Auto-generated method stub

	}

	public Input[] getData(Mario mario) {
		return new Input[] {new Input(getX() - mario.getX(),MIN_XY_VALUE,MAX_XY_VALUE), new Input(getY() - mario.getY(),MIN_XY_VALUE,MAX_XY_VALUE)};
	}
	
	public static int getDataSize() {
		return 2;
	}
}