package shaders.skybox;

import shaders.Shader;
import shaders.uniform.UniformInt;
import shaders.uniform.UniformMatrix4f;

public class ShaderSkybox extends Shader {

	private static final String VERTEX_FILE = "/shaders/skybox/vertex.glsl";
	private static final String FRAGMENT_FILE = "/shaders/skybox/fragment.glsl";
	
	private static final String ATTRIBUTE_POS = "pos";
	
	public final UniformMatrix4f projectionViewMatrix = new UniformMatrix4f("projViewMatrix");
	
	public final UniformInt textureSampler = new UniformInt("tex");

	public ShaderSkybox() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		registerUniforms(
				projectionViewMatrix,
				textureSampler
			);
	}

	public void bindAttributes() {
		bindToAttribute(0, ATTRIBUTE_POS);
	}
	
}
