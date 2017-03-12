package model.tool;
import java.util.Random;

import ipdlx.GameMatrix;
import ipdlx.GameResult;
import ipdlx.Player;
import ipdlx.StandardGame;
import ipdlx.Strategy;
import model.strategy.DoubleLookupStrategy;

// Warning: can only handle 26 moves between the histories
public class LookupArray2D implements DoubleLookup {
	private static final long serialVersionUID = 1410096656666957820L;
	private static final boolean DEBUG = false;
	private double[][] lookupTable;
	private int tableRows; // rows = history 1
	private int tableCols; // cols = history 2
	private int history1Length; // for printing
	private int history2Length;
	private Random rand;
	// variables for getting score
	private Player lookupPlayer;
	
	public LookupArray2D(int history1Length, int history2Length){
		this.history1Length = history1Length;
		this.history2Length = history2Length;
		tableRows = (int) Math.pow(2, history1Length);
		tableCols = (int) Math.pow(2, history2Length);
		lookupTable = new double[tableRows][tableCols];
		lookupPlayer = new Player(
						new DoubleLookupStrategy(this, new DoubleHistory(history1Length, history2Length)), 
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
		lookupTable = new double[tableRows][tableCols];
		for (int i = 0; i < tableRows; i++)
			for (int j = 0; j < tableCols; j++)
				lookupTable[i][j] = Math.round(rand.nextDouble());
	}

	@Override
	public void flipRandomAction() {
		int randRow = rand.nextInt(tableRows);
		int randCol = rand.nextInt(tableCols);
		if (lookupTable[randRow][randCol] == Strategy.COOPERATE)
			lookupTable[randRow][randCol] = Strategy.DEFECT;
		else
			lookupTable[randRow][randCol] = Strategy.COOPERATE;
	}

	@Override
	public double getAction(DoubleHistory doubleHistory) {
		return lookupTable[(int) doubleHistory.getHistory1Value()]
				[(int) doubleHistory.getHistory2Value()];
	}
	

	@Override
	public void flipAction(DoubleHistory doubleHistory) {
		int hist1 = (int) doubleHistory.getHistory1Value();
		int hist2 = (int) doubleHistory.getHistory2Value();
		if (lookupTable[hist1][hist2] == Strategy.COOPERATE)
			lookupTable[hist1][hist2] = Strategy.DEFECT;
		else
			lookupTable[hist1][hist2] = Strategy.COOPERATE;
	}

	@Override
	public void setAction(DoubleHistory doubleHistory, double action) {
		lookupTable[(int) doubleHistory.getHistory1Value()]
				[(int) doubleHistory.getHistory2Value()] = action;	
	}
	
	public String toString(){
		String retString = "Lookup Table:\nHistory 1 | History 2 => Action\n";
		for (int hist1 = 0; hist1 < tableRows; hist1++)
			for (int hist2 = 0; hist2 < tableCols; hist2++)
				retString += History.longToString(hist1, history1Length) + 
						" | " + History.longToString(hist2, history2Length) +
						" => " + (lookupTable[hist1][hist2]==1.0?"C":"D") + "\n";
		return retString;
	}
	
}
