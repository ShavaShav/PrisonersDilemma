package model.strategy;

import ipdlx.Strategy;

public class Gradual extends Strategy{
	int numDefect = 0, defectsDelivered = 0;
	public Gradual() {
		super("Grad", "Gradual", "Cooperates on the first move and onwards but defects After the first defection of the other player, it defects one time and cooperates two times; ... After the nth defection it reacts with n consecutive defections and then calms down its opponent with two cooperations");
		
	}
	@Override
	public double getMove() {	
		if(opponentMove == DEFECT)
		{
			numDefect++; // increment number of defects in a row
			defectsDelivered += numDefect; // accumulate current amount
		} else {
			numDefect = 0; // stopped defecting
		}
		
		if(defectsDelivered > 0)//Meaning we haven't given back as many defects as their accumlative amount.
		{
			defectsDelivered--; // deliver a defect
			return Strategy.DEFECT;
		}
		
		return Strategy.COOPERATE; // else we'll cooperate

	}
	
	@Override
	public void reset(){
		numDefect = 0;
		defectsDelivered = 0;
		opponentMove = Strategy.COOPERATE; // reset to random
	}
}
