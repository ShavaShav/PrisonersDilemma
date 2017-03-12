package model.strategy;
import ipdlx.Strategy;
import model.tool.History;
import model.tool.SingleLookup;

public class SingleLookupStrategy extends Strategy {
	// lookup and history must be initialized to same length!
	SingleLookup lookup;
	History opponentHistory;
	
	// Constructors
	public SingleLookupStrategy(SingleLookup lookup, History opponentHistory){
		super("LKUP", // shows in GUI
				"Single Lookup",  // full name of strategy
				"Plays game based off a lookup table containing a history of moves and actions"); // description of strategy
		this.lookup = lookup;
		this.opponentHistory = opponentHistory;
	}
	@Override
	public double getMove() {
		opponentHistory.add(opponentMove);
		return lookup.getAction(opponentHistory);
	}
	
	@Override
	public void reset(){
		opponentMove =  (int) (Math.random() + 0.5);
	}
	

}
