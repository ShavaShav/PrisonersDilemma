package controller;

import model.tool.Lookup;
import model.tool.LookupArray1D;
import model.tool.SingleLookup;
import model.tool.DoubleLookup;

public class HillClimber {
	public static boolean DEBUG = false;
	private Lookup lookup;
	private double maxScore; // if restart, should keep the better lookup
	private double currentMaxScore;
	private int numRestarts, numIterations, maxRestarts;
	private int sidewaysMoves, maxSideways, totalSidewaysMoves;
	
	public HillClimber(Lookup lookup){
		this.lookup = lookup;
		this.maxSideways = 0;
		this.maxRestarts = 0;
		maxScore = -1.0; // score over all restarts
		currentMaxScore = lookup.getScore(); // current hillclimb max
		sidewaysMoves = 0;
		totalSidewaysMoves = 0; // moves over all restarts
		numRestarts = 0;
		numIterations = 0;
	}
	
	public void setMaxRestarts(int maxRestarts){
		this.maxRestarts = maxRestarts;
	}
	
	public void setMaxSidewaysMoves(int sidewaysMoves){
		this.maxSideways = sidewaysMoves;
	}
	
	public int getNumRestarts(){ return numRestarts; }
	public int getNumIterations(){ return numIterations; }
	public int getSidewaysMoves(){ return sidewaysMoves; }
	public int getTotalSidewaysMoves(){ return totalSidewaysMoves; }
	public double getMaxScore(){ return maxScore; }
	
	// will alter the current lookup table!
	public Lookup climbHill(){
		// look for better tables until we hit a local max
		while (true) {
			// get the next best neighbour
			LookupState neighbourState = getNeighbour();

			// if it's a better neighbour
			if (neighbourState.getScore() > currentMaxScore){
				lookup.flipAction(neighbourState); // change lookup to it's state
				currentMaxScore = neighbourState.getScore();
				if (currentMaxScore > maxScore){
					// save the best lookup, as so can maintain best lookup over restarts
					maxScore = currentMaxScore;
					Lookup.saveLookup(lookup, FileInfo.lookupPath, FileInfo.utilyLookup);				
				}
				sidewaysMoves = 0; // reset sideways moves
			} else if (neighbourState.getScore() < currentMaxScore || numRestarts >= maxRestarts) {
				// if neighbour is worse than current, or out of restarts (no more sideways moves)
				// return the best lookup found so far, after loading from disk
				if (maxScore > 0) // only load if max has been set
					lookup = Lookup.loadLookup(FileInfo.lookupPath, FileInfo.utilyLookup);
				return lookup;
			} else { // neighbour score is equal to current max
				if (sidewaysMoves < maxSideways) {
					// try a sideways move if allowed, dont need to update score
					lookup.flipAction(neighbourState); // change lookup to same scoring neighbour
					sidewaysMoves++;
					totalSidewaysMoves++;
				} else {
					// out of sideways moves, do a random restart
					lookup.randomize();		
					currentMaxScore = lookup.getScore();
					sidewaysMoves = 0;
					numRestarts++;
				}			
			}
			numIterations++;
		}
	}
	
	// get the neighbour with the best score by flipping every action one after the other
	private LookupState getNeighbour(){
		// using a collection class to store indices of neighbours, instead of making new tables
		LookupState bestNeighbour = new LookupState();
		if (lookup instanceof SingleLookup){
			SingleLookup singleLookup = (SingleLookup)lookup;
			for (int history = 0; history < singleLookup.getLength(); history++){
				singleLookup.flipAction(history); // change state
				double score = singleLookup.getScore(); // get score (heuristic)
				singleLookup.flipAction(history); // flip back to previouse
				if (score > bestNeighbour.getScore()){ //  keep the best neighbour so far
					bestNeighbour.setRow(history);
					bestNeighbour.setScore(score);
				}
			}
		} else {
			DoubleLookup doubleLookup = (DoubleLookup)lookup;
			for (int history1 = 0; history1 < doubleLookup.getFirstLength(); history1++){
				for (int history2 = 0; history2 < doubleLookup.getSecondLength(); history2++){
					doubleLookup.flipAction(history1, history2); // change state
					double score = doubleLookup.getScore(); // get score (heuristic)
					doubleLookup.flipAction(history1, history2); // flip back to previouse
					if (score > bestNeighbour.getScore()){
						bestNeighbour.setRow(history1);
						bestNeighbour.setCol(history2);
						bestNeighbour.setScore(score);
					}		
				}
			}
		}
		return bestNeighbour;
	}
	
	
	
	// runs the algorithm numHillClimbs times on random lookups and returns the best
	public Lookup climbHills1D(int numHillClimbs, int historyLength){
		lookup = new LookupArray1D(historyLength);
		double maxScore = 0;
		for (int i = 1; i <= numHillClimbs; i++){
			lookup.randomize();
			Lookup solution = climbHill();
			double score = solution.getScore();
			if (score > maxScore){
				maxScore = score;
				// save lookup to file
				Lookup.saveLookup(solution, FileInfo.lookupPath, FileInfo.bestHillClimbFileName);
			}
			if (DEBUG)
				System.out.println(i+ ": "+ score + " (MAX: "+maxScore+")");
		}
		lookup = (LookupArray1D) Lookup.loadLookup(FileInfo.lookupPath, FileInfo.bestHillClimbFileName);
		return lookup;	
	}

}
