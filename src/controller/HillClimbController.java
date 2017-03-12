package controller;

import ipdlx.GameMatrix;
import model.tool.Lookup;
import model.tool.LookupArray1D;
import model.tool.LookupArray2D;
import model.tool.SingleLookup;
import model.tool.DoubleLookup;

public class HillClimbController {
	public static final int OPPONENT_HISTORY_LENGTH = 2;
	public static final int PLAYER_HISTORY_LENGTH = 2;
	public static final GameMatrix PAYOFF_MATRIX = new GameMatrix(GameMatrix.defaultPayoffMatrix2x2);
	
	public static void main(String args[]){
		// create a 1D lookup table
		LookupArray1D lookup1D = new LookupArray1D(OPPONENT_HISTORY_LENGTH);
		lookup1D.randomize();
		System.out.println("Initial 1D Lookup : Score = " + lookup1D.getScore(PAYOFF_MATRIX));
		System.out.println(lookup1D);
		
		// run the hill climbing algorithm to get a better table
		LookupArray1D best1D = (LookupArray1D) climbHill(lookup1D);
		System.out.println("1D Lookup after Hill Climbing : Score = " + best1D.getScore(PAYOFF_MATRIX));
		System.out.println(best1D);
		
		// create a 2D lookup table
		LookupArray2D lookup2D = new LookupArray2D(PLAYER_HISTORY_LENGTH, OPPONENT_HISTORY_LENGTH);
		lookup2D.randomize();
		System.out.println("Initial 2D Lookup : Score = " + lookup2D.getScore(PAYOFF_MATRIX));
		System.out.println(lookup2D);
		
		// run the hill climbing algorithm to get a better table
		LookupArray2D best2D = (LookupArray2D) climbHill(lookup2D);
		System.out.println("2D Lookup after Hill Climbing : Score = " + best2D.getScore(PAYOFF_MATRIX));
		System.out.println(best2D);
	}
	
	// will alter the current lookup table!
	public static Lookup climbHill(Lookup currentLookup){
		// get the intial score
		double currentScore = currentLookup.getScore(PAYOFF_MATRIX);
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
				double score = singleLookup.getScore(PAYOFF_MATRIX); // get score (heuristic)
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
					double score = doubleLookup.getScore(PAYOFF_MATRIX); // get score (heuristic)
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
}
