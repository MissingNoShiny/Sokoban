package be.ac.umons.info.sokoban;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LevelDisplay extends JPanel {

	private static final long serialVersionUID = 4992322428530946741L;
	
	/*
	 * For the focus
	 */
	public GridDisplay displayGrid;
	
	private JOptionPane IOError = new JOptionPane();
	
	private JPanel buttonsPanel;
	
	private Grid grid;
	
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
	
	private class InfoPanel extends JPanel{

		private static final long serialVersionUID = 6195740163330975520L;		
		/**
		 * The JLabel that displays the current moves count.
		 */
		private DefaultLabel movesCount;
			
		/**
		 * The JLabel that displays the current pushes count.
		 */
		private DefaultLabel pushesCount;
		
		public InfoPanel(Grid grid) {
			setBackground(Options.getBackgroundColor());
			movesCount = new DefaultLabel("", Options.getBackgroundColor());
			add(movesCount);
			pushesCount = new DefaultLabel("", Options.getBackgroundColor());
			add(pushesCount);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			movesCount.setText("Moves: " + grid.getTracker().getMovesCount());
			pushesCount.setText("Pushes: " + grid.getTracker().getPushesCount());
		}
	}
	
	
	public LevelDisplay(Grid grid, Game game, int levelIndex, String inputName) {
		
		this.grid = grid;
		this.game = game;
		this.levelIndex = levelIndex;
		
		setFocusable(true);
		setOpaque(false);
		setVisible(true);
		setLayout(new BorderLayout());
		
		displayGrid = new GridDisplay(grid, this);
		add(displayGrid, BorderLayout.CENTER);
		
		buttonsPanel = new JPanel();
		buttonsPanel.setBackground(Options.getBackgroundColor());
		buttonsPanel.setPreferredSize(new Dimension(game.getWindow().getWidth()/6, game.getWindow().getHeight()));
		buttonsPanel.setLayout(new GridLayout(3, 1, 3, 3));
		
		add(buttonsPanel, BorderLayout.EAST);

		Button undoButton = new Button("Undo", Options.getButtonColor(), Options.bigFont);
		undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.getTracker().undo();		
			}
		});
		buttonsPanel.add(undoButton);
		
		Button resetButton = new Button("Reset", Options.getButtonColor(), Options.bigFont);
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.getTracker().reset();
			}
		});
		buttonsPanel.add(resetButton);
		
		Button MenuButton = new Button("Menu", Options.getButtonColor(), Options.bigFont);
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
		
		//If the level is an generate level
		if (levelIndex == -2) {
			setEnabledButtons(false);
			
			final DefaultFrame saveFrame = new DefaultFrame("", 600, 300);
			saveFrame.setLayout(new GridLayout(5,1));
			
			saveFrame.add(new DefaultLabel("Do you want to save this level?"));
			saveFrame.add(new DefaultLabel("Save name:"));
			
			final JTextField saveFrameField = new JTextField();
			saveFrameField.setBackground(Options.getButtonColor());
			saveFrameField.setHorizontalAlignment(JTextField.CENTER);
			saveFrameField.setFont(Options.littleFont);
			saveFrame.add(saveFrameField);
			
			Button cancelButton = new Button("Cancel", Options.getButtonColor(), Options.littleFont);
			cancelButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					levelName = "NOTHING";
					setEnabledButtons(true);
					saveFrame.setVisible(false);
				}
			});
			
			
			
			Button validateButton = new Button("Save", Options.getButtonColor(), Options.littleFont);
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
	}
	
	public void displayVictoryScreen() {
		setEnabledButtons(false);
		DefaultFrame victoryScreen = new DefaultFrame("Victory !", 500, 300);
		
		if (levelIndex > 0 && levelIndex < 25)
			victoryScreen.setLayout(new GridLayout(3,1));
		else
			victoryScreen.setLayout(new GridLayout(2,1));
		
		victoryScreen.add(new DefaultLabel("You win"));
		
		if (levelIndex > 0 && levelIndex < 25) {
			Button nextLevelButton = new Button("Next level", Options.getButtonColor(), Options.littleFont);
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
		
		
		Button MenuButton = new Button("Menu", Options.getButtonColor(), Options.littleFont);
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
