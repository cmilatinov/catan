package objects;

import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

public class GameObjectFactory {

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

		return loadMesh(indices, vertices, normals, uvs);

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
		vao.addVBO(-1, indicesVBO);

		// Store vertex data in attribute list 0
		vao.storeFloatData(0, vertices, 3, GL_STATIC_DRAW);

		// Unbind VAO
		vao.unbind();

		// Return new mesh object
		return new Mesh(vao, indices.length);

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
		vao.addVBO(-1, indicesVBO);

		// Store vertex data in attribute list 0
		vao.storeFloatData(0, vertices, 3, GL_STATIC_DRAW);

		// Store normal data in attribute list 1
		vao.storeFloatData(1, normals, 3, GL_STATIC_DRAW);

		// Store uv data in attribute list 2
		vao.storeFloatData(2, uvs, 2, GL_STATIC_DRAW);

		// Unbind VAO
		vao.unbind();

		// Return new mesh object
		return new Mesh(vao, indices.length);

	}

	/**
	 * Loads a texture from a file into a texture object.
	 * 
	 * @param filename The file name.
	 * @param mipmap   Whether or not to generate a mipmap for the texture.
	 * @return [{@link Texture}] Null if the texture could not be loaded, otherwise
	 *         returns the texture object corresponding to the loaded texture.
	 */
	public Texture loadTexture2D(String filename, int filtering, boolean mipmap) {
		try {

			// Load the file
			BufferedImage img = ImageIO.read(new File("./textures/" + filename));

			// Generate a new OpenGL texture
			Texture result = null;
			int texture = glGenTextures();

			// Bind it
			glBindTexture(GL_TEXTURE_2D, texture);

			// Retrieve the pixel data
			int[] pixels = new int[img.getWidth() * img.getHeight()];
			img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());

			// Allocate a new buffer
			ByteBuffer buffer = ByteBuffer
					.allocateDirect(img.getWidth() * img.getHeight() * 4);

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
				result = new Texture(GL_TEXTURE_2D, texture);
				return result;
			}

			// Generate a mipmap with the right parameters
			glGenerateMipmap(GL_TEXTURE_2D);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4f);

			// Return the resulting texture
			result = new Texture(GL_TEXTURE_2D, texture);
			return result;

		} catch (IOException e) {
			return null;
		}
	}

}
