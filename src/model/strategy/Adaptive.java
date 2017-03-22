
package model.strategy;

import ipdlx.Strategy;
import model.tool.History;
import model.tool.LookupArray1D;
	
public class Adaptive extends Strategy{
	LookupArray1D lookup;
	History opponentHistory;
	double moveForC;
	double bestScoreC = 0.0;
	double moveForD;
	double bestScoreD = 0.0;
	int move = 0;
	public Adaptive() {
		super("Adap", "Adaptive", "Starts with C,C,C,C,C,C,D,D,D,D,D and then takes choices which have given the best average score re-calculated after every move");
	}
	
	@Override
	public double getMove() {
		if (move < 6){ // true for first 6 moves
			if (lastResult > bestScoreC && move > 0){
				moveForC = opponentMove; // keep opponents move if had a better score
				bestScoreC = lastResult;
			}
			move++;
			return Strategy.COOPERATE;
		} else if (move < 11) { // true for next 5 moves
			if (move == 6) { // last move was coop (edge case)
				if (lastResult > bestScoreC){
					moveForC = opponentMove; // keep opponents move if had a better score
					bestScoreC = lastResult;
				}
			} else if (lastResult > bestScoreD){
				if (lastResult > bestScoreD){
					moveForC = opponentMove; // keep opponents move if had a better score
					bestScoreD = lastResult;
				}
			}
			move++;
			return Strategy.DEFECT;
		} else { // the rest of the moves
			move++;
			if (opponentMove == Strategy.COOPERATE)
				return moveForC;
			else
				return moveForD;
		}
	}
	
	public void reset(){
		opponentMove = (int) (Math.random() + 0.5); // reset to random
	}
}

