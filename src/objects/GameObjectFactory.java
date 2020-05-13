package objects;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

public class GameObjectFactory implements GameObject {

	private final ArrayList<GameObject> cleanup = new ArrayList<GameObject>();

	/**
	 * Loads a mesh from an OBJ file.
	 * 
	 * @param path The path to the OBJ file.
	 * @return [{@link Mesh}] Null if unable to parse the file, the loaded mesh
	 *         object otherwise.
	 */
	public Mesh loadOBJ(String path) {
		AIScene scene = Assimp.aiImportFile(path, Assimp.aiProcess_Triangulate | Assimp.aiProcess_FlipUVs);

		if (scene.mNumMeshes() <= 0)
			return null;

		AIMesh aiMesh = AIMesh.create(scene.mMeshes().get(0));

		int[] indices = new int[aiMesh.mNumFaces() * 3];
		for (int i = 0; i < aiMesh.mNumFaces(); i++) {
			AIFace face = aiMesh.mFaces().get(i);
			indices[(i * 3)] = face.mIndices().get(0);
			indices[(i * 3) + 1] = face.mIndices().get(1);
			indices[(i * 3) + 2] = face.mIndices().get(2);
		}

		float[] vertices = new float[aiMesh.mNumVertices() * 3];
		for (int i = 0; i < aiMesh.mNumVertices(); i++) {
			AIVector3D vec = aiMesh.mVertices().get(i);
			vertices[(i * 3)] = vec.x();
			vertices[(i * 3) + 1] = vec.y();
			vertices[(i * 3) + 2] = vec.z();
		}

		float[] normals = new float[aiMesh.mNumVertices() * 3];
		for (int i = 0; i < aiMesh.mNumVertices(); i++) {
			AIVector3D vec = aiMesh.mNormals().get(i);
			normals[(i * 3)] = vec.x();
			normals[(i * 3) + 1] = vec.y();
			normals[(i * 3) + 2] = vec.z();
		}

		float[] uvs = new float[aiMesh.mNumVertices() * 2];
		for (int i = 0; i < aiMesh.mNumVertices(); i++) {
			AIVector3D vec = aiMesh.mTextureCoords(0).get(i);
			uvs[(i * 2)] = vec.x();
			uvs[(i * 2) + 1] = vec.y();
		}

		return addToCleanup(loadMesh(indices, vertices, normals, uvs));
	}

	/**
	 * Loads a mesh object using the given data.
	 * 
	 * @param indices  The index list of the mesh.
	 * @param vertices The vertex list of the mesh, in XYZ format.
	 * @param normals  The normal list of the mesh, in XYZ format.
	 * @param uvs      The texture coordinate list of the mesh, in UV format.
	 * @return [{@link Mesh}] The resulting mesh object.
	 */
	public Mesh loadMesh(int[] indices, float[] vertices, float[] normals, float[] uvs) {

		// Create VAO
		VAO vao = VAO.create().bind();

		// Create indices VBO
		VBO indicesVBO = VBO.create(GL_ELEMENT_ARRAY_BUFFER).bind().store(indices, GL_STATIC_DRAW);

		// Add indices VBO to VAO
		vao.addVBO(indicesVBO);

		// Store vertex data in attribute list 0
		vao.storeFloatData(0, vertices, 3, GL_STATIC_DRAW);

		// Store normal data in attribute list 1
		vao.storeFloatData(1, normals, 3, GL_STATIC_DRAW);

		// Store uv data in attribute list 2
		vao.storeFloatData(2, uvs, 2, GL_STATIC_DRAW);

		// Unbind VAO
		vao.unbind();

		// Return new mesh object
		return addToCleanup(new Mesh(vao, indices.length));

	}

