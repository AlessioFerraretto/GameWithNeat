package neat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import neat.NodeGene.TYPE;

public class Neat implements Serializable {

	static final String FILE_PATH = "saves/";
	static final String FILE_NAME = "neat";
	static final String FILE_EXTENSION = ".dat";
	static int INDEX = 0;
	Counter nodeInnovation, connectionInnovation;
	Evaluator eval;
	int generation = 0;


	public Neat(int inputs, int outputs, int size) {
		Genome genome = new Genome();
		nodeInnovation = new Counter();
		connectionInnovation = new Counter();
		for(int i=0;i<inputs;i++) {
			genome.addNodeGene(new NodeGene(TYPE.INPUT, nodeInnovation.getInnovation()));
		}
		for(int i=0;i<outputs;i++) {
			genome.addNodeGene(new NodeGene(TYPE.OUTPUT, nodeInnovation.getInnovation()));
		}

		for(int i=0;i<inputs;i++) {
			genome.addConnectionGene(new ConnectionGene(i,inputs+(i%outputs),RandomSingleton.getRandFloat(-1, 1),true, connectionInnovation.getInnovation()));	// conn id is 0
		}

		eval = new Evaluator(size, genome, nodeInnovation, connectionInnovation);

	}


	public void score(ScoreInterface scoreInterface) {
//		long t1 = System.currentTimeMillis();
		try {
			eval.score(scoreInterface);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		long t2 = System.currentTimeMillis();
//		System.out.println((t2-t1) / 1000 + "." + (t2-t1) %1000 + " time for score evaluation at generation: " + generation);
		generation++;
	}

	public ArrayList<Float[]> evaluate(ArrayList<ArrayList<Input>> in) {
		ArrayList<Float[]> result = new ArrayList<>();

		for(int i=0; i<eval.getGenomes().size(); i++) {
			result.add(new NeuralNetwork(eval.getGenomes().get(i)).evaluate(in.get(i)));
		}
		return result;
	}

	public Genome getFittest() {
		return eval.getFittestGenome();
	}

	public void save() {
//		long t1 = System.currentTimeMillis();
		try {
			FileOutputStream fileOut = new FileOutputStream(FILE_PATH + FILE_NAME + INDEX + FILE_EXTENSION);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		INDEX++;
//		long t2 = System.currentTimeMillis();
//		System.out.println((t2-t1) / 1000 + "." + (t2-t1) %1000 + " time to save generation: " + generation);
	
	}

	public static Neat load() {
		INDEX = findMaxSave();

		Neat n = null;
		try {
			FileInputStream fileIn = new FileInputStream(FILE_PATH + FILE_NAME + INDEX + FILE_EXTENSION);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			n = (Neat) in.readObject();
			in.close();
			fileIn.close();
		} catch (Exception e) {
		}
		return n;
	}

	private static int findMaxSave() {
		File folder = new File(FILE_PATH);
        File[] files = folder.listFiles();

        int highestNumber = 0;

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    int number = extractNumberFromFileName(file.getName());
                    highestNumber = Math.max(highestNumber, number);
                }
            }
        }

        return highestNumber;
	}
	
	 public static int extractNumberFromFileName(String fileName) {
	        // Assuming the number is at the end of the file name
	        String[] parts = fileName.split("[^0-9]+");

	        if (parts.length > 0) {
	            return Integer.parseInt(parts[parts.length - 1]);
	        } else {
	            return 0; // If no number is found, return 0
	        }
	    }

	public int getIndexOf(Genome g) {
		return eval.getGenomes().indexOf(g);
	}


	public int getGeneration() {
		return generation;
	}

}
