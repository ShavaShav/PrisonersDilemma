package model.tool;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

import controller.LookupState;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ipdlx.GameMatrix;
import ipdlx.Strategy;

public interface Lookup extends Serializable{
	public static final long serialVersionUID = 1L;
	// randomize the results of the lookup table
	public abstract void randomize();
	// change a random action
	public abstract void flipRandomAction();
	public abstract void flipAction(LookupState lookupState);
	// get the score of the table to be used in the fitness function
	public abstract double getScore(GameMatrix payoffMatrix);
	public abstract double getScore(GameMatrix payoffMatrix, Strategy strategy);
	// save table to file
	public static void saveLookup(Lookup lookup, String path, String name){
		try {
			FileOutputStream fileOut = new FileOutputStream(path + "/" + name + ".ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);	
			out.writeObject(lookup);
			out.close();
			fileOut.close();
		} catch(FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	// load table from file
	public static Lookup loadLookup(String path, String name) {
		try {
			FileInputStream fileIn = new FileInputStream(path + "/" + name + ".ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);	
			Lookup lookup = (Lookup)in.readObject();
			in.close();
			fileIn.close();
			return lookup;
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch(FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
