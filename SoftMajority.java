package model.strategy;

import ipdlx.Strategy;

public class SoftMajority extends Strategy{
	int coopCount = -1, defectCount = 0;
	
	
	public SoftMajority() {
		super("SM", "SoftMajority", "As long as the cummlaitve number of defects is lower than the cummlative number of cooperates then it cooperates otherwise defects");
	}

	@Override
	public double getMove() {
		// store opponents last move
		// lookup appropriate action according to history
		if(opponentMove == DEFECT)
		{
			defectCount++;
		}
		else
		{
			coopCount++;
		}
		if(coopCount >= defectCount)
		{
			return(Strategy.COOPERATE);
		}
		else
		{
			return(Strategy.DEFECT);			
		}
	}
	
	@Override
	public void reset(){
		opponentMove = Strategy.COOPERATE; // reset to random
	}
}
