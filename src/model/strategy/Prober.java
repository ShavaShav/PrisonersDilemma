package model.strategy;

import ipdlx.Strategy;
import model.tool.History;
import model.tool.LookupArray1D;

public class Prober extends Strategy{
	LookupArray1D lookup;
	History opponentHistory;
	int move = 0;
	double move2;
	boolean sucker = false;
	public Prober() {
		super("Prober", "Prober", "Starts with D,C,C then defects if the opponenet Cooperated in either of the second or third moves, otherwise plays TFT.");
		
		opponentHistory = new History(2);
		lookup = new LookupArray1D(2);
		
		// set lookups
		lookup.setAction(new History("CC"), Strategy.COOPERATE);
		lookup.setAction(new History("CD"), Strategy.COOPERATE);
		lookup.setAction(new History("DC"), Strategy.COOPERATE);
		lookup.setAction(new History("DD"), Strategy.DEFECT);
	}

	@Override
	public double getMove() {
		// store opponents last move
		opponentHistory.add(opponentMove);
		move++;
		if (move == 1){
			return Strategy.DEFECT;
		} else if (move == 2){
			return Strategy.COOPERATE;
		} else if (move == 3){
			if (opponentMove == Strategy.COOPERATE)
				sucker = true;
			return Strategy.COOPERATE;
		} else {
			if (move == 4 && opponentMove == Strategy.COOPERATE)
				sucker = true;
			
			if (sucker)
				return Strategy.DEFECT;
			else
				return lookup.getAction(opponentHistory);
		}
		
	}
	
	@Override
	public void reset(){
		opponentMove = (int) (Math.random() + 0.5); // reset to random
	}
}
