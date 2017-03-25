
public class Goal implements Component{

	@Override
	public String getSpriteName() {
		return getClass().getSimpleName();
	}

	@Override
	public boolean canGoTrough() {
		return true;
	}
	
}
