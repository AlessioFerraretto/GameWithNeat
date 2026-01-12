package neat;

import game.TouchType;

public class Input {

	private float value;
	private float maxValue;
	private float minValue;
	
	private static final boolean normalize = true;
	
	public Input(int v, int minValue, int maxValue) {
		value = v;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public Input(boolean v) {
		this(v?-1:1, -1, 1);
	}

	public Input(TouchType touchType) {
		switch(touchType) {
		case DIE:
			value = 1;
			break;
		case NO_TOUCH:
			value = 2;
			break;
		case TOUCH:
			value = 3;
			break;
		}
		this.minValue = 0;
		this.maxValue = 3;
		
	}

	public float getValue() {
		if(normalize) {
			return (value - minValue - (maxValue-minValue)/2) / ((maxValue-minValue)/2);
		}
		
		
		return value;
	}
}
