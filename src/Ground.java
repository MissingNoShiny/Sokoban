
public class Ground implements Component{

	@Override
	public String getSpriteName() {
		return "Ground";
	}

	@Override
	public boolean canGoTrough() {
		return true;
	}

}
