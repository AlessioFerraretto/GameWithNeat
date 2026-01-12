package game;

public enum EnemyType {

	SPIKES("resources\\spikes.png", 1, TouchType.DIE, Block.BLOCK_SIZE, 20),
	GHOST("resources\\ghost.png", 2, TouchType.DIE, Block.BLOCK_SIZE, Block.BLOCK_SIZE),
	WIN("resources\\win.png", 3, TouchType.WIN, Block.BLOCK_SIZE, Block.BLOCK_SIZE),
	FAKE_GROUND_TOUCH("resources\\ground.png", 4, TouchType.TOUCH, Block.BLOCK_SIZE, Block.BLOCK_SIZE),
	FAKE_GROUND_NO_TOUCH("resources\\ground.png", 5, TouchType.NO_TOUCH, Block.BLOCK_SIZE, Block.BLOCK_SIZE);

	protected int value, height, width;
	protected String img;
	protected TouchType touchType;
	
	EnemyType(String s, int v, TouchType tt, int w, int h) {
		this.img = s;
		value = v;
		touchType = tt;
		width = w;
		height = h;
	}

	public String getImg() {
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

	int getWidth() {
		return width;
	}
	
}
