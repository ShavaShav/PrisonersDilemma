package model.strategy;

import ipdlx.Strategy;
import model.tool.History;
import model.tool.LookupArray1D;

public class Prober extends Strategy{
	LookupArray1D lookup;
	History opponentHistory;
	int Move = 0;
	boolean Sucker = false;
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
		switch(Move)
		{
			case 0:
			{
				System.out.println("Did I make it to the switch case?");
				Move+=1;
				return(Strategy.DEFECT);
			}
			case 1:
			{
				Move+=1;
				return(Strategy.COOPERATE);
			}
			case 2:
			{
				Move+=1;
				if(opponentMove == COOPERATE)
				{
					Sucker = true;
				}
				return(Strategy.COOPERATE);
			}
		}
		if(Move == 3 && opponentMove == COOPERATE)
		{
			Sucker = true;
			Move++;
			return(Strategy.DEFECT);
		}
		else if(Move == 3) 
		{
			Move++;
			return(Strategy.COOPERATE);
		}
		if(Move > 3 && Sucker == false)
		{
			opponentHistory.add(opponentMove);
			// lookup appropriate action according to history
			return lookup.getAction(opponentHistory);
		}
		return(Strategy.DEFECT);
	}
	
	@Override
	public void reset(){
		opponentMove = (int) (Math.random() + 0.5); // reset to random
	}
}
