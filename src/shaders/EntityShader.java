package shaders;

public class EntityShader extends Shader {
	
	private static final String VERTEX_FILE = "/shaders/entity/vertex.glsl";
	private static final String FRAGMENT_FILE = "/shaders/entity/fragment.glsl";
	
	private static final String ATTRIBUTE_POS = "pos";
	private static final String ATTRIBUTE_NORMAL = "normal";
	private static final String ATTRIBUTE_UV = "uv";
	
	private static final int MAX_LIGHTS = 10;
	
	public final UniformMatrix4f projectionViewMatrix = new UniformMatrix4f("projViewMatrix");
	public final UniformMatrix4f modelMatrix = new UniformMatrix4f("modelMatrix");
	
	public final UniformInt textureSampler = new UniformInt("tex");
	
	public final UniformVector3fArray lightPositions = new UniformVector3fArray("lightPositions", MAX_LIGHTS);
	public final UniformVector3fArray lightColors = new UniformVector3fArray("lightColors", MAX_LIGHTS);
	public final UniformInt numLights = new UniformInt("numLights");
	
	public final UniformVector3f cameraPos = new UniformVector3f("cameraPos");

	public EntityShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		registerUniforms(
				projectionViewMatrix,
				modelMatrix,
				textureSampler,
				lightPositions,
				lightColors,
				numLights,
				cameraPos
			);
	}

	public void bindAttributes() {
		bindToAttribute(0, ATTRIBUTE_POS);
		bindToAttribute(1, ATTRIBUTE_NORMAL);
		bindToAttribute(2, ATTRIBUTE_UV);
	}
	
}
