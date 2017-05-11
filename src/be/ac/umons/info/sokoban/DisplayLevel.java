package be.ac.umons.info.sokoban;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
	 * For the focus
	 */
	public DisplayGrid displayGrid;
	
	private JOptionPane IOError = new JOptionPane();
	
	private JPanel buttonsPanel;
	
	private Game game;
	
	private int levelIndex;
	
	public DisplayLevel(final Grid grid, Game game, int levelIndex) {
		
		this.game = game;
		this.levelIndex = levelIndex;
		
		setFocusable(true);
		setOpaque(false);
		setVisible(true);
		setLayout(new BorderLayout());
		
		displayGrid = new DisplayGrid(grid, this);
		add(displayGrid, BorderLayout.CENTER);
		
		buttonsPanel = new JPanel();
		buttonsPanel.setBackground(Options.backGroundColor);
		buttonsPanel.setPreferredSize(new Dimension(game.getWindow().getWidth()/6, game.getWindow().getHeight()));
		buttonsPanel.setLayout(new GridLayout(4, 1, 3, 3));
		
		add(buttonsPanel, BorderLayout.EAST);
		

		Button undoButton = new Button("Undo", Options.buttonsColor, localButtonsFont);
		undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.getTracker().undo();		
			}
		});
		buttonsPanel.add(undoButton);
		
		Button resetButton = new Button("Reset", Options.buttonsColor, localButtonsFont);
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.getTracker().reset();
			}
		});
		buttonsPanel.add(resetButton);
		
		
		final JFrame saveFrame = new JFrame();
		saveFrame.setSize(500, 300);
		saveFrame.setLocationRelativeTo(null); 
		saveFrame.setResizable(false);
		saveFrame.setLayout(new GridLayout(4,1));
		
		DefaultLabel saveFrameLabel = new DefaultLabel("Save name:");
		saveFrame.add(saveFrameLabel);
		
		final JTextField saveFrameField = new JTextField();
		saveFrameField.setBackground(Options.buttonsColor);
		saveFrameField.setHorizontalAlignment(JTextField.CENTER);
		saveFrameField.setFont(Options.defaultFont);
		saveFrame.add(saveFrameField);
		
		Button cancelButton = new Button("Cancel", Options.buttonsColor, Options.defaultFont);
		cancelButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setEnabledButtons(true);
				saveFrame.setVisible(false);
			}
		});
		
		
		
		Button validateButton = new Button("Save", Options.buttonsColor, Options.defaultFont);
		validateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = saveFrameField.getText();
				if (name.length() > 10) {
					JOptionPane.showMessageDialog(IOError, "Number characters must be less than 10 !!!!!!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					File file = new File("../saves/" + name + ".xsb");
					int canOverrideSave = 0;
					if (file.exists()) 
						canOverrideSave = JOptionPane.showConfirmDialog(IOError, "Do you want to overwrite existing save?", "Warning",  JOptionPane.YES_NO_OPTION);
	
					if (canOverrideSave == 0) {
						try {
							GridReader.saveGridState(grid, name);
							setEnabledButtons(true);
							saveFrame.setVisible(false);
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(IOError, "Save failed", "Error", JOptionPane.ERROR_MESSAGE);
						};
					}
				}
			}
		});
		
		saveFrame.add(validateButton);
		saveFrame.add(cancelButton);
		
		Button saveButton = new Button("Save", Options.buttonsColor, localButtonsFont);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setEnabledButtons(false);
				saveFrame.setVisible(true);
			}
		});
		buttonsPanel.add(saveButton);
		
		Button MenuButton = new Button("Menu", Options.buttonsColor, localButtonsFont);
		MenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.loadMenu();
			}
		});
		buttonsPanel.add(MenuButton);
		
		final InfoPanel infoPanel = new InfoPanel(game, grid);
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
	
	public void displayVictoryScreen() {
		setEnabledButtons(false);
		JFrame victoryScreen = new JFrame();
		victoryScreen.setSize(500, 300);
		victoryScreen.setLocationRelativeTo(null);
		
		if (levelIndex > 0)
			victoryScreen.setLayout(new GridLayout(3,1));
		else
			victoryScreen.setLayout(new GridLayout(2,1));
		
		victoryScreen.add(new DefaultLabel("You win"));
		
		if (levelIndex > 0) {
			Button nextLevelButton = new Button("Next level", Options.buttonsColor, Options.defaultFont);
			nextLevelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						levelIndex++;
						game.loadLevel("levels/level "+levelIndex, true, levelIndex);
						victoryScreen.setVisible(false);
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(IOError, "Loading failed", "Error", JOptionPane.ERROR_MESSAGE);
					} catch (InvalidFileException e2) {
						JOptionPane.showMessageDialog(IOError, "Invalid File", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			victoryScreen.add(nextLevelButton);
		}
		
		
		Button MenuButton = new Button("Menu", Options.buttonsColor, Options.defaultFont);
		MenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.loadMenu();
				victoryScreen.setVisible(false);
			}
		});
		
		victoryScreen.add(MenuButton);
		
		victoryScreen.setVisible(true);
	}
	
	
	private void setEnabledButtons(boolean arg) {
		for (int i = 0; i < buttonsPanel.getComponentCount(); i++) {
			buttonsPanel.getComponent(i).setEnabled(arg);
		}
	}
}
