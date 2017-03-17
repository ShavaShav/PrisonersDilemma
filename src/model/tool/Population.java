package model.tool;

import java.util.ArrayList;
import java.util.Comparator;

import controller.GA;

public class Population {
	private ArrayList<Lookup> population;
	ArrayList<IndividualFitness> sortedPopFitness;
	private Lookup bestIndividual;
	private double bestScore, worstScore, avgScore;
	int alreadyChosenIndex = -1;
	
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
		avgScore = 0.0;
		// sort population according to fitness, largest is at index 0
		sortedPopFitness = new ArrayList<IndividualFitness>();
		for (Lookup individual : population){
			double score = individual.getScore();
			avgScore += score;
			IndividualFitness individualFitness = new IndividualFitness(individual, score);
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
		worstScore = sortedPopFitness.get(sortedPopFitness.size()-1).score;
		avgScore /= sortedPopFitness.size();
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
		}			
		else {
			alreadyChosenIndex = -1; // reset father to default
		}
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
	
	public double getAvgScore(){
		if (avgScore == 0.0)
			judge();
		return avgScore;
	}
	
	public double getWorstScore(){
		if (worstScore == 0.0)
			judge();
		return worstScore;
	}
	
	public double getRange(){
		if (worstScore == 0.0 || bestScore == 0.0)
			judge();
		return bestScore - worstScore;
	}
	
	private int pickRandomBiasedI(int min, int max){
		// chose a random number between 0 and 1
		double randProb = Math.random();
		// pick a random index, bias towards stronger individuals
	    randProb = Math.pow(randProb, GA.BIAS_TOWARDS_FITNESS);
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
//		Logging.ON = false;
		Population pop = new Population();
//		for (int i = 0; i < 10; i++){
//			LookupArray2D lookup = new LookupArray2D(3, 3);
//			lookup.randomize();
//			pop.add(lookup);
//		}
//		pop.judge(); // sets scores and probabilities
//		
//		System.out.println("Lookup A: \n" + pop.population.get(0).getActionString() + "\nLookup B: \n" + pop.population.get(1).getActionString());
//		Lookup child = pop.population.get(0).makeChild(pop.population.get(1));
//		System.out.println("Child of A & B:" );
//		System.out.println(child.getActionString());
//		
		// Testing skew
		pop.printSkewGraph();
//		
	}

}
