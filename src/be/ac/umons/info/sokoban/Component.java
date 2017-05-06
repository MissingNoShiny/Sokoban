package be.ac.umons.info.sokoban;

public interface Component {

	/**
	 * Gets the name of the class.
	 * @return the name of the class
	 */
	public default String getName() {
		return getClass().getSimpleName();
	}
	
	/**
	 * Checks if the Component can be passed through by a Player or a Crate.
	 * @return true if the Component can be passed through, false else
	 */
	public default boolean canBePassedThrough(){
		return false;
	}
}
