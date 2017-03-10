import ipdlx.Strategy;

public class TEMPLATE_Strategy extends Strategy{
	/*
	 *  Some protected Strategy fields you might find useful:
	 *  
	 *  double opponentMove
	 *  	- opponent's last move - Cooperate or Defect
	 *  double lastResult
	 *  	- info about last game -> score
	 *  int nrOfOpponents
	 *  	- number of opponents (only for multiplayer games)
	 *  double[] opponentMoves
	 *  	- opponents' last moves - (only for multiplayer games)
	 */ 
	
	// Constructor
	public TEMPLATE_Strategy(){
		super("TEMP", // shows in GUI
				"A template strategy",  // full name of strategy
				"Doesn't do much, basically it's ALLC"); // description of strategy
	}
	
	// returns the current move -> DEFECT or COOPERATE
	@Override
	public double getMove() {
		// TODO Write strategy for how moves are made here and you
		// might want to save the opponents last move if required
		
		return Strategy.COOPERATE; // always coop for now
	}
	
	// should implement reset() if strategy involves using memory
	@Override
	public void reset(){
		// TODO Write reset of move history here
		
		opponentMove = (int) (Math.random() + 0.5); // reset to random
	}
}
