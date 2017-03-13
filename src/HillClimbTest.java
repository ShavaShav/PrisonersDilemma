import controller.HillClimbController;

import model.tool.Lookup;
import model.tool.LookupArray1D;
import model.tool.LookupArray2D;

public class HillClimbTest {
	public static final int OPPONENT_HISTORY_LENGTH = 3;
	public static final int PLAYER_HISTORY_LENGTH = 4;
	private static final boolean DEBUG = false;
	
	public static void main(String args[]){
		testAllArrayMethods();
		
//		HillClimbController.DEBUG = true;
//		Lookup solution = HillClimbController.climbHills1D(50, 5);
//		double score = solution.getScore();
//		System.out.print("Best " + solution + "\nScore: " + score);
		
	}

	public static void testAllArrayMethods() {
		// create a 1D lookup table
		LookupArray1D lookup1D = new LookupArray1D(OPPONENT_HISTORY_LENGTH);
		lookup1D.randomize();
		double score1D = lookup1D.getScore();
		if (DEBUG){
			System.out.println("Initial 1D Lookup : Score = " + score1D);
			System.out.println(lookup1D);			
		} 
		
		// run the hill climbing algorithm to get a better table
		LookupArray1D best1D = (LookupArray1D) HillClimbController.climbHill(lookup1D);
		double bestScore1D = best1D.getScore();
		if (DEBUG){
			System.out.println("1D Lookup after Hill Climbing : Score = " + bestScore1D);
			System.out.println(best1D);
		} else {
			System.out.println("1D:\n  " + score1D + " => HC => " + bestScore1D);
		}
		
		// create a 2D lookup table
		LookupArray2D lookup2D = new LookupArray2D(PLAYER_HISTORY_LENGTH, OPPONENT_HISTORY_LENGTH);
		lookup2D.randomize();
		double score2D = lookup2D.getScore();
		if (DEBUG){
			System.out.println("Initial 2D Lookup : Score = " + score2D);
			System.out.println(lookup2D);
		}
		
		// run the hill climbing algorithm to get a better table
		LookupArray2D best2D = (LookupArray2D) HillClimbController.climbHill(lookup2D);
		double bestScore2D = best2D.getScore();
		if (DEBUG){
			System.out.println("2D Lookup after Hill Climbing : Score = " + bestScore2D);
			System.out.println(best2D);
		} else {
			System.out.println("2D:\n  " + score2D + " => HC => " + bestScore2D);
		}
	}
}
