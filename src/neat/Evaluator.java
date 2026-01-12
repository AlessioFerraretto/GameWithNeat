package neat;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Evaluator implements Serializable {

	private FitnessGenomeComparator fitComp = new FitnessGenomeComparator();

	protected Counter nodeInnovation;
	protected Counter connectionInnovation;

	private List<Genome> genomes;
	private List<FitnessGenome> fitnessGenomes;
	private List<Genome> nextGenGenomes;

	private List<Species> species;

	private Map<Genome, Species> mappedSpecies;
	private Map<Genome, Float> scoreMap;
	private Float highestScore = 0f;
	private Integer stagnation = 0;
	private Genome fittestGenome;
	int populationSize;

	public Evaluator(int populationSize, Genome startingGenome, Counter nodeInnovation, Counter connectionInnovation) {
		this.populationSize = populationSize;
		this.nodeInnovation = nodeInnovation;
		this.connectionInnovation = connectionInnovation;

		genomes = new ArrayList<Genome>(populationSize);
		for (int i = 0; i < populationSize; i++) {
			genomes.add(new Genome(startingGenome));
		}
		species = new Vector<Species>();
	}

	/**
	 * Runs one generation
	 */
	public void score(ScoreInterface scoreInterface) {
		stagnation++;

		// Reset everything for next generation
		for (Species s : species) {
			s.reset();
		}
		scoreMap = new HashMap<Genome, Float>();
		mappedSpecies = new HashMap<Genome, Species>();
		nextGenGenomes = new LinkedList<Genome>();
		fitnessGenomes = new LinkedList<FitnessGenome>();

		// Place genomes into species
		int added = 0;
		for (Genome g : genomes) {
			boolean foundSpecies = false;
			for (Species s : species) {
				if (Genome.compatibilityDistance(g, s.mascot, NeatConfiguration.C1, NeatConfiguration.C2, NeatConfiguration.C3) < NeatConfiguration.DT) { // compatibility distance is less than DT, so genome belongs to this species
					s.members.add(g);
					mappedSpecies.put(g, s);
					foundSpecies = true;
					break;
				}
			}
			if (!foundSpecies) { // if there is no appropiate species for genome, make a new one
				Species newSpecies = new Species(g);
				species.add(newSpecies);
				mappedSpecies.put(g, newSpecies);
				added++;
			}
		}
		//System.out.println("Added "+added+" species");

		// Remove unused species
		removeEmptySpecies();

		// Evaluate genomes and assign score
		for (int i=0;i<genomes.size();i++) {
			Genome g = genomes.get(i);
			Species s = mappedSpecies.get(g);		// Get species of the genome

			float score = scoreInterface.evaluateGenome(g, i);
			float adjustedScore = score / mappedSpecies.get(g).members.size();

			FitnessGenome fitGenome = new FitnessGenome(g, adjustedScore);

			s.addAdjustedFitness(adjustedScore);	
			s.fitnessPop.add(fitGenome);

			fitnessGenomes.add(fitGenome);

			scoreMap.put(g, adjustedScore);
			if (score >= highestScore) {
				stagnation = 0;
				highestScore = score;
				fittestGenome = g;
			}
		}

		// put best genomes from each species into next generation
		nextGenGenomes.add(fittestGenome);
		for (Species s : species) {
			if (s.members.size() > 5) {
				Collections.sort(s.fitnessPop, fitComp);
				Collections.reverse(s.fitnessPop);
				FitnessGenome fittestInSpecies = s.fitnessPop.get(0);
				//System.out.println("Adding genome with fitness: "+fittestInSpecies.fitness);
				nextGenGenomes.add(fittestInSpecies.genome);
			}
		}

		if (stagnation > 40) {
			System.out.println("CLEANING UPUUUP!");
			Collections.sort(fitnessGenomes, fitComp);
			Collections.reverse(fitnessGenomes);
			for (int i = 5; i < fitnessGenomes.size(); i++) {
				FitnessGenome fg = fitnessGenomes.get(i);
				genomes.remove(fg.genome);
				for (Species s : species) {
					s.members.remove(fg.genome);
					s.fitnessPop.remove(fg);
					mappedSpecies.remove(fg.genome);
					scoreMap.remove(fg.genome);
				}
			}
			removeEmptySpecies();
			stagnation = 0;
		}

		// Breed the rest of the genomes
		while (nextGenGenomes.size() < populationSize) { // replace removed genomes by randomly breeding
			Species s = getRandomSpeciesBiasedAjdustedFitness();

			Genome p1 = getRandomGenomeBiasedAdjustedFitness(s);
			Genome p2 = getRandomGenomeBiasedAdjustedFitness(s);

			if (RandomSingleton.nextFloat() < 0.2f) {
				if (scoreMap.get(p1) >= scoreMap.get(p2)) {
					Genome mutated = new Genome(p1);
					mutated.mutation();
					nextGenGenomes.add(mutated);
				} else {
					Genome mutated = new Genome(p2);
					mutated.mutation();
					nextGenGenomes.add(mutated);
				}
			} else {
				Genome child;
				if (scoreMap.get(p1) >= scoreMap.get(p2)) {
					child = Genome.crossover(p1, p2);
				} else {
					child = Genome.crossover(p2, p1);
				}
				if (RandomSingleton.nextFloat() < NeatConfiguration.MUTATION_RATE) {
					child.mutation();
				}
				if (RandomSingleton.nextFloat() < NeatConfiguration.ADD_CONNECTION_RATE) {
					//System.out.println("Adding connection mutation...");
					child.addConnectionMutation(connectionInnovation, 10);
				}
				if (RandomSingleton.nextFloat() < NeatConfiguration.ADD_NODE_RATE) {
					//System.out.println("Adding node mutation...");
					child.addNodeMutation(connectionInnovation, nodeInnovation);
				}
				nextGenGenomes.add(child);
			}
		}

		genomes = nextGenGenomes;
		nextGenGenomes = new ArrayList<Genome>();
	}

	private void removeEmptySpecies() {
		Iterator<Species> iter = species.iterator();
		while(iter.hasNext()) {
			Species s = iter.next();
			if (s.members.isEmpty()) {
				iter.remove();
			}
		}
	}

	/**
	 * Selects a random species from the species list, where species with a higher total adjusted fitness have a higher chance of being selected
	 */
	private Species getRandomSpeciesBiasedAjdustedFitness() {
		double completeWeight = 0.0;	// sum of probablities of selecting each species - selection is more probable for species with higher fitness
		for (Species s : species) {
			completeWeight += s.totalAdjustedFitness;
		}
		double r = RandomSingleton.random() * completeWeight;
		double countWeight = 0.0;
		for (Species s : species) {
			countWeight += s.totalAdjustedFitness;
			if (countWeight >= r) {
				return s;
			}
		}
		throw new RuntimeException("Couldn't find a species... Number is species in total is "+species.size()+", and the total adjusted fitness is "+completeWeight);
	}

	/**
	 * Selects a random genome from the species chosen, where genomes with a higher adjusted fitness have a higher chance of being selected
	 */
	private Genome getRandomGenomeBiasedAdjustedFitness(Species selectFrom) {
		double completeWeight = 0.0;	// sum of probablities of selecting each genome - selection is more probable for genomes with higher fitness
		for (FitnessGenome fg : selectFrom.fitnessPop) {
			completeWeight += fg.fitness;
		}
		double r = RandomSingleton.random() * completeWeight;
		double countWeight = 0.0;
		for (FitnessGenome fg : selectFrom.fitnessPop) {
			countWeight += fg.fitness;
			if (countWeight >= r) {
				return fg.genome;
			}
		}
		throw new RuntimeException("Couldn't find a genome... Number is genomes in selected species is "+selectFrom.fitnessPop.size()+", and the total adjusted fitness is "+completeWeight);
	}

	public int getSpeciesAmount() {
		return species.size();
	}

	public float getHighestFitness() {
		return highestScore;
	}

	public Genome getFittestGenome() {
		return fittestGenome;
	}

	public class FitnessGenome implements Serializable {

		float fitness;
		Genome genome;

		public FitnessGenome(Genome genome, float fitness) {
			this.genome = genome;
			this.fitness = fitness;
		}
	}

	public class Species implements Serializable {

		public Genome mascot;
		public List<Genome> members;
		public List<FitnessGenome> fitnessPop;
		public float totalAdjustedFitness = 0f;

		public Species(Genome mascot) {
			this.mascot = mascot;
			this.members = new LinkedList<Genome>(); 
			this.members.add(mascot);
			this.fitnessPop = new ArrayList<FitnessGenome>(); 
		}

		public void addAdjustedFitness(float adjustedFitness) {
			this.totalAdjustedFitness += adjustedFitness;
		}

		/*
		 *	 Selects new random mascot + clear members + set totaladjustedfitness to 0f
		 */
		public void reset() {
			int newMascotIndex = RandomSingleton.nextInt(members.size());
			this.mascot = members.get(newMascotIndex);
			members = new LinkedList<Genome>();
			fitnessPop = new LinkedList<FitnessGenome>();
			totalAdjustedFitness = 0f;
		}
	}

	public class FitnessGenomeComparator implements Comparator<FitnessGenome>, Serializable {

		@Override
		public int compare(FitnessGenome one, FitnessGenome two) {
			if (one.fitness > two.fitness) {
				return 1;
			} else if (one.fitness < two.fitness) {
				return -1;
			}
			return 0;
		}

	}

	public List<Genome> getGenomes() {
		return genomes;
	}
}
