package shaders;

public abstract class Uniform {
	
	protected String name;
	
	public Uniform(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract void retrieveLocation(int programID);
	
}
