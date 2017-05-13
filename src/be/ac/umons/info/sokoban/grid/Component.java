package be.ac.umons.info.sokoban.grid;

interface Component {

	/**
	 * Gets the name of the class.
	 * @return the name of the class
	 */
	public String getName();
	
	/**
	 * Checks if the Component can be passed through by a Player or a Crate.
	 * @return true if the Component can be passed through, false else
	 */
	public boolean canBePassedThrough();
}
