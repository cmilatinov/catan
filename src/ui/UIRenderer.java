package ui;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import display.Window;
import objects.Mesh;
import objects.VAO;
import objects.VBO;
import shaders.ui.UIShader;

public class UIRenderer {
	
	private static final int[] UI_MESH_INDICES = {
		0, 1, 2,
		2, 3, 0
	};
	
	private static final float[] UI_MESH_VERTICES = {
		-1, -1,
		1, -1,
		1, 1,
		-1, 1,
	};
	
	private static final float[] UI_MESH_UVS = {
		0, 1,
		1, 1,
		1, 0,
		0, 0
	};
	
	private static final int MAX_INSTANCES = 1000;
	private static final int DATA_LENGTH = 25;
	
	private final Window window;
	private final Mesh mesh;
	private final VBO instanceVBO;
	private final UIShader shader;
	
	public UIRenderer(Window window) {
		this.window = window;
		this.instanceVBO = VBO.create(GL_ARRAY_BUFFER);
		this.mesh = initMesh(instanceVBO);
		this.shader = new UIShader();
	}
	
	private Mesh initMesh(VBO instanceVBO) {
		
		// Vao
		VAO vao = VAO.create().bind();

		// Create indices VBO
		VBO indicesVBO = VBO.create(GL_ELEMENT_ARRAY_BUFFER).bind().store(UI_MESH_INDICES, GL_STATIC_DRAW);

		// Add indices VBO to VAO
		vao.addVBO(indicesVBO);

		// Store vertex data in attribute list 0
		vao.storeFloatData(0, UI_MESH_VERTICES, 2, GL_STATIC_DRAW);

		// Store uv data in attribute list 1
		vao.storeFloatData(1, UI_MESH_UVS, 2, GL_STATIC_DRAW);
		
		// Create instanced VBO
		instanceVBO.bind().allocate(MAX_INSTANCES * DATA_LENGTH * Float.BYTES, GL_DYNAMIC_DRAW);
		
		// Add instanced VBO to VAO
		vao.addVBO(instanceVBO);
		
		// Create instanced attributes
		
		// Model matrix
		vao.addInstancedAttribute(2, 4, GL_FLOAT, DATA_LENGTH * Float.BYTES, 0);
		vao.addInstancedAttribute(3, 4, GL_FLOAT, DATA_LENGTH * Float.BYTES, 4 * Float.BYTES);
		vao.addInstancedAttribute(4, 4, GL_FLOAT, DATA_LENGTH * Float.BYTES, 8 * Float.BYTES);
		vao.addInstancedAttribute(5, 4, GL_FLOAT, DATA_LENGTH * Float.BYTES, 12 * Float.BYTES);
		
		// Quad dimensions
		vao.addInstancedAttribute(6, 4, GL_FLOAT, DATA_LENGTH * Float.BYTES, 16 * Float.BYTES);
		
		// Color
		vao.addInstancedAttribute(7, 4, GL_FLOAT, DATA_LENGTH * Float.BYTES, 20 * Float.BYTES);
		
		// Border radius
		vao.addInstancedAttribute(8, 1, GL_FLOAT, DATA_LENGTH * Float.BYTES, 24 * Float.BYTES);
		
		// Unbind VAO and store mesh
		return new Mesh(vao.unbind(), UI_MESH_INDICES.length);
		
	}
	
	public void render(UIComponent root) {
		
		// Screen size
		int width = window.getWidth();
		int height = window.getHeight();
		
		// Mesh vao
		VAO vao = mesh.getVAO();
		
		// Create list of quads to be rendered
		List<UIQuad> quadsToRender = new ArrayList<UIQuad>();
		collectForRender(quadsToRender, root);
		
		if(quadsToRender.size() <= 0)
			return;
		
		quadsToRender.sort((a, b) -> a.getDimensions().getElevation() - b.getDimensions().getElevation());
		
		// Allocate float array and fill it with data
		float[] instanceData = new float[quadsToRender.size() * DATA_LENGTH];
		for (int i = 0; i < quadsToRender.size(); i++) {
			
			UIQuad quad = quadsToRender.get(i);
			
			Matrix4f modelMatrix = quad.getDimensions().computeModelMatrix(width, height);
			instanceData[(i * DATA_LENGTH)] = modelMatrix.m00();
			instanceData[(i * DATA_LENGTH ) + 1] = modelMatrix.m01();
			instanceData[(i * DATA_LENGTH) + 2] = modelMatrix.m02();
			instanceData[(i * DATA_LENGTH) + 3] = modelMatrix.m03();
			instanceData[(i * DATA_LENGTH) + 4] = modelMatrix.m10();
			instanceData[(i * DATA_LENGTH) + 5] = modelMatrix.m11();
			instanceData[(i * DATA_LENGTH) + 6] = modelMatrix.m12();
			instanceData[(i * DATA_LENGTH) + 7] = modelMatrix.m13();
			instanceData[(i * DATA_LENGTH) + 8] = modelMatrix.m20();
			instanceData[(i * DATA_LENGTH) + 9] = modelMatrix.m21();
			instanceData[(i * DATA_LENGTH) + 10] = modelMatrix.m22();
			instanceData[(i * DATA_LENGTH) + 11] = modelMatrix.m23();
			instanceData[(i * DATA_LENGTH) + 12] = modelMatrix.m30();
			instanceData[(i * DATA_LENGTH) + 13] = modelMatrix.m31();
			instanceData[(i * DATA_LENGTH) + 14] = modelMatrix.m32();
			instanceData[(i * DATA_LENGTH) + 15] = modelMatrix.m33();
			
			UIDimensions dimensions = quad.getDimensions();
			instanceData[(i * DATA_LENGTH) + 16] = dimensions.getX();
			instanceData[(i * DATA_LENGTH) + 17] = dimensions.getY();
			instanceData[(i * DATA_LENGTH) + 18] = dimensions.getWidth();
			instanceData[(i * DATA_LENGTH) + 19] = dimensions.getHeight();
			
			UIColor color = quad.getColor();
			instanceData[(i * DATA_LENGTH) + 20] = color.getR();
			instanceData[(i * DATA_LENGTH) + 21] = color.getG();
			instanceData[(i * DATA_LENGTH) + 22] = color.getB();
			instanceData[(i * DATA_LENGTH) + 23] = color.getA();
			
			instanceData[(i * DATA_LENGTH) + 24] = quad.getBorderRadius();
		}
		
		// Update the VBO
		instanceVBO.bind().storeSubData(0, instanceData);
		
		// Load uniforms
		shader.use();
		shader.screenSize.set(new Vector2f(width, height));
		
		// Bind VAO
		vao.bind(0, 1, 2, 3, 4, 5, 6, 7, 8);
		
		// Draw meshes
		mesh.drawInstanced(quadsToRender.size());
		
		// Unbind VAO
		vao.unbind(0, 1, 2, 3, 4, 5, 6, 7, 8);
		
		// Stop shader
		shader.stop();

	}
	
	private void collectForRender(List<UIQuad> quads, UIComponent component) {
		
		if (component.getClass() == UIQuad.class && component.isVisible())
			quads.add((UIQuad)component);
		
		for (UIComponent child : component.children)
			collectForRender(quads, child);
		
	}
	
}
