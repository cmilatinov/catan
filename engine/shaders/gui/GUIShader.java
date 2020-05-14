package shaders.gui;

import shaders.Shader;
import shaders.UniformInt;
import shaders.UniformMatrix4f;

public class GUIShader extends Shader {

	private static final String VERTEX_FILE = "/shaders/gui/vertex.glsl";
	private static final String FRAGMENT_FILE = "/shaders/gui/fragment.glsl";
	
	private static final String ATTRIBUTE_POS = "pos";
	private static final String ATTRIBUTE_UV = "uv";
	
	public final UniformMatrix4f projectionMatrix = new UniformMatrix4f("projectionMatrix");
	public final UniformMatrix4f modelMatrix = new UniformMatrix4f("modelMatrix");
	
	public final UniformInt textureSampler = new UniformInt("tex");

	public GUIShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		registerUniforms(
				projectionMatrix,
				modelMatrix,
				textureSampler
			);
	}

	public void bindAttributes() {
		bindToAttribute(0, ATTRIBUTE_POS);
		bindToAttribute(1, ATTRIBUTE_UV);
	}

}
