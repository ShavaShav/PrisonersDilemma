import controller.GA;
import controller.HillClimber;
import model.tool.Logging;
import model.tool.Lookup;
import model.tool.LookupArray1D;
import model.tool.LookupArray2D;

public class StratLogger {
	private static int playerHistoryLength;
	private static int opponentHistoryLength;
	private static boolean singleLookup;
	private static final boolean DEBUG = false;

	public static void logHillClimb(int maxRestarts, int maxSidewaysMoves){
		Lookup solution;
		HillClimber hc;
		if (singleLookup){
			LookupArray1D lookup = new LookupArray1D(opponentHistoryLength);
			lookup.randomize();
			hc = new HillClimber(lookup);
		} else {
			LookupArray2D lookup = new LookupArray2D(playerHistoryLength, opponentHistoryLength);
			lookup.randomize();
			hc = new HillClimber(lookup);
		}
		hc.setMaxRestarts(maxRestarts);
		hc.setMaxSidewaysMoves(maxSidewaysMoves);
		solution = hc.climbHill();
		if (DEBUG) {
			double score = solution.getScore();
			System.out.println("HC Best Score (recalc): " + score + (singleLookup?" (1D)":" (2D)"));
		}
	}
	
	// population size must be > 2!
	public static void logEvolution(int populationSize, int numGenerations, int numChildrenPerCouple){
		Lookup solution;
		GA ga;
		if (singleLookup)
			ga = new GA(opponentHistoryLength); // single history (length of 3 moves)
		else
			ga = new GA(playerHistoryLength, opponentHistoryLength);
		solution = ga.evolve(populationSize, numGenerations, numChildrenPerCouple);
		if (DEBUG) {
			double score = solution.getScore();
			System.out.println("GA Best Score (recalc): " + score + (singleLookup?" (1D)":" (2D)"));			
		}
	}
	
	public static void main(String[] args) {
		Logging.init();
		
		int numHillClimbs = 1, numEvolutions = 1;
		
		playerHistoryLength = 3;
		opponentHistoryLength = 3;
		
		/*
		 *  HILL CLIMBER - COLLECT DATA
		 */
		int numRestarts = 10, numSidewaysMoves = 10;
		
		// 1D array method
		singleLookup = true;
		for (int i = 0; i < numHillClimbs; i++){			
			logHillClimb(numRestarts, numSidewaysMoves);
			System.out.println("\n***********************\n");
		}
		
		// 2D array method
		singleLookup = false;
		for (int i = 0; i < numHillClimbs; i++){
			logHillClimb(numRestarts, numSidewaysMoves);
			System.out.println("\n***********************\n");
		}
		
		/*
		 *  GENETIC ALGORITHM - COLLECT DATA
		 */
		int populationSize = 100, numGenerations = 100, numChildrenPerCouple = 1;
		
		// 1D array method
		singleLookup = true;
		for (int i = 0; i < numEvolutions; i++){
			logEvolution(populationSize, numGenerations, numChildrenPerCouple);
			System.out.println("\n***********************\n");
		}
		
		// 2D array method
		singleLookup = false;
		for (int i = 0; i < numEvolutions; i++){
			logEvolution(populationSize, numGenerations, numChildrenPerCouple);
			System.out.println("\n***********************\n");
		}
	}

}
