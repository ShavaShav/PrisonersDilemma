import ipdlx.*;
import ipdlx.strategy.*;

/*
 *  Test of a 2-player game
 */
public class StandardGameTest {
	public static void main(String args[]){
		System.out.println(Strategy.COOPERATE);
		// create payoff matrix
		double p_array[][] = GameMatrix.defaultPayoffMatrix2x2;
		GameMatrix payoffMatrix = new GameMatrix(p_array);
		System.out.println(payoffMatrix);
		
		// create players with strategies
		// create a TFTT from table lookup
		SingleLookup lookup = new LookupArray1D(2); // 2 moves
		lookup.setAction(new History("CC"), Strategy.COOPERATE);
		lookup.setAction(new History("CD"), Strategy.COOPERATE);
		lookup.setAction(new History("DC"), Strategy.COOPERATE);
		lookup.setAction(new History("DD"), Strategy.DEFECT);
		History history = new History("CC"); // random history
		
		Player player1 = new Player(new SingleLookupStrategy(lookup, history), "P1"); // TFTT
		Player player2 = new Player(new TFT(), "P2"); // TFT
		
		// create a standard game (2 players) -> 50 rounds
		StandardGame game = new StandardGame(50, payoffMatrix);
		game.setPlayerA(player1);
		game.setPlayerB(player2);
		
		// play the game and store results
		GameResult results = game.play();

		// show detailed results, round by round
		double gameLog[][] = results.getLog();
		for (int i = 0; i < game.getNumberOfRounds(); i++){
			double p1Move = gameLog[0][i];
			double p2Move = gameLog[1][i];
			int p1Payoff = (int) payoffMatrix.getPayoff(p1Move, p2Move, 0);
			int p2Payoff = (int) payoffMatrix.getPayoff(p1Move, p2Move, 1);
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
