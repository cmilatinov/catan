package shaders;

public class EntityShader extends Shader {
	
	private static final String VERTEX_FILE = "/shaders/entity/vertex.glsl";
	private static final String FRAGMENT_FILE = "/shaders/entity/fragment.glsl";
	
	private static final String ATTRIBUTE_POS = "pos";
	private static final String ATTRIBUTE_NORMAL = "normal";
	private static final String ATTRIBUTE_UV = "uv";
	
	public final UniformMatrix4f projectionViewMatrix = new UniformMatrix4f("projViewMatrix");
	public final UniformMatrix4f modelMatrix = new UniformMatrix4f("modelMatrix");
	public final UniformInt textureSampler = new UniformInt("tex");

	public EntityShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		registerUniforms(
				projectionViewMatrix,
				modelMatrix,
				textureSampler
			);
	}

	public void bindAttributes() {
		bindToAttribute(0, ATTRIBUTE_POS);
		bindToAttribute(1, ATTRIBUTE_NORMAL);
		bindToAttribute(2, ATTRIBUTE_UV);
	}
	
}
