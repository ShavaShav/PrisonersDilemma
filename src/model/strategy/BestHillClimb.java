package model.strategy;

import controller.FileInfo;
import ipdlx.Strategy;
import model.tool.History;
import model.tool.Lookup;
import model.tool.SingleLookup;

public class BestHillClimb extends Strategy {
	// lookup and history must be initialized to same length!
	SingleLookup lookup;
	History opponentHistory;
	
	// Constructors
	public BestHillClimb(){
		super("LKUP", // shows in GUI
				"Single Lookup",  // full name of strategy
				"Plays game based off a lookup table containing a history of moves and actions"); // description of strategy
		lookup = (SingleLookup) Lookup.loadLookup(FileInfo.lookupPath, FileInfo.bestHillClimbFileName);
		opponentHistory = new History(lookup.getHistoryLength());
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
