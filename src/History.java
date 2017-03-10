import java.util.Random;

import ipdlx.Strategy;

// History of moves encoded as binary for faster lookups
// Co-operate = 1 and Defect = 0 ( ex. "CCDDC" = 11001 = 25)
public class History {
	private static int MAX_LENGTH = 56;
	private long history; // only safe up to 56 moves
	private int length; // length of history
	private Random rand;
	
	// random history of size length
	public History(int length){
		if (length > MAX_LENGTH)
			this.length = MAX_LENGTH;
		else
			this.length = length;
		randomize();			
	}
	
	// string of moves ("CCDDCD"), newest move is last
	public History(String initialHistory){
		rand = new Random();
		if (initialHistory.length() > MAX_LENGTH){
			length = MAX_LENGTH;
			String trimmedHistory = initialHistory.substring(
					initialHistory.length()-MAX_LENGTH, initialHistory.length());
			history = stringToLong(trimmedHistory);
		} else {
			history = stringToLong(initialHistory);
			length = initialHistory.length();			
		}
	}
	
	public int length(){ return length; }
	
	public static String longToString(long history, int length){

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length - Long.numberOfLeadingZeros(history); i++)
			sb.append('D');
		String bString = Long.toBinaryString(history);
		for (int i = 0; i < bString.length(); i++){
			if (bString.charAt(i) == '0')
				sb.append('D');
			else
				sb.append('C');
		}
		return sb.toString();
	}
	
	private long stringToLong(String history){
		
		long binaryHistory = 0; // zero-fill
		int exp = 0; // power of 2
		// for each char, set bit to 1 if it's a 'C'
		for (int move = history.length()-1; move >= 0; move--){
			if (history.charAt(move) == 'C'){
				binaryHistory += Math.pow(2, exp);
			} 
			exp++;
		}
		return binaryHistory;
	}

	public void add(double move){
		history <<= 1; // left shift, now rightmost is 0 : 'D'		
		// have to clear top half since could overflow length
		trimToLength();		
		if (move == Strategy.COOPERATE)
			history++; // add 1 to set rightmost to 1 : 'C'	
	}
	
	// will clear the bits past the history
	private void trimToLength(){
		long mask;
		if (length > 31)
			mask = (long) Math.pow(2, length) - 1; // set cutoff bit and complement
		else
			mask = (1 << length) - 1;
		history &= mask;
	}
	
	public void flipMove(int numMovesFromLast){
		long mask;
		// left shift bit into place and xor to flip
		if (length > 31)
			mask = (long) Math.pow(2, numMovesFromLast);
		else
			mask = 1 << numMovesFromLast;
		history ^= mask;
		trimToLength();
	}
	
	public void flipRandomMove(){
		int random = rand.nextInt(length);
		flipMove(random);
	}
	
	public void randomize(){
		// generate some permutation of bits within length
		rand = new Random();
		history = rand.nextLong();
		trimToLength();
	}
	
	public long getValue(){
		return history;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		long rem = history%2;
		long div = history/2;
		while (div > 0){
			sb.insert(0, rem==1?'C':'D');
			rem = div%2;
			div = div/2;
		}
		sb.insert(0, rem==1?'C':'D');
		// pad with D's (for 0s)
		while (sb.length() != length){
			sb.insert(0, 'D');
		}
		return sb.toString();
	}

	
	@Override
	public boolean equals(Object o){
		if (o instanceof History){
			History h = (History) o;
			if (history == h.getValue())
				return true;
		}
		return false;
	}
	
//	public static void main(String args[]){
//		History h1 = new History("DCDCCCDCCDDDDCCDCCDDDDCCDDCCCDCCDDDDCCDDDDCCDDDCCCDCCDDCDDCDDDCDCCCD");
//		System.out.println(h1 + 
//				" | Binary: " + Long.toBinaryString(h1.history) + 
//				" | Length: " + h1.length + 
//				" | Long " + h1.history + 
//				" | Squared: " + (h1.history*h1.history));
//		
//		History h2 = new History(62);
//		System.out.println(h2 +  
//				" | Binary: " + Long.toBinaryString(h2.history) + 
//				" | Length: " + Long.toBinaryString(h2.history).length() + 
//				" | Long " + h2.history + 
//				" | Squared: " + (h2.history*h2.history));
//		
//		h2.add(Strategy.COOPERATE);
//		h2.add(Strategy.COOPERATE);
//		System.out.println(h2 + " after cooperating twice");
//		h2.add(Strategy.DEFECT);
//		System.out.println(h2 + " after defecting once");
//		
//		h2.flipMove(0);
//		System.out.println(h2 + " after flipping last move");
//		h2.flipMove(h2.length-1);
//		System.out.println(h2 + " after flipping earliest move");
//		h2.flipRandomMove();
//		System.out.println(h2 + " after flipping random move");
//		h2.randomize();
//		System.out.println(h2 + " after randomizing");
//	}
}
