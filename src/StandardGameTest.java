
import ipdlx.*;
import model.strategy.DoubleLookupStrategy;
import model.strategy.SingleLookupStrategy;
import model.tool.DoubleHistory;
import model.tool.DoubleLookup;
import model.tool.History;
import model.tool.SingleLookup;
import model.tool.LookupArray1D;
import model.tool.LookupArray2D;
import model.tool.ScoringInfo;

/*
 *  Test of a 2-player game
 */
public class StandardGameTest {
	public static void main(String args[]){		
		// create players with strategies
		SingleLookup lookup = new LookupArray1D(2); // 2 moves history
		lookup.randomize();
		History history = new History(2); // random history
		// create a TFTT from table lookup
		lookup.setAction(new History("CC"), Strategy.COOPERATE);
		lookup.setAction(new History("CD"), Strategy.COOPERATE);
		lookup.setAction(new History("DC"), Strategy.COOPERATE);
		lookup.setAction(new History("DD"), Strategy.DEFECT);
		
		DoubleLookup doubleLookup = new LookupArray2D(1, 1); // store last move for both
		doubleLookup.randomize();
		DoubleHistory doubleHistory = new DoubleHistory(1, 1);
		// create a Suspicious forgiver
		doubleLookup.setAction(new DoubleHistory("C", "C"), Strategy.COOPERATE);
		doubleLookup.setAction(new DoubleHistory("C", "D"), Strategy.DEFECT);
		doubleLookup.setAction(new DoubleHistory("D", "C"), Strategy.COOPERATE);
		doubleLookup.setAction(new DoubleHistory("D", "D"), Strategy.DEFECT);
		
		Player player1 = new Player(new SingleLookupStrategy(lookup, history), "P1"); // TFT
		Player player2 = new Player(new DoubleLookupStrategy(doubleLookup, doubleHistory), "P2");
		
		System.out.println("Testing scoring function.. ");
		long startTime = System.nanoTime();
		double generalScore = lookup.getScore();
		long generalScoreTime = (System.nanoTime() - startTime) / 1000000;
		startTime = System.nanoTime();
		double generalDScore = doubleLookup.getScore();
		long generalDScoreTime = (System.nanoTime() - startTime) / 1000000;
		System.out.println(lookup);
		System.out.println("Score of Single Lookup (P1): " + generalScore + " (took " + generalScoreTime + " ms)\n");
		System.out.println(doubleLookup);
		System.out.println("Score of Double Lookup (P2): " + generalDScore + " (took " + generalDScoreTime + " ms)\n");

		// create a standard game (2 players) -> 50 rounds
		StandardGame game = new StandardGame(10, ScoringInfo.MATRIX);
		game.setPlayerA(player1);
		game.setPlayerB(player2);
		
		// play the game and store results
		GameResult results = game.play();

		// show detailed results, round by round
		double gameLog[][] = results.getLog();
		for (int i = 0; i < game.getNumberOfRounds(); i++){
			double p1Move = gameLog[0][i];
			double p2Move = gameLog[1][i];
			int p1Payoff = (int) ScoringInfo.MATRIX.getPayoff(p1Move, p2Move, 0);
			int p2Payoff = (int) ScoringInfo.MATRIX.getPayoff(p1Move, p2Move, 1);
			System.out.println("Round " + (i+1) + " -->  " + // round #
					"P1 (" + (p1Move==Strategy.COOPERATE ? "COOP":"DEFC") +
					"): " + p1Payoff +
					"\t-- P2 (" + (p2Move==Strategy.COOPERATE ? "COOP":"DEFC") + 
					"): " + p2Payoff);
		}
		System.out.println();
		
		// show general result info with toString()
		System.out.println(results);
	}
}
