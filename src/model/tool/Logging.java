package model.tool;

public class Logging {
	public static boolean ON = true;
	public static boolean TO_FILE = false;
	
	public static void print(String s){
		if (ON){
			if (TO_FILE){
				
			} else {
				// to console
				System.out.print(s);
			}
		}
	}
	
	public static void println(String s){
		if (ON){
			if (TO_FILE){
				
			} else {
				// to console
				System.out.println(s);
			}
		}
	}
}
