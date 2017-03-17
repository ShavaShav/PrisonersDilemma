package model.tool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logging {
	public static boolean ON = true;
	private static FileWriter writer;
	private static boolean writerOpened;
	public static int dec = 2;
	
	public static void close(){
		if (writerOpened){
			try {
				writer.flush();
				writer.close();
				writerOpened = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("File already closed for writing");
		}
	}
	
	// open file for logging
	public static void open(String name) {
		if (ON & !writerOpened){
			try {
				File file = new File("./data/"+name+".txt");
				file.createNewFile();
				writer = new FileWriter(file);
				writerOpened = true;
			} catch (IOException e) {
				e.printStackTrace();
			}			
		} else {
			System.out.println("File already opened for writing, or logging is turned off");
		}
		
	}
	
	public static void print(String s){
		if (ON){
			if (writerOpened){
				try {
					writer.write(s);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				// to console
				System.out.print(s);
			}
		}
	}
	
	public static void println(String s){
		if (ON){
			if (writerOpened){
				try {
					writer.write(s+"\n");
				//	writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				// to console
				System.out.println(s);
			}
		}
	}
	
	
	public static void init(){
		//calc decimal places for logging
		dec = 0;
		int acc = (int) ScoringInfo.ACCURACY;
		while (acc > 1){
			acc /= 10;
			dec++;
		}
		
		// turn on
		ON = true;
	}
}
