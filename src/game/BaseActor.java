package game;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class BaseActor {

	static final int MIN_XY_VALUE = 0;
	static final int MAX_XY_VALUE = 500;
	
	private Stage stage;
	private int x, y, width, height;
	private TouchType touchType;
	private BlockType blockType;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public TouchType getTouchType() {
		return touchType;
	}

	public void setTouchType(TouchType touchType) {
		this.touchType = touchType;
	}

	public BlockType getBlockType() {
		return blockType;
	}

	public void setBlockType(BlockType type) {
		this.blockType = type;
	}

	public boolean collidesWith(Rectangle o) {
		return getBounds().intersects(o.getBounds());
	}
	
	public boolean collidesWith(Actor a) {
		return getBounds().intersects(a.getBounds());
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x,y,width,height);
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public String toString() {
		return "x: " + getX() + "\ty: " + getY() + "\tw: " + getWidth() + "\th: " + getHeight() + "\ttype: " + getBlockType() +"\ttouchType: " + getTouchType();
	}
	
	protected double getDistance(BaseActor o) {
		Point t = new Point((int) getRectangle().getCenterX(),(int) getRectangle().getCenterY());
		return t.distance(new Point((int) o.getRectangle().getCenterX(),(int) o.getRectangle().getCenterY()));
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}

	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}

}
