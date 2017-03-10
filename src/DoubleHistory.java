
public class DoubleHistory {
	History playerHistory;
	History opponentHistory;
	
	public DoubleHistory(int playerHistoryLength, int opponentHistoryLength){
		this.playerHistory = new History(playerHistoryLength);
		this.opponentHistory = new History(playerHistoryLength);
	}
	
	public DoubleHistory(String playerHistory, String opponentHistory){
		this.playerHistory = new History(playerHistory);
		this.opponentHistory = new History(opponentHistory);
	}
	
	public DoubleHistory(String playerHistory, int opponentHistoryLength){
		this.playerHistory = new History(playerHistory);
		this.opponentHistory = new History(opponentHistoryLength);
	}
	
	public DoubleHistory(int playerHistoryLength, String opponentHistory){
		this.playerHistory = new History(playerHistoryLength);
		this.opponentHistory = new History(opponentHistory);
	}
	
	public int playerLength(){ return playerHistory.length(); }
	public int opponentLength(){ return opponentHistory.length(); }
	
	public History getPlayerHistory(){
		return playerHistory;
	}
	
	public History getOpponentHistory(){
		return opponentHistory;
	}
	
	public void flipMove(int numMovesFromLast, boolean playerMove){
		if (playerMove){
			playerHistory.flipMove(numMovesFromLast);
		} else {
			opponentHistory.flipMove(numMovesFromLast);
		}
	}
	
	public void flipRandomMove(){
		if (Math.random() > 0.5){
			playerHistory.flipRandomMove();
		} else {
			opponentHistory.flipRandomMove();
		}
	}
	
	public void randomize(){
		playerHistory.randomize();
		opponentHistory.randomize();
	}
	
	public long getPlayerValue(){
		return playerHistory.getValue();
	}
	
	public long getOpponentValue(){
		return opponentHistory.getValue();
	}
	
	public void add(double playerMove, double opponentMove){
		playerHistory.add(playerMove);
		opponentHistory.add(opponentMove);
	}
	
	public String toString(){
		return playerHistory + " | " + opponentHistory; 
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof DoubleHistory){
			DoubleHistory dh = (DoubleHistory) o;
			if (playerHistory.equals(dh.playerHistory) &&
					opponentHistory.equals(dh.opponentHistory))
				return true;
		}
		return false;
	}

//	public static void main(String args[]){
//		DoubleHistory h1 = new DoubleHistory("CDCCDDDDCCDDDCCCCCCDDDDC", 12354);
//		System.out.println(h1);
//
//		
//		
//		h1.add(Strategy.COOPERATE, Strategy.DEFECT);
//		h1.add(Strategy.COOPERATE, Strategy.DEFECT);
//		System.out.println(h1 + " after cooperating twice, opponent defecting twice");
//		h1.add(Strategy.DEFECT, Strategy.DEFECT);
//		System.out.println(h1 + " after defecting once, opponent defecting again");
//		
//		h1.flipMove(0, false);
//		System.out.println(h1 + " after flipping opponent's last move");
//		h1.flipMove(h1.playerLength()-1, true);
//		System.out.println(h1 + " after flipping player's earliest move");
//		h1.flipRandomMove();
//		System.out.println(h1 + " after flipping random move");
//		h1.randomize();
//		System.out.println(h1 + " after randomizing");
//	}
}
