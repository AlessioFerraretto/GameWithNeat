package game;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;

import neat.Input;

public class Block extends BaseActor {

	public static int BLOCK_SIZE = 50;
	ImageIcon img;

	public Block(int x, int y, BlockType bt, Stage s) {
		setStage(s);
		setBlockType(bt);
		setTouchType(bt.getTouch());
		setX(x);
		setY(y);
		setWidth(BLOCK_SIZE);
		setHeight(bt.getHeight());
		img = new ImageIcon(bt.getImg());
	}

	public void draw(Graphics g) {
		g.drawImage(img.getImage(), getX(), getY() + BLOCK_SIZE - getHeight(), BLOCK_SIZE, getHeight(), null);

//		if(debug) { 
//			g.setColor(Color.green);
//			g.drawRect(getX(), getY(), BLOCK_SIZE, BLOCK_SIZE);
//
//			for(Mario mario : getStage().getArrayMario()) {
//				if(this.collidesWith(mario)) {
//					g.fillRect(getX(), getY(), BLOCK_SIZE, BLOCK_SIZE);
//				}
//			}
//		}
		
	}
	
	
	public Input[] getData(Mario mario) {
		return new Input[] {new Input(getX() - mario.getX(),MIN_XY_VALUE,MAX_XY_VALUE), new Input(getY()  - mario.getY(),MIN_XY_VALUE,MAX_XY_VALUE), new Input(getTouchType().equals(TouchType.TOUCH))};

	}
	
	public static int getDataSize() {
		return 3;
	}
}
