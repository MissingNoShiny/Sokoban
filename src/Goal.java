
public class Goal implements Component{

	@Override
	public String getSpriteName() {
		return "Goal";
	}

	@Override
	public boolean canGoTrough() {
		return true;
	}
	
}