	/**
	 * Loads a mesh object using the given data.
	 * 
	 * @param indices  The index list of the mesh.
	 * @param vertices The vertex list of the mesh, in XYZ format.
	 * @param uvs      The texture coordinate list of the mesh, in UV format.
	 * @return [{@link Mesh}] The resulting mesh object.
	 */
	public Mesh loadMesh(int[] indices, float[] vertices, float[] uvs) {

		// Create VAO
		VAO vao = VAO.create().bind();

		// Create indices VBO
		VBO indicesVBO = VBO.create(GL_ELEMENT_ARRAY_BUFFER).bind().store(indices, GL_STATIC_DRAW);

		// Add indices VBO to VAO
		vao.addVBO(indicesVBO);

		// Store vertex data in attribute list 0
		vao.storeFloatData(0, vertices, 3, GL_STATIC_DRAW);

		// Store uv data in attribute list 1
		vao.storeFloatData(1, uvs, 2, GL_STATIC_DRAW);

		// Unbind VAO
		vao.unbind();

		// Return new mesh object
		return addToCleanup(new Mesh(vao, indices.length));

	}

	/**
	 * Loads a mesh object using the given data.
	 * 
	 * @param indices  The index list of the mesh.
	 * @param vertices The vertex list of the mesh, in XYZ format.
	 * @return [{@link Mesh}] The resulting mesh object.
	 */
	public Mesh loadMesh(int[] indices, float[] vertices) {

		// Create VAO
		VAO vao = VAO.create().bind();

		// Create indices VBO
		VBO indicesVBO = VBO.create(GL_ELEMENT_ARRAY_BUFFER).bind().store(indices, GL_STATIC_DRAW);

		// Add indices VBO to VAO
		vao.addVBO(indicesVBO);

		// Store vertex data in attribute list 0
		vao.storeFloatData(0, vertices, 3, GL_STATIC_DRAW);

		// Unbind VAO
		vao.unbind();

		// Return new mesh object
		return addToCleanup(new Mesh(vao, indices.length));

	}

