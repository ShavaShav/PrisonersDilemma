import java.util.Random;

import ipdlx.GameMatrix;
import ipdlx.GameResult;
import ipdlx.Player;
import ipdlx.StandardGame;
import ipdlx.Strategy;
import ipdlx.strategy.RAND;
// Warning: Can only handle histories up to length 31! (array indices limit is integer)
public class LookupArray1D implements SingleLookup{

	private double[] lookupTable;
	private int tableLength;
	private Random rand;
	private static final int NUM_ROUNDS = 200;
	
	public LookupArray1D(int historyLength){
		tableLength = (int) Math.pow(2, historyLength);
		lookupTable = new double[tableLength];
	}
	
	/*
	 * getScore() plays a number of games against TFT (the standard)
	 * to determine the fitness of the table. Returns the average score
	 * This will likely take the most time to compute
	 */
	@Override
	public double getScore() {
		double p_array[][] = GameMatrix.defaultPayoffMatrix2x2;
		GameMatrix payoffMatrix = new GameMatrix(p_array);		
		// create players with strategies // TODO creating random history for temp strat might not be good?
		Player lookupPlayer = new Player(new SingleLookupStrategy(this, new History(tableLength)), "Lookup");
		Player TTFT = new Player(new RAND(), "P2"); // random	
		// create a standard game (2 players) -> 50 rounds
		StandardGame game = new StandardGame(NUM_ROUNDS, payoffMatrix);
		game.setPlayerA(lookupPlayer);
		game.setPlayerB(TTFT);	
		// play the game and return results
		GameResult results = game.play();
		return results.getPayoffPlayer(0)/NUM_ROUNDS;
	}

	// O(n) to create the random lookup table
	@Override
	public void randomize() {
		System.out.println("Hi");
		rand = new Random();
		lookupTable = new double[tableLength];
		for (int i = 0; i < tableLength; i++)
			lookupTable[i] = Math.round(rand.nextDouble());
	}

	@Override
	public void flipRandomAction() {
		int randI = rand.nextInt(tableLength);
		if (lookupTable[randI] == Strategy.COOPERATE)
			lookupTable[randI] = Strategy.DEFECT;
		else
			lookupTable[randI] = Strategy.DEFECT;
	}

	@Override
	public double getAction(History history) {
		return lookupTable[(int) history.getValue()];
	}
	

	@Override
	public void flipAction(History history) {
		if (lookupTable[(int) history.getValue()] == Strategy.COOPERATE)
			lookupTable[(int) history.getValue()] = Strategy.DEFECT;
		else
			lookupTable[(int) history.getValue()] = Strategy.DEFECT;
	}

	@Override
	public void setAction(History history, double action) {
		lookupTable[(int)history.getValue()] = action;	
	}
	
	public String toString(){
		String retString = "";
		for (int i = 0; i < tableLength; i++){
			retString += History.longToString(i, tableLength) + " => " + lookupTable[i] + "\n";
		}
		return retString;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
}
