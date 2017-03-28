
public class Wall implements Component{

	@Override
	public String getSpriteName() {
		return "Wall";
	}

	@Override
	public boolean canGoTrough() {
		return false;
	}

}
