
public interface SingleLookup extends Lookup {
	public abstract double getAction(History history);
	public abstract void flipAction(History history);
	public abstract void setAction(History history, double action);
}
