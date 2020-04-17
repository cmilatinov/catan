package shaders;

public class StaticShader extends Shader {
	
	private static final String VERTEX_FILE = "./shaders/entity/vertex.glsl";
	private static final String FRAGMENT_FILE = "./shaders/entity/fragment.glsl";

	public StaticShader(String vertexFile, String fragmentFile) {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void bindAttributes() {
		
	}
	
}
