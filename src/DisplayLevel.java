
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JPanel;

public class DisplayLevel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4992322428530946741L;
	
	/*
	 * J'ai du faire de displaygrid un attribut de displayLevel, pour lui donner le focus. Il faudra trouver 
	 * une meilleure solution
	 */
	public DisplayGrid displayGrid;
	
	/**
	 * TODO
	 * @param grid The Grid object to get data from
	 * @param game
	 */
	
	public DisplayLevel(Grid grid, Game game) {
		setFocusable(true);
		setOpaque(false);
		setVisible(true);
		setLayout(new BorderLayout());
		
		displayGrid = new DisplayGrid(grid);
		add(displayGrid, BorderLayout.CENTER);
		//displayGrid.requestFocusInWindow();
		
		JPanel buttonsPanel = new JPanel(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				setBackground(Game.BLEU_CLAIR);
			}
		};
		buttonsPanel.setOpaque(true);
		buttonsPanel.setPreferredSize(new Dimension(game.getWindow().getWidth()/6, game.getWindow().getHeight()));
		
		add(buttonsPanel, BorderLayout.EAST);
		GridLayout gridLayout = new GridLayout(4,1);
		gridLayout.setVgap(4);
		buttonsPanel.setLayout(gridLayout);
		

		Button undoButton = new Button("Undo", Color.orange, 50);
		undoButton.addMouseListener(new ButtonListener(undoButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				grid.getTracker().undo(grid);
			}
		});
		buttonsPanel.add(undoButton);
		
		Button resetButton = new Button("Reset", Color.orange, 50);
		resetButton.addMouseListener(new ButtonListener(resetButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				grid.getTracker().reset(grid);
			}
		});
		buttonsPanel.add(resetButton);
		
		Button saveButton = new Button("Save", Color.orange, 50);
		saveButton.addMouseListener(new ButtonListener(saveButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				try {
					GridReader.saveGame(grid, "pomme", game);
					System.out.println("Save reussie");
				} catch (IOException e1) {
					System.out.println("Save impossible");
					e1.printStackTrace();
					//Creer JLabel pour avertir qu'un probl�me de sauvegarde a eu lieu
				};
			}
		});
		buttonsPanel.add(saveButton);
		
		Button menuButton = new Button("Menu", Color.orange, 50);
		menuButton.addMouseListener(new ButtonListener(menuButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				game.loadMenu();
			}
		});
		buttonsPanel.add(menuButton);
		
		InfoPanel infoPanel = new InfoPanel(game, grid);
		infoPanel.setPreferredSize(buttonsPanel.getPreferredSize());
		infoPanel.setOpaque(true);
		add(infoPanel, BorderLayout.WEST);
		
		addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent arg0) {
				
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				
			}

			@Override
			public void componentResized(ComponentEvent e) {
				buttonsPanel.setPreferredSize(new Dimension(game.getWindow().getWidth()/6, game.getWindow().getHeight()));
				infoPanel.setPreferredSize(buttonsPanel.getPreferredSize());
			}

			@Override
			public void componentShown(ComponentEvent e) {
				
			}
		});
	}
}
