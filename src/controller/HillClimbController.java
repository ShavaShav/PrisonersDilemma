package controller;

import model.tool.Lookup;
import model.tool.LookupArray1D;
import model.tool.SingleLookup;
import model.tool.DoubleLookup;

public class HillClimbController {
	public static boolean DEBUG = false;
		
	// will alter the current lookup table!
	public static Lookup climbHill(Lookup currentLookup){
		// get the intial score
		double currentScore = currentLookup.getScore();
		// look for better tables until we hit a local max
		while (true) {
			// get the next best neighbour
			LookupState neighbourState = getNeighbour(currentLookup);
			if (neighbourState.getScore() <= currentScore)
				return currentLookup; // current lookup is better
			else{
				currentLookup.flipAction(neighbourState); // change lookup to better neighbour
				currentScore = neighbourState.getScore();
			}
		}

	}
	
	// get the neighbour with the best score by flipping every action one after the other
	private static LookupState getNeighbour(Lookup currentLookup){
		// using a collection class to store indices of neighbours, instead of making new tables
		LookupState bestNeighbour = new LookupState();
		if (currentLookup instanceof SingleLookup){
			SingleLookup singleLookup = (SingleLookup)currentLookup;
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
			DoubleLookup doubleLookup = (DoubleLookup)currentLookup;
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
	public static Lookup climbHills1D(int numHillClimbs, int historyLength){
		LookupArray1D currentLookup = new LookupArray1D(historyLength);
		double maxScore = 0;
		for (int i = 1; i <= numHillClimbs; i++){
			currentLookup.randomize();
			Lookup solution = climbHill(currentLookup);
			double score = solution.getScore();
			if (score > maxScore){
				maxScore = score;
				// save lookup to file
				Lookup.saveLookup(solution, FileInfo.lookupPath, FileInfo.bestHillClimbFileName);
			}
			if (DEBUG)
				System.out.println(i+ ": "+ score + " (MAX: "+maxScore+")");
		}
		currentLookup = (LookupArray1D) Lookup.loadLookup(FileInfo.lookupPath, FileInfo.bestHillClimbFileName);
		return currentLookup;	
	}
}
