package model.tool;

public interface DoubleLookup extends Lookup{
	public abstract double getAction(DoubleHistory doubleHistory);
	public abstract void flipAction(DoubleHistory doubleHistory);
	public abstract void setAction(DoubleHistory doubleHistory, double action);
}
