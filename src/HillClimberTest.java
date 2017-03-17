import controller.HillClimber;
import model.tool.History;
import model.tool.Logging;
import model.tool.Lookup;
import model.tool.LookupArray1D;
import model.tool.LookupArray2D;
import model.tool.SingleLookup;

public class HillClimberTest {
	public static final int OPPONENT_HISTORY_LENGTH = 3;
	public static final int PLAYER_HISTORY_LENGTH = 3;
	public static final int MAX_RESTARTS = 0;
	public static final int MAX_SIDEWAYS_MOVES = 0;
	private static final boolean DEBUG = false;
	
	public static void main(String args[]){
		Logging.ON = false;
		testAllArrayMethods();
		
//		SingleLookup lookup = new LookupArray1D(OPPONENT_HISTORY_LENGTH);
//		HillClimber hc = new HillClimber(lookup);
//		hc.setMaxRestarts(MAX_RESTARTS);
//		hc.setMaxSidewaysMoves(MAX_SIDEWAYS_MOVES);
//		HillClimber.DEBUG = true;
//		SingleLookup solution = (SingleLookup) hc.climbHills1D(10,4);
//		double score = solution.getScore();
//		System.out.print("Best " + solution + "\nScore: " + score);
		
	}

	public static void testAllArrayMethods() {
		// create a 1D lookup table
		LookupArray1D lookup1D = new LookupArray1D(OPPONENT_HISTORY_LENGTH);
		lookup1D.randomize();
		
		HillClimber hc = new HillClimber(lookup1D);
		hc.setMaxRestarts(MAX_RESTARTS);
		hc.setMaxSidewaysMoves(MAX_SIDEWAYS_MOVES);
		
		double score1D = lookup1D.getScore();
		if (DEBUG){
			System.out.println("Initial 1D Lookup : Score = " + score1D);
			System.out.println(lookup1D);			
		} 
		
		// run the hill climbing algorithm to get a better table
		LookupArray1D best1D = (LookupArray1D) hc.climbHill();
		double bestScore1D = best1D.getScore();
		if (DEBUG){
			System.out.println("1D Lookup after Hill Climbing : Score = " + bestScore1D);
			System.out.println(best1D);
		} else {
			System.out.println("1D:\n  " + score1D + " => HC => " + bestScore1D);
		}
		System.out.println("\tNumIterations: " + hc.getNumIterations() +
				"\n\tTotalSidewaysMoves: " + hc.getTotalSidewaysMoves() +
				"\n\tRestarts: " + hc.getNumRestarts());
		
		// create a 2D lookup table
		LookupArray2D lookup2D = new LookupArray2D(PLAYER_HISTORY_LENGTH, OPPONENT_HISTORY_LENGTH);
		lookup2D.randomize();
		
		hc = new HillClimber(lookup2D);
		hc.setMaxRestarts(MAX_RESTARTS);
		hc.setMaxSidewaysMoves(MAX_SIDEWAYS_MOVES);
		
		double score2D = lookup2D.getScore();
		if (DEBUG){
			System.out.println("Initial 2D Lookup : Score = " + score2D);
			System.out.println(lookup2D);
		}
		
		// run the hill climbing algorithm to get a better table
		LookupArray2D best2D = (LookupArray2D) hc.climbHill();
		double bestScore2D = best2D.getScore();
		if (DEBUG){
			System.out.println("2D Lookup after Hill Climbing : Score = " + bestScore2D);
			System.out.println(best2D);
		} else {
			System.out.println("2D:\n  " + score2D + " => HC => " + bestScore2D);
		}
		System.out.println("\tNumIterations: " + hc.getNumIterations() +
				"\n\tTotalSidewaysMoves: " + hc.getTotalSidewaysMoves() +
				"\n\tRestarts: " + hc.getNumRestarts());
	}
}
