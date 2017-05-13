package be.ac.umons.info.sokoban.grid;

/**
 * A Component used to represent a wall.
 * @author Vincent Larcin, Joachim Sneessens
 */
class Wall implements Component {

	@Override
	public String getName() {
		return "Wall";
	}

	@Override
	public boolean canBePassedThrough() {
		return false;
	}

}
