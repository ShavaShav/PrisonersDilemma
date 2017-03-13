
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

import ipdlx.*;
import ipdlx.gui.*;
import model.strategy.BestHillClimb;
import model.strategy.SingleLookupStrategy;
import model.strategy.TROLL;
import model.tool.History;
import model.tool.Lookup;
import model.tool.SingleLookup;

/*
 *  Tournament allows for large game with all possible
 *  combinations of players (including self v self)
 *  Also provides gui components
 */
public class TournamentTest {

	public static void main(String[] args) {
		// create tournament
		Tournament tourney = new Tournament(new StandardGame());
		// create players with strategies
		Player player1 = new Player(new TROLL(), "Player");
		// add the best hill climb lookup table
		Player hillClimber = new Player(new BestHillClimb(), "Hill Climber");
		
		tourney.addPlayer(player1);
		tourney.addPlayer(hillClimber);
		// create panel
		TournamentPanel tourneyPanel = new TournamentPanel(tourney);
		// display gui
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame frame = new JFrame("Prisoner's Dilemma");
					frame.setContentPane(tourneyPanel);
					frame.setSize(new Dimension(1200, 800));
					frame.setVisible(true);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
