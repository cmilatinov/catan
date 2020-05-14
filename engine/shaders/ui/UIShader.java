package shaders.ui;

import shaders.Shader;
import shaders.UniformInt;
import shaders.UniformVector2f;

public class UIShader extends Shader {

	private static final String VERTEX_FILE = "/shaders/ui/vertex.glsl";
	private static final String FRAGMENT_FILE = "/shaders/ui/fragment.glsl";
	
	private static final String ATTRIBUTE_POS = "pos";
	private static final String ATTRIBUTE_UV = "uv";
	private static final String ATTRIBUTE_MODEL_MATRIX = "modelMatrix";
	private static final String ATTRIBUTE_DIMENSIONS = "dimensions";
	private static final String ATTRIBUTE_COLOR = "color";
	private static final String ATRRIBUTE_BORDER_RADIUS = "borderRadius";
	
	public final UniformVector2f screenSize = new UniformVector2f("screenSize");
	
	public final UniformInt textureSampler = new UniformInt("tex");

	public UIShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		registerUniforms(
				screenSize,
				textureSampler
			);
	}

	public void bindAttributes() {
		bindToAttribute(0, ATTRIBUTE_POS);
		bindToAttribute(1, ATTRIBUTE_UV);
		bindToAttribute(2, ATTRIBUTE_MODEL_MATRIX);
		bindToAttribute(6, ATTRIBUTE_DIMENSIONS);
		bindToAttribute(7, ATTRIBUTE_COLOR);
		bindToAttribute(8, ATRRIBUTE_BORDER_RADIUS);
	}

}
