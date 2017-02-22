import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

import ipdlx.*;
import ipdlx.gui.*;

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
		tourney.addPlayer(player1);
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
