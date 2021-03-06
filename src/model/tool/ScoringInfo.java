package model.tool;

import ipdlx.GameMatrix;
import ipdlx.Strategy;
import ipdlx.strategy.ALLC;
import ipdlx.strategy.ALLD;
import ipdlx.strategy.NEG;
import ipdlx.strategy.Pavlov;
import ipdlx.strategy.STFT;
import ipdlx.strategy.TFT;
import ipdlx.strategy.TFTT;
import model.strategy.TROLL;

// Used by lookup tables to calculate the score.
public class ScoringInfo {
	// these values seem to work good for evaluation
	// lowering accuracy for hill climber can help to "smooth" hill (more sideways moves)
	public static final double ACCURACY = 100.0;  // add a 0 to add a decimal place, rounds the score
	public static final int NUM_ROUNDS = 25; // number of rounds played per games
	public static final int NUM_GAMES = 50; // number of games played against strategy
	public static final GameMatrix MATRIX = new GameMatrix(GameMatrix.defaultPayoffMatrix2x2);
	// Training set -> Strategies that rely on randomness shouldn't be used for scoring
	public static final Strategy[] STRATEGIES = {
		new ALLC(), new ALLD(), new NEG(), new TROLL(),
		new Pavlov(), new STFT(), new TFT(), new TFTT()
	};
}
