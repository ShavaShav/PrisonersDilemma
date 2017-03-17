package model.tool;

import java.util.ArrayList;
import java.util.Comparator;

public class Population {
	private ArrayList<Lookup> population;
	ArrayList<IndividualFitness> sortedPopFitness;
	private Lookup bestIndividual;
	private double bestScore;
	int alreadyChosenIndex = -1;
	static double BIAS_TOWARDS_FITNESS = 0.5; // 0 -> 1 where 0 is only strongest, and 1 is random distribution
	
	// initializes to an empty pop
	public Population(){
		population = new ArrayList<Lookup>();
	}
	
	public void add(Lookup individual){
		population.add(individual);	
	}

	// only call once population has been filled!
	// creates a second sorted array, so that best individuals get chosen more often in the rando select
	public void judge(){
		// sort population according to fitness, largest is at index 0
		sortedPopFitness = new ArrayList<IndividualFitness>();
		for (Lookup individual : population){
			IndividualFitness individualFitness = new IndividualFitness(individual, individual.getScore());
			sortedPopFitness.add(individualFitness);
		}
		sortedPopFitness.sort(new Comparator<IndividualFitness>(){
			@Override
			public int compare(IndividualFitness a, IndividualFitness b) {
				if (a.score > b.score)
					return -1;
				else if (a.score < b.score)
					return 1;
				else
					return 0;
			}
		});
		bestIndividual = sortedPopFitness.get(0).individual;
		bestScore = sortedPopFitness.get(0).score;
		// log sorted member values
		int i = 1;
		for (IndividualFitness fitness : sortedPopFitness){
			Logging.print("Individual " + i++ +" : " + fitness.individual.getActionString());	
			Logging.println(" Score: " + fitness.score);
		}
	}
	
	// select a random individual, leaning towards better scoring tables
	public Lookup randomSelection(){
		int randI = pickRandomBiasedI(sortedPopFitness.size()-1, 0);
		// chose a random individual, if their calculated probability is higher
		while ((randI == alreadyChosenIndex)){ // cant be same as father
			randI = pickRandomBiasedI(sortedPopFitness.size()-1, 0);
		}
		if (alreadyChosenIndex == -1){
			alreadyChosenIndex = randI; // set father to selection
			Logging.print("Mating " + randI);
		}			
		else {
			alreadyChosenIndex = -1; // reset father to default
			Logging.print(" and " + randI + " : ");
		}
//		System.out.println(randI);
		return population.get(randI);
	}
	
	public Lookup getBestIndividual(){
		if (bestIndividual == null)
			judge();
		return bestIndividual;
	}
	
	public double getBestScore(){
		if (bestScore == 0.0)
			judge();
		return bestScore;
	}
	
	private int pickRandomBiasedI(int min, int max){
		// chose a random number between 0 and 1
		double randProb = Math.random();
		// pick a random index, bias towards stronger individuals
	    randProb = Math.pow(randProb, BIAS_TOWARDS_FITNESS);
	    return (int) (min + (max - min) * randProb);
	}
	
	private class IndividualFitness {
		Lookup individual;
		double score;
		public IndividualFitness(Lookup individual, double score){
			this.individual = individual;
			this.score = score;
		}
	}
	
	private void printSkewGraph(){
		int[] array = new int[10];
		for (int i = 0; i < 1000; i++) {
			int randI = pickRandomBiasedI(10,0);
			array[randI]++;
		}
		for (int i = 0; i < 10; i++) {
			System.out.println(i + " : " + array[i]);
		}
	}
	
	
	public static void main(String[] args) {
		Logging.ON = false;
		Population pop = new Population();
		for (int i = 0; i < 10; i++){
			LookupArray2D lookup = new LookupArray2D(3, 3);
			lookup.randomize();
			pop.add(lookup);
		}
		pop.judge(); // sets scores and probabilities
		
		System.out.println("Lookup A: \n" + pop.population.get(0).getActionString() + "\nLookup B: \n" + pop.population.get(1).getActionString());
		Lookup child = pop.population.get(0).makeChild(pop.population.get(1));
		System.out.println("Child of A & B:" );
		System.out.println(child.getActionString());
		
		// Testing skew
		pop.printSkewGraph();
		
	}

}
