package be.ac.umons.info.sokoban.grid;

/**
 * A Component used to represent a goal.
 * @author Vincent Larcin, Joachim Sneessens
 */
class Goal implements Component{
	
	@Override
	public String getName() {
		return "Goal";
	}
	
	@Override
	public boolean canBePassedThrough() {
		return true;
	}
}
