package model.tool;
import java.util.Random;

import controller.LookupState;
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

	public LookupArray2D(int history1Length, int history2Length){
		this.history1Length = history1Length;
		this.history2Length = history2Length;
		tableRows = (int) Math.pow(2, history1Length);
		tableCols = (int) Math.pow(2, history2Length);
		lookupTable = new double[tableRows][tableCols];
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
				new DoubleLookupStrategy(this, new DoubleHistory(history1Length, history2Length)), 
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
		lookupTable = new double[tableRows][tableCols];
		for (int i = 0; i < tableRows; i++)
			for (int j = 0; j < tableCols; j++)
				lookupTable[i][j] = Math.round(rand.nextDouble());
	}

	@Override
	public void flipRandomAction() {
		int randRow = rand.nextInt(tableRows);
		int randCol = rand.nextInt(tableCols);
		flipAction(randRow, randCol);
	}

	@Override
	public double getAction(DoubleHistory doubleHistory) {
		return lookupTable[(int) doubleHistory.getHistory1Value()]
				[(int) doubleHistory.getHistory2Value()];
	}
	

	@Override
	public void flipAction(DoubleHistory doubleHistory) {
		flipAction(doubleHistory.getHistory1Value(), 
				doubleHistory.getHistory2Value());
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

	@Override
	public void flipAction(long hist1_index, long hist2_index) {
		if (lookupTable[(int) hist1_index][(int) hist2_index] == Strategy.COOPERATE)
			lookupTable[(int) hist1_index][(int) hist2_index] = Strategy.DEFECT;
		else
			lookupTable[(int) hist1_index][(int) hist2_index] = Strategy.COOPERATE;
	}

	@Override
	public void flipAction(LookupState lookupState) {
		flipAction(lookupState.getRow(), lookupState.getCol());
	}

	@Override
	public int getFirstLength() {
		return tableRows;
	}

	@Override
	public int getSecondLength() {
		return tableCols;
	}

	@Override
	public int getFirstHistoryLength() {
		return history1Length;
	}

	@Override
	public int getSecondHistoryLength() {
		return history2Length;
	}
	
	@Override
	public Lookup makeChild(Lookup partner) {
		LookupPair children = makeChildren(partner);
		if (children.brother.getScore() > children.sister.getScore())
			return children.brother;
		else
			return children.sister;
		
	}

	@Override
	public LookupPair makeChildren(Lookup partner) {
		if (partner instanceof LookupArray2D){
			LookupArray2D partner2D = (LookupArray2D) partner;
			// pick a pivot somewhere in the middle of rows and cols (middle 50%)
			int quarterRow = tableRows/4;
			int quarterCol = tableCols/4;
			int randSplitRow = quarterRow + (int) (Math.random() * (tableRows - (quarterRow)));
			int randSplitCol = quarterCol + (int) (Math.random() * (tableCols - (quarterCol)));
			LookupArray2D brother = new LookupArray2D(history1Length, history2Length);
			LookupArray2D sister = new LookupArray2D(history1Length, history2Length);
			// top left corner
			for (int row = 0; row < randSplitRow; row++){
				for (int col = 0; col < randSplitCol; col++){
					brother.lookupTable[row][col] = this.lookupTable[row][col];
					sister.lookupTable[row][col] = partner2D.lookupTable[row][col];	
				}
			}
			//bottom left
			for (int row = randSplitRow; row < tableRows; row++){
				for (int col = 0; col < randSplitCol; col++){
					brother.lookupTable[row][col] = partner2D.lookupTable[row][col];
					sister.lookupTable[row][col] = this.lookupTable[row][col];	
				}
			}
			// top right
			for (int row = 0; row < randSplitRow; row++){
				for (int col = randSplitCol; col < tableCols; col++){
					brother.lookupTable[row][col] = partner2D.lookupTable[row][col];
					sister.lookupTable[row][col] = this.lookupTable[row][col];	
				}
			}
			// bottom right
			for (int row = randSplitRow; row < tableRows; row++){
				for (int col = randSplitCol; col < tableCols; col++){
					brother.lookupTable[row][col] = this.lookupTable[row][col];
					sister.lookupTable[row][col] = partner2D.lookupTable[row][col];	
				}
			}
			return new LookupPair(brother, sister);
		}
		return null;
	}

	@Override
	public String getActionString() {
		String retString = "";
		for (int hist1 = 0; hist1 < tableRows; hist1++){
			for (int hist2 = 0; hist2 < tableCols; hist2++){
				retString += lookupTable[hist1][hist2]==1.0?"C":"D";				
			}
			retString += "\n";
		}
		return retString;
	}
	
}
