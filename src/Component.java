public abstract class Component {
	
	public Component() {
		
	}
	
	public String getSpriteName() {
		return getClass().getSimpleName();
	}	
}
