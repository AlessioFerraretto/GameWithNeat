package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Mario extends Actor {

	private double minDistance=9999;
	private Enemy win;
	
	private int iconX, iconY, iconWidth, iconHeight;
	private int jumpCounter = 0;
	private int tickPerJump = 20;
	private int tickAirboneJump = 5;
	private long lastTimeJump = 0;
	private long timeWaitAfterJump = 30000 / GameThread.FPS ;
	private GameState gameState = GameState.PLAYING;

	public Mario(Stage s) {
		setStage(s);
		setImgRight(new ImageIcon("resources\\marioRight.png"));
		setImgRightJumping(new ImageIcon("resources\\marioRightJumping.png"));
		setImgLeft(new ImageIcon("resources\\marioLeft.png"));
		setImgLeftJumping(new ImageIcon("resources\\marioLeftJumping.png"));
		setImgIdle(new ImageIcon("resources\\marioIdle.png"));
		setImgUp(new ImageIcon("resources\\marioUp.png"));


		setMovementSpeedX(7);
		setMovementSpeedY(10);

		iconX = 0;
		iconY = 0;
		iconWidth = 28;
		iconHeight = 33;

		setWidth((int) (iconWidth*1.5));
		setHeight((int) (iconHeight*1.5));
	}
	public Mario(Stage s ,Enemy win) {
		this(s);
		this.win = win;
		
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(getImage(),getX(),getY(),getX()+getWidth(),getY()+getHeight(), iconX + iconWidth*imageIndex, iconY, iconX + iconWidth*(imageIndex+1), iconHeight, null, null);

		if(Debug.DEBUG) {
			g.setColor(Color.red);
			g.drawRect(getX(),getY(), getWidth(), getHeight());
		}
	}

	protected void incrementImageIdex() {
		imageIndex++;
		imageIndex = imageIndex % 8;
	}

	public void idle() {
		if(!isJumping) {
			setDirection(Direction.IDLE);
		}
	}

	public void moveRight() {
		setDirection(Direction.RIGHT);
		incrementImageIdex();
		trytoMove(new Rectangle(getX() + getMovementSpeedX(), getY(), getWidth(), getHeight()), Direction.RIGHT);

		if(getDistance(win) < minDistance) {
			minDistance = getDistance(win);
		}
	}

	public void moveLeft() {
		setDirection(Direction.LEFT);
		incrementImageIdex();
		trytoMove(new Rectangle(getX() - getMovementSpeedX(), getY(), getWidth(), getHeight()), Direction.LEFT);
	}

	public void jump() {
		Rectangle test = new Rectangle(getX(), getY() + 5, getWidth(), getHeight());

		boolean ok = false;
		for(BaseActor a : getStage().getEntities()) {
			if(a.collidesWith(test)) {
				if(a.getTouchType().equals(TouchType.TOUCH)) {
					ok = true;
				}
			}
		}

		if(lastTimeJump < System.currentTimeMillis() - timeWaitAfterJump) {
			ok &= true;
		} else {
			ok = false;
		}

		if(!isJumping && ok) {
			isJumping = true;
			lastTimeJump = System.currentTimeMillis();
			if(getD() == Direction.IDLE) {
				setDirection(Direction.RIGHT);
			}
			jumpCounter = 0;
		}

	}

	private boolean trytoMove(Rectangle t, Direction d) {
		Rectangle test = t;
		int yTopTouch = 0;
		int yBotTouch = 0;
		int xLeftTouch = 0;
		int xRightTouch = 01;

		boolean ok = true;
		for(BaseActor a : getStage().getEntities()) {
			if(a.collidesWith(test)) {
				if(a.getTouchType().equals(TouchType.TOUCH)) {
					yTopTouch = a.getY() - getHeight();
					yBotTouch = a.getY() + a.getHeight();
					xLeftTouch = a.getX() - getWidth();
					xRightTouch = a.getX()  + a.getWidth();
					ok = false;
					break;
				}
			}
			if(!ok) { break; }
		}

		if(ok) {
			setX((int) t.getX());
			setY((int) t.getY());
			setWidth((int) t.getWidth());
			setHeight((int) t.getHeight());
		}
		else {
			if(!isJumping) 
				switch(d) {
				case RIGHT:
					setX(xLeftTouch);
					break;
				case LEFT:
					setX(xRightTouch);
					break;
				case UP:
					setY(yBotTouch);
					break;
				case DOWN:
					setY(yTopTouch);
					break;

				}
		}

		return ok;

	}

	public void tick() {
		if(isJumping) {
			if(jumpCounter<=tickPerJump) {
				if(!trytoMove(new Rectangle(getX(), getY() - getMovementSpeedY(), getWidth(), getHeight()),Direction.UP)) {
					jumpCounter = tickPerJump+1;
				}
			}
			else if(jumpCounter<tickPerJump*2 + tickAirboneJump) {
				if(!trytoMove(new Rectangle(getX(), getY() + getMovementSpeedY(), getWidth(), getHeight()),Direction.DOWN)) {
					jumpCounter = 0;
					isJumping = false;
				}
			}
			else {
				jumpCounter = 0;
				isJumping = false;
			}
			jumpCounter++;
		} else {
			trytoMove(new Rectangle(getX(), getY()  + getMovementSpeedY(), getWidth(), getHeight()),Direction.DOWN);
		}

	}

	public void setState(GameState s) {
		gameState = s;

	}

	public GameState getGameState() {
		return gameState;
	}

	//questo metodo viene usato per la fitness function dell'ia e ritorna
	//la minima distanza mai raggiunta dalla vincita
	//non è il medoto migliore di valutare un'ia ma per ora ce lo facciamo andare bene
	public int getMinDistanceFromWin() {
		return (int) minDistance;
	}
	public void setWin(Enemy win) {
		this.win = win;
	}


}