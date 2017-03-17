package model.tool;
import java.util.Random;

import controller.LookupState;
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
	
	public LookupArray1D(int historyLength){
		this.historyLength = historyLength;
		tableLength = (int) Math.pow(2, historyLength);
		lookupTable = new double[tableLength];
		rand = new Random();
	}
	
	/*
	 * getScore() plays a number of games against TFT (the standard)
	 * to determine the fitness of the table. Returns the average score
	 * per round. This will likely take the most time to compute
	 */
	@Override
	public double getScore() {
		double score = 0.0;
		for (int i = 0; i < ScoringInfo.STRATEGIES.length; i++){
			score += getScore(ScoringInfo.STRATEGIES[i]);
		}
		return Math.round((score / ScoringInfo.STRATEGIES.length) * ScoringInfo.ACCURACY) 
				/ ScoringInfo.ACCURACY;
		
	}
	
	@Override
	// get average score score per round against a particular strategy
	public double getScore(Strategy strategy) {
		Player lookupPlayer = new Player(
				new SingleLookupStrategy(this, new History(historyLength)), 
				"Lookup");
		double score = 0.0;
		for (int i = 0; i < ScoringInfo.NUM_GAMES; i++){
			Player opponent = new Player(strategy, "P2"); // random	
			// create a standard game (2 players) -> 50 rounds
			StandardGame game = new StandardGame(ScoringInfo.NUM_ROUNDS, ScoringInfo.MATRIX);
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

	@Override
	public int getHistoryLength() {
		return historyLength;
	}

	// this method abandons the weaker child
	public Lookup makeChild(Lookup partner){
		LookupPair children = makeChildren(partner);
		if (children.brother.getScore() > children.sister.getScore())
			return children.brother;
		else
			return children.sister;
	}
	
	@Override
	public LookupPair makeChildren(Lookup partner) {
		if (partner instanceof LookupArray1D){
			LookupArray1D partner1D = (LookupArray1D) partner;
			// pick a pivot somewhere in the middle (middle 50%)
			int quarter = tableLength/4;
			int randSplitI = quarter + (int) (Math.random() * (tableLength - (quarter)));
			
			LookupArray1D brother = new LookupArray1D(historyLength);
			LookupArray1D sister = new LookupArray1D(historyLength);
			for (int i = 0; i < randSplitI; i++){
				brother.lookupTable[i] = this.lookupTable[i];
				sister.lookupTable[i] = partner1D.lookupTable[i];
			}
			for (int i = randSplitI; i < tableLength; i++){
				brother.lookupTable[i] = partner1D.lookupTable[i];
				sister.lookupTable[i] = this.lookupTable[i];
			}
			return new LookupPair(brother, sister);
		}
		return null;
	}

	@Override
	public String getActionString() {
		String retString = "";
		for (int i = 0; i < tableLength; i++){
			retString += lookupTable[i]==1.0?"C":"D";
		}
		return retString;
	}
	
}
