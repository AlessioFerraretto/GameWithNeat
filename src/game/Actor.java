package game;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public abstract class Actor extends BaseActor {

	private int movementSpeedX, movementSpeedY;
	protected int imageIndex;
	private ImageIcon imgRight, imgLeft, imgRightJumping, imgLeftJumping, imgUp, imgDown, imgIdle;
	private Direction d = Direction.RIGHT;
	boolean isJumping = false;

	public abstract void draw(Graphics g);

//	public void setX(int x) {
//		if(x>=0 && x<=Frame.WIDTH-getWidth()-5) {
//			super.setX(x);
//		}
//	}

	public ImageIcon getImgRight() {
		return imgRight;
	}

	public void setImgRight(ImageIcon imgRight) {
		this.imgRight = imgRight;
	}

	public ImageIcon getImgLeft() {
		return imgLeft;
	}

	public void setImgLeft(ImageIcon imgLeft) {
		this.imgLeft = imgLeft;
	}

	public ImageIcon getImgUp() {
		return imgUp;
	}

	public void setImgUp(ImageIcon imgUp) {
		this.imgUp = imgUp;
	}

	public ImageIcon getImgDown() {
		return imgDown;
	}

	public void setImgDown(ImageIcon imgDown) {
		this.imgDown = imgDown;
	}

	public int getMovementSpeedX() {
		return movementSpeedX;
	}

	public void setMovementSpeedX(int movementSpeedX) {
		this.movementSpeedX = movementSpeedX;
	}

	public int getMovementSpeedY() {
		return movementSpeedY;
	}

	public void setMovementSpeedY(int movementSpeedY) {
		this.movementSpeedY = movementSpeedY;
	}

	public Image getImage() {
		switch(d) {
		case RIGHT:
			if(isJumping) {
				return imgRightJumping.getImage();
			} else {
				return imgRight.getImage();
			}
		case LEFT:
			if(isJumping) {
				return imgLeftJumping.getImage();
			} else {
				return imgLeft.getImage();
			}
		case UP:
			return imgUp.getImage();
		case DOWN:
			return imgDown.getImage();
		case IDLE:
			return imgIdle.getImage();
		}

		return null;
	}

	protected void setDirection(Direction d) {
		this.d = d;

	}

	public ImageIcon getImgRightJumping() {
		return imgRightJumping;
	}

	public void setImgRightJumping(ImageIcon imgRightJumping) {
		this.imgRightJumping = imgRightJumping;
	}

	public ImageIcon getImgLeftJumping() {
		return imgLeftJumping;
	}

	public void setImgLeftJumping(ImageIcon imgLeftJumping) {
		this.imgLeftJumping = imgLeftJumping;
	}

	public ImageIcon getImgIdle() {
		return imgIdle;
	}

	public void setImgIdle(ImageIcon imgIdle) {
		this.imgIdle = imgIdle;
	}

	public Direction getD() {
		return d;
	}

	public void setD(Direction d) {
		this.d = d;
	}
	

	public abstract void tick();

	protected abstract void incrementImageIdex();
	
}
