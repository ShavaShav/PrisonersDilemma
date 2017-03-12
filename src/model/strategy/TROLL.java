package model.strategy;
import ipdlx.Strategy;

// Troll strategy
public class TROLL extends Strategy{

	// Constructor	
	public TROLL(){
		super("TROLL",
				"Troll ¯\\_(^-^)_/¯",
				"Picks the opposite move... most of the time");
		reset();
	}
	
	@Override
	public double getMove() {
		if (Math.random() < 0.1)
			return opponentMove; // 10% of time choose opponent's last move
		else if (opponentMove == Strategy.COOPERATE)
			return Strategy.DEFECT;
		else
			return Strategy.COOPERATE; 
	}
	
	@Override
	public void reset(){
		// history of opponents last move set to random move
		opponentMove = (int) (Math.random() + 0.5);
	}

}