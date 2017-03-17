package controller;

import model.tool.Logging;
import model.tool.Lookup;
import model.tool.LookupArray1D;
import model.tool.LookupArray2D;
import model.tool.Population;

// genetic algorithm
public class GA {
	
	private boolean singleLookup;
	private int history1Length;
	private int history2Length;
	public static double MUT_PROB = 0.1;
	private Lookup bestIndividual;
	private boolean evolved;
	public static boolean DEBUG = false;
	
	// can change the type once constructed
	public GA(int historyLength){
		singleLookup = true;
		history1Length = historyLength;
		evolved = false;
	}
	
	// must make a new GA if you want to use 2D
	public GA(int history1Length, int history2Length){
		singleLookup = false;
		this.history1Length = history1Length;
		this.history2Length = history2Length;
		evolved = false;
	}
	
	public boolean isEvolved() { return evolved; }
	
	// evolves the current population and returns the best lookup
	public Lookup evolve(int populationSize, int numGenerations, int numChildrenPerCouple){
		if (numChildrenPerCouple < 1 || numChildrenPerCouple > 2){
			System.out.println("Only 1 or 2 children allowed per couple");
			return null;
		}
		
		Logging.println("Starting Evolution!");
		Logging.println(" Pop. Size:\t" + populationSize);
		Logging.println(" Generations:\t" + numGenerations);
		Logging.println(" Child/Couple:\t" + numChildrenPerCouple);
		Logging.println(" MutationProb:\t" + MUT_PROB);
		Logging.println(" SingleLookup?:\t" + singleLookup);
		
		Population population = getInitialPopulation(populationSize); // random individuals
		for (int gen = 1; gen <= numGenerations; gen++){
			
			Logging.println("***Generation***"+ gen);
			
			if (DEBUG) System.out.println("Generation " + gen + ": Best Score: " + population.getBestScore());
			
			Population nextGeneration = new Population(); // empty population
			// create a new population from the current one
			for (int i = 0; i < populationSize; i++){
				
				// select two members, leaning towards strong ones
				Lookup father = population.randomSelection();
				Lookup mother = population.randomSelection();
				if (numChildrenPerCouple == 1){
					// reproduce
					Lookup child = father.makeChild(mother);
					
					// mutate child with small probability
					if (Math.random() < MUT_PROB){
						Logging.print(("(M) ")); // mutation
						child.flipRandomAction();
					}
					
					Logging.println(child.getActionString() + " Score " + child.getScore());
					
					// add child to new population
					nextGeneration.add(child);
				} else { // 2 children
					// reproduce
					Lookup.LookupPair children = father.makeChildren(mother);
					
					// mutate children with small probability
					if (Math.random() < MUT_PROB){
						Logging.print("(M) ");
						children.brother.flipRandomAction();
					}
					
					Logging.print(children.brother.getActionString() + " Score " + children.brother.getScore() + " and ");
					
					if (Math.random() < MUT_PROB){
						Logging.print("(M) ");						
						children.sister.flipRandomAction();
					}
					
					Logging.println(children.sister.getActionString() + " Score " + children.sister.getScore());
					
					// add children to new population
					nextGeneration.add(children.brother);
					i++;
					if (i < populationSize)
						nextGeneration.add(children.sister);
				}
			}
			// replace population
			population = nextGeneration;
			population.judge(); // score members so can select the strongest randomly
		}
		evolved = true;
		// return the best individual in the final population
		bestIndividual = population.getBestIndividual();
		return bestIndividual;
	}
	
	// creates a new random population of lookup tables
	private Population getInitialPopulation(int numIndividuals){
		Population pop = new Population();
		// generate new random indiviuals
		if (singleLookup) { // 1D array lookup
			for (int i = 0; i < numIndividuals; i++){
				LookupArray1D individual = new LookupArray1D(history1Length);
				individual.randomize();
				pop.add(individual);
			}
		} else {
			for (int i = 0; i < numIndividuals; i++){
				LookupArray2D individual = new LookupArray2D(history1Length, history2Length);
				individual.randomize();
				pop.add(individual);
			}
		}
		pop.judge(); // score members so can select the strongest randomly
		return pop;
	}
}
