
public class DeadlockDetector {
	
	/**
	 * This enum is private to prevent the function isFrozen from being called with a non-null frozenAxis argument.
	 */
	private enum Axis {
		X,
		Y;
	}
	
	/**
	 * Constructor is private to prevent instantiations.
	 */
	private DeadlockDetector() {
		
	}
	
	/**
	 * Detects if a crate is frozen.
	 * @param crate The crate to test.
	 * @param grid The grid containing the crate.
	 * @param frozenAxis The axis the function knows is locked by another crate. Must be null when the function is called.
	 * @return true is the crate is frozen, false else.
	 */
	public static boolean isFrozen(Crate crate, Grid grid, Axis frozenAxis) throws IllegalArgumentException {
		if (!grid.getCrateList().contains(crate))
			throw new IllegalArgumentException("Specified crate is not part of specified grid");
		boolean x = false, y = false;
		if (
				frozenAxis == Axis.X
				||crate.getX() == 0 
				|| (grid.hasCrateAt(crate.getX() - 1, crate.getY()) && DeadlockDetector.isFrozen(grid.getCrateAt(crate.getX() - 1, crate.getY()), grid, Axis.X))
				|| grid.getComponentAt(crate.getX() - 1, crate.getY()).getName().equals("Wall")
				|| crate.getX() == grid.getWidth() - 1
				|| (grid.hasCrateAt(crate.getX() + 1, crate.getY()) && DeadlockDetector.isFrozen(grid.getCrateAt(crate.getX() + 1, crate.getY()), grid, Axis.X))
				|| grid.getComponentAt(crate.getX() + 1, crate.getY()).getName().equals("Wall")
				)
			x = true;
		if (
				frozenAxis == Axis.Y
				||crate.getY() == 0 
				|| (grid.hasCrateAt(crate.getX(), crate.getY() - 1) && DeadlockDetector.isFrozen(grid.getCrateAt(crate.getX(), crate.getY() - 1), grid, Axis.Y))
				|| grid.getComponentAt(crate.getX(), crate.getY() - 1).getName().equals("Wall")
				|| crate.getY() == grid.getHeight() - 1
				|| (grid.hasCrateAt(crate.getX(), crate.getY() + 1) && DeadlockDetector.isFrozen(grid.getCrateAt(crate.getX(), crate.getY() + 1), grid, Axis.Y))
				|| grid.getComponentAt(crate.getX(), crate.getY() + 1).getName().equals("Wall")
				)
			y = true;
		if (x == true && y == true)
			return true;
		return false;
	}
}
