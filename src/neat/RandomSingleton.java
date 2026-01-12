package neat;
import java.util.Random;

public class RandomSingleton {

	private RandomSingleton() {}
	
	private static Random r;
	
	static {
		 r = new Random();
		 r.setSeed(1);
	}
	
	public static double random() {
		return r.nextDouble();
	}
	
	public static Double getRandDouble(int from, int to) {
		return r.nextDouble() * (to - from) + from;
	}

	public static float getRandFloat(int from, int to) {
		return r.nextFloat() * (to - from) + from;

	}

	public static int nextInt(int a) {
		return r.nextInt(a);
	}

	public static float nextFloat() {
		return r.nextFloat();
	}

	public static boolean nextBoolean() {
		return r.nextBoolean();
	}

}
