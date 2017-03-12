package model.tool;

public interface DoubleLookup extends Lookup{
	public abstract double getAction(DoubleHistory doubleHistory);
	public abstract void flipAction(DoubleHistory doubleHistory);
	public abstract void flipAction(long hist1_index, long hist2_index);
	public abstract void setAction(DoubleHistory doubleHistory, double action);
	public abstract int getFirstLength();
	public abstract int getSecondLength();
}
