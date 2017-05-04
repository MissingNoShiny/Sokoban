
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DisplayLevel extends JPanel{

	private static final long serialVersionUID = 4992322428530946741L;
	
	private static final Font localButtonsFont = new Font (Options.fontName, 0, 50);
	
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
				setBackground(Options.backGroundColor);
			}
		};
		//buttonsPanel.setOpaque(true);
		buttonsPanel.setPreferredSize(new Dimension(game.getWindow().getWidth()/6, game.getWindow().getHeight()));
		buttonsPanel.setLayout(new GridLayout(4, 1, 3, 3));
		
		add(buttonsPanel, BorderLayout.EAST);
		

		Button undoButton = new Button("Undo", Options.buttonsColor, localButtonsFont);
		undoButton.addMouseListener(new ButtonListener(undoButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				grid.getTracker().undo(grid);
			}
		});
		buttonsPanel.add(undoButton);
		
		Button resetButton = new Button("Reset", Options.buttonsColor, localButtonsFont);
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
		
		DefaultLabel saveFrameLabel = new DefaultLabel("Nom de la sauvegarde :");
		saveFrame.add(saveFrameLabel);
		
		JTextField saveFrameField = new JTextField();
		saveFrameField.setBackground(Options.buttonsColor);
		saveFrameField.setHorizontalAlignment(JTextField.CENTER);
		saveFrameField.setFont(Options.defaultFont);
		saveFrame.add(saveFrameField);
		
		Button cancelButton = new Button("Annuler", Options.buttonsColor, Options.defaultFont);
		cancelButton.addMouseListener(new ButtonListener(cancelButton){
			@Override
			public void mouseReleased(MouseEvent e) {
				saveFrame.setVisible(false);
			}
		});
		
		JOptionPane saveGestion = new JOptionPane();
		
		Button validateButton = new Button("Valider", Options.buttonsColor, Options.defaultFont);
		validateButton.addMouseListener(new ButtonListener(validateButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				String name = saveFrameField.getText();
				File file = new File("../saves/" + name + ".xsb");
				int canOverrideSave = 0;
				if (file.exists()) {
					canOverrideSave = JOptionPane.showConfirmDialog(saveGestion, "Do you want to overwrite existing save?", "Save name already used",  JOptionPane.YES_NO_OPTION);
				}
				if (canOverrideSave == 0) {
					try {
						GridReader.saveGame(grid, name);
						saveFrame.setVisible(false);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(saveGestion, "Save failed", "Save failed", JOptionPane.ERROR_MESSAGE);
					};
				}
			}
		});
		
		saveFrame.add(validateButton);
		saveFrame.add(cancelButton);
		
		Button saveButton = new Button("Save", Options.buttonsColor, localButtonsFont);
		saveButton.addMouseListener(new ButtonListener(saveButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				saveFrame.setVisible(true);
			}
		});
		buttonsPanel.add(saveButton);
		
		Button OptionsButton = new Button("Options", Options.buttonsColor, localButtonsFont);
		OptionsButton.addMouseListener(new ButtonListener(OptionsButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				game.loadMenu();
			}
		});
		buttonsPanel.add(OptionsButton);
		
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
