package model.tool;

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
	protected static final double ACCURACY = 100.0;  // 2 decimal places
	protected static final int NUM_ROUNDS = 20; // number of rounds played per games
	protected static final int NUM_GAMES = 50; // number of games played against strategy
	// Strategies that rely on randomness shouldn't be used for scoring
	protected static final Strategy[] STRATEGIES = {
		new ALLC(), new ALLD(), new NEG(), new TROLL(),
		new Pavlov(), new STFT(), new TFT(), new TFTT()
	};
}
