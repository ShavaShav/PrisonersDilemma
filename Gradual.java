package model.strategy;

import ipdlx.Strategy;

public class Gradual extends Strategy{
	int numDefect = 0, defectDelivered = 0;
	public Gradual() {
		super("Grad", "Gradual", "Cooperates on the first move and onwards but defects After the first defection of the other player, it defects one time and cooperates two times; ... After the nth defection it reacts with n consecutive defections and then calms down its opponent with two cooperations");
		
		// set lookups
	}
	@Override
	public double getMove() {
		// store opponents last move
		
		// lookup appropriate action according to history
		if(opponentMove == DEFECT)
		{
			numDefect+= 1;
			defectDelivered += numDefect;
		}
		if(defectDelivered != 0)//Meaning we haven't given back as many defects as their accumlative amount.
		{
			defectDelivered -= 1;
			return(Strategy.DEFECT);
		}
		else
		{
			return(Strategy.COOPERATE);
		}
	}
	
	@Override
	public void reset(){

		opponentMove = Strategy.COOPERATE; // reset to random
	}
}
