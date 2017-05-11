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

public class DisplayLevel extends JPanel {

	private static final long serialVersionUID = 4992322428530946741L;
	
	private static final Font localButtonsFont = new Font (Options.fontName, 0, 50);
	
	/*
	 * For the focus
	 */
	public DisplayGrid displayGrid;
	
	private JOptionPane IOError = new JOptionPane();
	
	private JPanel buttonsPanel;
	
	private Game game;
	
	/**
	 * The index of the level if the level belong to the campaign.
	 * -1 if it is an loaded level.
	 * -2 if it is an generatedLevel
	 */
	private int levelIndex;
	
	/**
	 * Used for the save
	 */
	private String levelName;
	
	public DisplayLevel(final Grid grid, Game game, int levelIndex, String inputName) {
		
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
		buttonsPanel.setLayout(new GridLayout(3, 1, 3, 3));
		
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
		
		Button MenuButton = new Button("Menu", Options.buttonsColor, localButtonsFont);
		MenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (levelIndex >= 0)
					grid.getTracker().saveMov("levels/saved/level "+ levelIndex +".mov");
				else if (!levelName.equals("NOTHING"))
					grid.getTracker().saveMov("saves/" + levelName + ".mov");
				game.loadMenu();
			}
		});
		buttonsPanel.add(MenuButton);
		
		final InfoPanel infoPanel = new InfoPanel(grid);
		infoPanel.setPreferredSize(buttonsPanel.getPreferredSize());
		infoPanel.setOpaque(true);
		add(infoPanel, BorderLayout.WEST);
		
		if (levelIndex == -2) {
			setEnabledButtons(false);
			
			final JFrame saveFrame = new JFrame();
			saveFrame.setSize(600, 300);
			//saveFrame.setLocationRelativeTo(null); 
			saveFrame.setResizable(false);
			saveFrame.setLayout(new GridLayout(5,1));
			
			saveFrame.add(new DefaultLabel("Do you want to save this level?"));
			saveFrame.add(new DefaultLabel("Save name:"));
			
			final JTextField saveFrameField = new JTextField();
			saveFrameField.setBackground(Options.buttonsColor);
			saveFrameField.setHorizontalAlignment(JTextField.CENTER);
			saveFrameField.setFont(Options.defaultFont);
			saveFrame.add(saveFrameField);
			
			Button cancelButton = new Button("Cancel", Options.buttonsColor, Options.defaultFont);
			cancelButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					levelName = "NOTHING";
					setEnabledButtons(true);
					saveFrame.setVisible(false);
				}
			});
			
			
			
			Button validateButton = new Button("Save", Options.buttonsColor, Options.defaultFont);
			validateButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String saveName = saveFrameField.getText();
					if (saveName.length() > 10) 
						JOptionPane.showMessageDialog(IOError, "Number characters must be less than 10 !!!!!!", "Error", JOptionPane.ERROR_MESSAGE);
					else if (saveName.equals("NOTHING"))
						JOptionPane.showMessageDialog(IOError, "Please change your save choice.", "Error", JOptionPane.ERROR_MESSAGE);
					else {
						File file = new File("saves/" + saveName + ".xsb");
						int canOverrideSave = 0;
						if (file.exists()) 
							canOverrideSave = JOptionPane.showConfirmDialog(IOError, "Do you want to overwrite existing save?", "Warning",  JOptionPane.YES_NO_OPTION);
		
						if (canOverrideSave == 0) {
							GridReader.saveGrid(grid, saveName);
							levelName = saveName;
							setEnabledButtons(true);
							saveFrame.setVisible(false);;
						}
					}
				}
			});
			
			saveFrame.add(validateButton);
			saveFrame.add(cancelButton);
			
			saveFrame.setVisible(true);
		}
		else 
			levelName = inputName;

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
		victoryScreen.setResizable(false);
		victoryScreen.setLocationRelativeTo(null);
		
		if (levelIndex > 0 && levelIndex < 25)
			victoryScreen.setLayout(new GridLayout(3,1));
		else
			victoryScreen.setLayout(new GridLayout(2,1));
		
		victoryScreen.add(new DefaultLabel("You win"));
		
		if (levelIndex > 0 && levelIndex < 25) {
			Button nextLevelButton = new Button("Next level", Options.buttonsColor, Options.defaultFont);
			nextLevelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						levelIndex++;
						game.loadLevel("levels/level "+levelIndex, levelIndex, "campaignLevel");
						victoryScreen.setVisible(false);
					} catch (IOException | InvalidFileException e1) {
						JOptionPane.showMessageDialog(IOError, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
