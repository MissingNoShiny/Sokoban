
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
		
		JFrame saveName = new JFrame();
		saveName.setSize(300, 300);
		saveName.setLayout(new GridLayout(3,1));
		JTextField saveNameField = new JTextField("save1");
		saveNameField.setBackground(Color.orange);
		saveNameField.setFont(new Font("arial", 0, 40));
		saveName.add(saveNameField);
		
		Button cancelButton = new Button("Annuler", Color.orange, 40);
		cancelButton.addMouseListener(new ButtonListener(cancelButton){
			@Override
			public void mouseReleased(MouseEvent e) {
				saveName.setVisible(false);
			}
		});
		
		Button validateButton = new Button("Valider", Color.orange, 40);
		validateButton.addMouseListener(new ButtonListener(validateButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				String name = saveNameField.getText();
				try {
					GridReader.saveGame(grid, name, game);
					System.out.println("Save reussie");
					saveName.setVisible(false);
				} catch (IOException e1) {
					System.out.println("Save impossible");
					e1.printStackTrace();
					//Creer JLabel pour avertir qu'un problème de sauvegarde a eu lieu
				};
			}
		});
		
		saveName.add(validateButton);
		saveName.add(cancelButton);
		
		Button saveButton = new Button("Save", Color.orange, 50);
		saveButton.addMouseListener(new ButtonListener(saveButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				saveName.setVisible(true);
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
