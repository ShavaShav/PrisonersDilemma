package model.strategy;
import ipdlx.Strategy;
import model.tool.DoubleHistory;
import model.tool.DoubleLookup;

public class DoubleLookupStrategy extends Strategy {
	// lookup row/cols and histories must be initialized to same length!
	DoubleLookup lookup;
	DoubleHistory doubleHistory;
	double lastMove;
	
	// Constructors
	public DoubleLookupStrategy(DoubleLookup lookup, DoubleHistory doubleHistory){
		super("LKUP2", // shows in GUI
				"Double Lookup",  // full name of strategy
				"Plays game based off a lookup table containing a history of moves and actions for opponent and self"); // description of strategy
		this.lookup = lookup;
		this.doubleHistory = doubleHistory;
	}
	@Override
	public double getMove() {
		doubleHistory.add(lastMove, opponentMove); 
		lastMove = lookup.getAction(doubleHistory); // get next action, store it and return
		return lastMove;
	}
	
	@Override
	public void reset(){
		opponentMove = (int) (Math.random() + 0.5); // reset to random
	}
}
