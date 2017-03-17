import controller.GA;
import model.tool.Logging;
import model.tool.Lookup;

public class GATest {
	
	public static void main(String args[]){
		Logging.ON = false;
		GA gene = new GA(3, 3); // single history (length of 3 moves)
		GA.DEBUG = true;
		// must be at least 3 individuals
		Lookup fittest = gene.evolve(100, 100, 1); // pop size, generations, numchildren
		System.out.println("Best Score (rejudge): " + fittest.getScore());
	}

}
