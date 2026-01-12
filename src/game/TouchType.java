package game;

public enum TouchType {

	NO_TOUCH(0),
	TOUCH(1),
	WIN(2),
	DIE(3);
	
	int value;
	TouchType(int t) {
		this.value = t;
	}
	
	int getValue() {
		return value;
	}
	
}
