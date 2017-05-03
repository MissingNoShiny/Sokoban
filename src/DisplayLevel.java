
import java.awt.BorderLayout;
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

public class DisplayLevel extends JPanel{

	private static final long serialVersionUID = 4992322428530946741L;
	
	private static final Font localButtonsFont = new Font (Menu.fontName, 0, 50);
	
	/*
	 * J'ai du faire de displaygrid un attribut de displayLevel, pour lui donner le focus. Il faudra trouver 
	 * une meilleure solution
	 */
	public DisplayGrid displayGrid;
	
	
	public DisplayLevel(Grid grid, Game game) {
		setFocusable(true);
		setOpaque(false);
		setVisible(true);
		setLayout(new BorderLayout());
		
		displayGrid = new DisplayGrid(grid);
		add(displayGrid, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel(){
		
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				setBackground(Game.BLEU_CLAIR);
			}
		};
		//buttonsPanel.setOpaque(true);
		buttonsPanel.setPreferredSize(new Dimension(game.getWindow().getWidth()/6, game.getWindow().getHeight()));
		buttonsPanel.setLayout(new GridLayout(4, 1, 3, 3));
		
		add(buttonsPanel, BorderLayout.EAST);
		

		Button undoButton = new Button("Undo", Menu.defaultColor, localButtonsFont);
		undoButton.addMouseListener(new ButtonListener(undoButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				grid.getTracker().undo(grid);
			}
		});
		buttonsPanel.add(undoButton);
		
		Button resetButton = new Button("Reset", Menu.defaultColor, localButtonsFont);
		resetButton.addMouseListener(new ButtonListener(resetButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				grid.getTracker().reset(grid);
			}
		});
		buttonsPanel.add(resetButton);
		
		JFrame saveFrame = new JFrame();
		saveFrame.setSize(500, 300);
		saveFrame.setLocationRelativeTo(null); 
		saveFrame.setResizable(false);
		saveFrame.setLayout(new GridLayout(4,1));
		
		defaultLabel saveFrameLabel = new defaultLabel("Nom de la sauvegarde :");
		saveFrame.add(saveFrameLabel);
		
		defaultTextField saveFrameField = new defaultTextField();
		saveFrame.add(saveFrameField);
		
		Button cancelButton = new Button("Annuler", Menu.defaultColor, Menu.defaultFont);
		cancelButton.addMouseListener(new ButtonListener(cancelButton){
			@Override
			public void mouseReleased(MouseEvent e) {
				saveFrame.setVisible(false);
			}
		});
		
		Button validateButton = new Button("Valider", Menu.defaultColor, Menu.defaultFont);
		validateButton.addMouseListener(new ButtonListener(validateButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				String name = saveFrameField.getText();
				try {
					GridReader.saveGame(grid, name, game);
					System.out.println("Save reussie");
					saveFrame.setVisible(false);
				} catch (IOException e1) {
					System.out.println("Save impossible");
					e1.printStackTrace();
					//Creer JLabel pour avertir qu'un problème de sauvegarde a eu lieu
				};
			}
		});
		
		saveFrame.add(validateButton);
		saveFrame.add(cancelButton);
		
		Button saveButton = new Button("Save", Menu.defaultColor, localButtonsFont);
		saveButton.addMouseListener(new ButtonListener(saveButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				saveFrame.setVisible(true);
			}
		});
		buttonsPanel.add(saveButton);
		
		Button menuButton = new Button("Menu", Menu.defaultColor, localButtonsFont);
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
