package model.strategy;

import ipdlx.Strategy;

public class HardMajority extends Strategy{
	int coopCount = 0, defectCount = -1;
	
	
	public HardMajority() {
		super("HM", "HardMajority", "If the cummlaitve number of defects is equal to the lower than the cummlative number of cooperates then it cooperates otherwise defects and defects on first move.");
	}

	@Override
	public double getMove() {
		if(opponentMove == Strategy.DEFECT)
		{
			defectCount++;
		}
		else
		{
			coopCount++;
		}
		
		if(coopCount > defectCount)
		{
			return Strategy.COOPERATE;
		}
		else
		{
			return Strategy.DEFECT;			
		}
	}
	
	@Override
	public void reset(){
		opponentMove = Strategy.DEFECT; // reset to random
	}
}
