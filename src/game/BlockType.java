package game;

public enum BlockType {
	
	AIR("resources\\air.png", 0, TouchType.NO_TOUCH, Block.BLOCK_SIZE),
	GROUND("resources\\ground.png", 1, TouchType.TOUCH, Block.BLOCK_SIZE);

	protected int value, height;
	protected String img;
	protected TouchType touchType;
	protected String name;
	
	BlockType(String img, int v, TouchType tt, int h) {
		this.img = img;
		value = v;
		touchType = tt;
		height = h;
	}
	
	String getImg() {
		return img;
	}
	
	int getValue() {
		return value;
	}

	int getHeight() {
		return height;
	}
	
	public String toString() {
		return name() + "\t" + getValue() + "\t" + getTouch();
	}

	TouchType getTouch() {
		return touchType;
	}
}
