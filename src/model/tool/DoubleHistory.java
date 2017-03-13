package model.tool;

public class DoubleHistory {
	protected History history1;
	protected History history2;
	
	public DoubleHistory(int history1Length, int history2Length){
		this.history1 = new History(history1Length);
		this.history2 = new History(history2Length);
	}
	
	public DoubleHistory(String history1, String history2){
		this.history1 = new History(history1);
		this.history2 = new History(history2);
	}
	
	public DoubleHistory(String history1, int history2Length){
		this.history1 = new History(history1);
		this.history2 = new History(history2Length);
	}
	
	public DoubleHistory(int history1Length, String history2){
		this.history1 = new History(history1Length);
		this.history2 = new History(history2);
	}
	
	public int history1Length(){ return history1.length(); }
	public int history2Length(){ return history2.length(); }
	
	public void flipMove(int numMovesFromLast, boolean playerMove){
		if (playerMove){
			history1.flipMove(numMovesFromLast);
		} else {
			history2.flipMove(numMovesFromLast);
		}
	}
	
	public void flipRandomMove(){
		if (Math.random() > 0.5){
			history1.flipRandomMove();
		} else {
			history2.flipRandomMove();
		}
	}
	
	public void randomize(){
		history1.randomize();
		history2.randomize();
	}
	
	public long getHistory1Value(){
		return history1.getValue();
	}
	
	public long getHistory2Value(){
		return history2.getValue();
	}
	
	public void add(double playerMove, double opponentMove){
		history1.add(playerMove);
		history2.add(opponentMove);
	}
	
	public String toString(){
		return history1 + " | " + history2; 
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof DoubleHistory){
			DoubleHistory dh = (DoubleHistory) o;
			if (history1.equals(dh.history1) &&
					history2.equals(dh.history2))
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
