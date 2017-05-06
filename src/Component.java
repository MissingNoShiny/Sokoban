
/**
 * An abstract class from which inherit all components of the game.
 * @author Vincent Larcin, Joachim Sneessens
 */
public abstract class Component {

	/**
	 * Gets the name of the class.
	 * @return the name of the class
	 */
	public String getName() {
		return getClass().getSimpleName();
	}
	
	/**
	 * Checks if the Component can be passed through by a Player.
	 * @return true if the Component can be passed through, false else
	 */
	public boolean canBePassedThrough() {
		return true;
	};
}
