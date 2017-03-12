package controller;

public class LookupState {
	private int lookupIndex1;
	private int lookupIndex2;	 // for 1D arrays, this should not be used
	private double score;
	
	public LookupState() {
		lookupIndex1 = -1; 
		lookupIndex2 = -1;
		score = -1.0;
	}
	
	public void setRow(int row) { lookupIndex1 = row; }
	public void setCol(int col) { lookupIndex2 = col; }
	public void setScore(double score) { this.score = score; }
	
	public int getRow(){
		return lookupIndex1;
	}
	
	public int getCol(){
		return lookupIndex2;
	}
	
	public double getScore(){
		return score;
	}
}
