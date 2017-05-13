package be.ac.umons.info.sokoban.grid;

/**
 * A Component that has dynamic coordinates, which therefore need to be tracked.
 * @author Vincent Larcin, Joachim Sneessens
 */
abstract class Position extends Point implements Component {
	
	/**
	 * The Grid object which contains the position.
	 */
	protected Grid grid;
	
	/**
	 * Creates a position with the specified coordinates in input.
	 * @param inputX The X-coordinate of the object
	 * @param inputY The Y-coordinate of the object
	 */
	Position(Grid grid, int xInput, int yInput) {
		super(xInput, yInput);
		this.grid = grid;
	}

	@Override
	public boolean canBePassedThrough() {
		return false;
	}
}
