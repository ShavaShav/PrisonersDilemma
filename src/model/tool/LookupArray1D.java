package model.tool;
import java.util.Random;

import controller.LookupState;
import ipdlx.GameMatrix;
import ipdlx.GameResult;
import ipdlx.Player;
import ipdlx.StandardGame;
import ipdlx.Strategy;
import model.strategy.SingleLookupStrategy;
// Warning: Can only handle histories up to length 31 (array indices limit is integer)
// and standard java heap limits further to 26. Increasing heap to 4GB only helps marginally (27 moves)
public class LookupArray1D implements SingleLookup{
	private static final long serialVersionUID = 5572505912269640758L;
	private static final boolean DEBUG = false;
	private double[] lookupTable;
	private int tableLength;
	private int historyLength;
	private Random rand;
	// variables for getting score
	private Player lookupPlayer;
	
	public LookupArray1D(int historyLength){
		this.historyLength = historyLength;
		tableLength = (int) Math.pow(2, historyLength);
		lookupTable = new double[tableLength];
		lookupPlayer = new Player(
						new SingleLookupStrategy(this, new History(historyLength)), 
						"Lookup");
	}
	
	/*
	 * getScore() plays a number of games against TFT (the standard)
	 * to determine the fitness of the table. Returns the average score
	 * per round. This will likely take the most time to compute
	 */
	@Override
	public double getScore(GameMatrix payoffMatrix) {
		double score = 0.0;
		for (int i = 0; i < ScoringInfo.STRATEGIES.length; i++){
			score += getScore(payoffMatrix, ScoringInfo.STRATEGIES[i]);
		}
		return Math.round((score / ScoringInfo.STRATEGIES.length) * ScoringInfo.ACCURACY) 
				/ ScoringInfo.ACCURACY;
		
	}
	@Override
	// get average score score per round against a particular strategy
	public double getScore(GameMatrix payoffMatrix, Strategy strategy) {
		double score = 0.0;
		for (int i = 0; i < ScoringInfo.NUM_GAMES; i++){
			Player opponent = new Player(strategy, "P2"); // random	
			// create a standard game (2 players) -> 50 rounds
			StandardGame game = new StandardGame(ScoringInfo.NUM_ROUNDS, payoffMatrix);
			game.setPlayerA(lookupPlayer);
			game.setPlayerB(opponent);	
			// play the game and return results
			GameResult results = game.play();
			score += results.getPayoffPlayer(0)/ScoringInfo.NUM_ROUNDS;
		}
		if (DEBUG) System.out.println("Score against " + strategy + ": "
				+ score/ScoringInfo.NUM_GAMES);
		return score/ScoringInfo.NUM_GAMES;			
	}

	// O(n) to create the random lookup table
	@Override
	public void randomize() {
		rand = new Random();
		lookupTable = new double[tableLength];
		for (int i = 0; i < tableLength; i++)
			lookupTable[i] = Math.round(rand.nextDouble());
	}

	@Override
	public void flipRandomAction() {
		int randI = rand.nextInt(tableLength);
		flipAction((long) randI);
	}

	@Override
	public double getAction(History history) {
		return lookupTable[(int) history.getValue()];
	}
	

	@Override
	public void flipAction(History history) {
		flipAction(history.getValue());
	}

	@Override
	public void setAction(History history, double action) {
		lookupTable[(int)history.getValue()] = action;	
	}
	
	public String toString(){
		String retString = "Lookup Table:\nHistory => Action\n";
		for (int i = 0; i < tableLength; i++){
			retString += History.longToString(i, historyLength) + " => " + (lookupTable[i]==1.0?"C":"D") + "\n";
		}
		return retString;
	}

	@Override
	public void flipAction(long historyValue) {
		if (lookupTable[(int) historyValue] == Strategy.COOPERATE)
			lookupTable[(int) historyValue] = Strategy.DEFECT;
		else
			lookupTable[(int) historyValue] = Strategy.COOPERATE;
		
	}

	@Override
	public void flipAction(LookupState lookupState) {
		flipAction(lookupState.getRow());
	}

	@Override
	public int getLength() {
		return tableLength;
	}
	
}
