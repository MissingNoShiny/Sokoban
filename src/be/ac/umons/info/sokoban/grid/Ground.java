package be.ac.umons.info.sokoban.grid;

/**
 * A Component used to represent ground.
 * @author Vincent Larcin, Joachim Sneessens
 */
class Ground implements Component{

	@Override
	public String getName() {
		return "Ground";
	}	
	
	@Override
	public boolean canBePassedThrough() {
		return true;
	}
}
