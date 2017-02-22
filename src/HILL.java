import ipdlx.Strategy;

// Hill-climbing strategy
public class HILL extends Strategy{
	
	// Constructor
	public HILL(){
		super("HILL", "Hill Climbing", 
				"Uses local search to optimize move choices");
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