	/**
	 * Loads a 2D texture from a file into a texture object.
	 * 
	 * @param filepath  Path to the bitmap file.
	 * @param filtering The filtering strategy to use when scaling the texture.
	 * @param mipmap    Whether or not to generate a mipmap for the texture.
	 * @return [{@link Texture}] Null if the texture could not be loaded, otherwise
	 *         returns the texture object corresponding to the loaded bitmap.
	 */
	public Texture loadTexture2D(String filepath, int filtering, boolean mipmap) {
		try {

			// Load the file
			BufferedImage img = ImageIO.read(new File(filepath));

			// Generate a new OpenGL texture
			int texture = glGenTextures();

			// Bind it
			glBindTexture(GL_TEXTURE_2D, texture);

			// Retrieve the pixel data
			int[] pixels = new int[img.getWidth() * img.getHeight()];
			img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());

			// Allocate a new buffer
			ByteBuffer buffer = ByteBuffer.allocateDirect(img.getWidth() * img.getHeight() * 4);

			// Put in the pixel data in the bytebuffer correctly
			for (int y = 0; y < img.getHeight(); y++) {
				for (int x = 0; x < img.getWidth(); x++) {
					int pixel = pixels[y * img.getWidth() + x];
					buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
					buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green component
					buffer.put((byte) (pixel & 0xFF)); // Blue component
					buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component. Only for RGBA
				}
			}

			// Flip the buffer to be read
			buffer.flip();

			// Load the texture with the pixel data
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.getWidth(), img.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
					buffer);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filtering);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filtering);

			// If no mipmap needed, return the resulting texture
			if (!mipmap) {
				Texture t = new Texture(GL_TEXTURE_2D, texture);
				cleanup.add(t);
				return t;
			}

			// Generate a mipmap with the right parameters
			glGenerateMipmap(GL_TEXTURE_2D);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4f);

			// Return the resulting texture
			return addToCleanup(new Texture(GL_TEXTURE_2D, texture));

		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Loads a cube map texture from six files into a texture object.
	 * 
	 * @param filepath Path to the folder containing the six textures. Textures must
	 *                 be named 'skybox0.jpg' through 'skybox5.jpg'.
	 * @return [{@link Texture}] Null if any of the textures could not be loaded, otherwise
	 *         returns the texture object corresponding to the loaded cube map.
	 */
	public Texture loadTextureCubeMap(String filepath) {

		try {

			// Generate a new OpenGL texture
			int texture = glGenTextures();

			// Bind it
			glBindTexture(GL_TEXTURE_CUBE_MAP, texture);

			for (int i = 0; i < 6; i++) {

				// Load the file
				BufferedImage img = ImageIO.read(new File(filepath + "/skybox" + i + ".jpg"));

				// Retrieve the pixel data
				int[] pixels = new int[img.getWidth() * img.getHeight()];
				img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());

				// Allocate a new buffer
				ByteBuffer buffer = ByteBuffer.allocateDirect(img.getWidth() * img.getHeight() * 4);

				// Put in the pixel data in the bytebuffer correctly
				for (int y = 0; y < img.getHeight(); y++) {
					for (int x = 0; x < img.getWidth(); x++) {
						int pixel = pixels[y * img.getWidth() + x];
						buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
						buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green component
						buffer.put((byte) (pixel & 0xFF)); // Blue component
						buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component. Only for RGBA
					}
				}

				// Flip the buffer to be read
				buffer.flip();

				// Load the texture with the pixel data
				glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, img.getWidth(), img.getHeight(), 0,
						GL_RGBA, GL_UNSIGNED_BYTE, buffer);

			}

			glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);

			return new Texture(GL_TEXTURE_CUBE_MAP, texture);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Generates a plane with the given number of subdivisions.
	 * 
	 * @param subdivisions The number of subdivisions to create the plane with.
	 * @return [{@link Mesh}] The resulting mesh.
	 */
	public Mesh generatePlane(int subdivisions) {

		float[] vertices = new float[(subdivisions + 1) * (subdivisions + 1) * 2];
		int[] indices = new int[subdivisions * subdivisions * 6];

		for (int j = 0; j < subdivisions + 1; j++)
			for (int i = 0; i < subdivisions + 1; i++) {
				vertices[2 * (i + (subdivisions + 1) * j)] = i;
				vertices[2 * (i + (subdivisions + 1) * j) + 1] = j;
			}

		for (int j = 0; j < subdivisions; j++)
			for (int i = 0; i < subdivisions; i++) {
				// First face
				indices[6 * (i + subdivisions * j)] = i + 1 + (subdivisions + 1) * j;
				indices[6 * (i + subdivisions * j) + 1] = i + (subdivisions + 1) * j;
				indices[6 * (i + subdivisions * j) + 2] = i + 1 + (subdivisions + 1) * (j + 1);

				// Second face
				indices[6 * (i + subdivisions * j) + 3] = i + (subdivisions + 1) * (j + 1);
				indices[6 * (i + subdivisions * j) + 4] = i + 1 + (subdivisions + 1) * (j + 1);
				indices[6 * (i + subdivisions * j) + 5] = i + (subdivisions + 1) * j;
			}

		// Create VAO
		VAO vao = VAO.create().bind();

		// Create indices VBO
		VBO indicesVBO = VBO.create(GL_ELEMENT_ARRAY_BUFFER).bind().store(indices, GL_STATIC_DRAW);

		// Add indices VBO to VAO
		vao.addVBO(indicesVBO);

		// Store vertex data in attribute list 0
		vao.storeFloatData(0, vertices, 2, GL_STATIC_DRAW);

		// Unbind VAO
		vao.unbind();

		return addToCleanup(new Mesh(vao, indices.length));
	}

	/**
	 * {@inheritDoc}
	 */
	public void destroy() {
		for (GameObject g : cleanup)
			g.destroy();
	}

	@SuppressWarnings("unchecked")
	private <T extends GameObject> T addToCleanup(GameObject g) {
		cleanup.add(g);
		return (T) g;
	}

}
