package ui;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

import java.util.*;
import java.util.stream.Collectors;

import objects.Texture;
import org.joml.Matrix4f;

import display.Window;
import objects.Mesh;
import objects.VAO;
import objects.VBO;
import shaders.ui.ShaderUI;
import shaders.uisprite.ShaderUISprite;

interface TypeCheck {
	public boolean instanceOf();
}

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

	private final ShaderUI shader;
	private final ShaderUISprite imageShader;

	private final Map<UIText, Texture> textTextures = new HashMap<UIText, Texture>();
	
	public UIRenderer(Window window) {
		this.window = window;
		this.instanceVBO = VBO.create(GL_ARRAY_BUFFER);
		this.mesh = initMesh(instanceVBO);
		this.shader = new ShaderUI();
		this.imageShader = new ShaderUISprite();

		imageShader.use();
		imageShader.textureSampler.set(0);
		imageShader.stop();
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

		// Create list of quads to be rendered
		var quadsToRender = root.children
				.stream()
				.flatMap(UIComponent::flatten)
				.filter(uiComponent -> uiComponent instanceof UIQuad)
				.map(uiComponent -> (UIQuad)uiComponent)
				.collect(Collectors.toList());
		if (root instanceof UIQuad) {
			quadsToRender.add((UIQuad) root);
		}
		// Render process for quads
		if (quadsToRender.size() > 0)
			renderQuads(quadsToRender);

		// Create list of sprites to be rendered
		List<UISprite> spritesToRender = root.children
				.stream()
				.flatMap(UIComponent::flatten)
				.filter(component -> component instanceof UISprite)
				.map(uiComponent -> (UISprite)uiComponent)
				.collect(Collectors.toList());
		if (root instanceof UISprite) {
			spritesToRender.add((UISprite)root);
		}
		// Render process for sprites
		if (spritesToRender.size() > 0)
			renderSprites(spritesToRender);

		// Create list of texts to be rendered
		List<UIText> textsToRender = root.children
				.stream()
				.flatMap(UIComponent::flatten)
				.filter(component -> component instanceof UIText)
				.map(uiComponent -> (UIText)uiComponent)
				.collect(Collectors.toList());
		if (root instanceof UIText) {
			textsToRender.add((UIText) root);
		}
		// Render process for texts
		if (textsToRender.size() > 0)
			renderTexts(textsToRender);
	}

	private void renderQuads(List<UIQuad> quadsToRender) {

		// Screen size
		int width = window.getWidth();
		int height = window.getHeight();

		// Mesh vao
		VAO vao = mesh.getVAO();

		// Sort quads by elevation
		quadsToRender.sort(Comparator.comparingInt(a -> a.getDimensions().getElevation()));

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

		// Use shader
		shader.use();

		// Update the VBO
		instanceVBO.bind().storeSubData(0, instanceData);

		// Bind VAO
		vao.bind(0, 1, 2, 3, 4, 5, 6, 7, 8);

		// Draw meshes
		mesh.drawInstanced(quadsToRender.size());

		// Unbind VAO
		vao.unbind(0, 1, 2, 3, 4, 5, 6, 7, 8);

		// Stop shader
		shader.stop();

	}

	private void renderSprites(List<UISprite> spritesToRender) {

		// Screen size
		int width = window.getWidth();
		int height = window.getHeight();

		// Mesh vao
		VAO vao = mesh.getVAO();

		// Sort sprites by elevation
		spritesToRender.sort(Comparator.comparingInt(a -> a.getDimensions().getElevation()));

		// Use the shader
		imageShader.use();

		for (UISprite sprite : spritesToRender) {

			// Load shader uniforms
			imageShader.modelMatrix.set(sprite.dimensions.computeModelMatrix(width, height));
			imageShader.textureFormat.set(ShaderUISprite.IMAGE_FORMAT_RGBA);

			// Bind VAO
			vao.bind(0, 1);

			// Bind texture
			sprite.getTexture().bindToUnit(0);

			// Draw mesh
			mesh.draw();

			// Unbind VAO
			vao.unbind(0, 1);

		}

		// Stop the shader
		imageShader.stop();

	}

	private void renderTexts(List<UIText> textsToRender) {

		// Screen size
		int width = window.getWidth();
		int height = window.getHeight();

		// Mesh vao
		VAO vao = mesh.getVAO();

		// Sort texts by elevation
		textsToRender.sort(Comparator.comparingInt(a -> a.getDimensions().getElevation()));

		// Use the shader
		imageShader.use();

		for (UIText text : textsToRender) {

			// Create a new texture or get the old one
			Texture texture;
			if (!textTextures.containsKey(text)) {
				texture = Texture.create2D(text.getDimensions().getWidth(), text.getDimensions().getHeight(), GL_LINEAR);
				textTextures.put(text, texture);
			} else
				texture = textTextures.get(text);

			// Update the texture if necessary
			if (text.shouldUpdateImage) {
				text.drawImageToTexture(texture);
				text.shouldUpdateImage = false;
			}

			// Load shader uniforms
			imageShader.modelMatrix.set(text.dimensions.computeModelMatrix(width, height));
			imageShader.textureFormat.set(ShaderUISprite.IMAGE_FORMAT_ARGB);

			// Bind VAO
			vao.bind(0, 1);

			// Bind texture
			texture.bindToUnit(0);

			// Draw mesh
			mesh.draw();

			// Unbind VAO
			vao.unbind(0, 1);

		}

		// Stop the shader
		imageShader.stop();

	}
}
