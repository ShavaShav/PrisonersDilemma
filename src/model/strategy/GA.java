package model.strategy;
import ipdlx.Strategy;

// Genetic Algorithm strategy
public class GA extends Strategy{

	// Constructor
	public GA(){
		super("GA", "Genetic Algorithm", 
				"Chooses moves based on history and random mutations");
	}

	@Override
	public double getMove() {
		// TODO Write strategy for how moves are made here
		return Strategy.COOPERATE; // always co-operate for now
	}

	@Override
	public void reset(){
		// TODO Write memory reset here
	}
}