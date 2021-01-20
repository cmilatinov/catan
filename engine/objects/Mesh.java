package objects;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;

/**
 * Represents a mesh with vertex, face, and normal data.
 */
public class Mesh implements GameResource {

	/**
	 * The underlying OpenGL VAO structure.
	 */
	private final VAO vao;

	/**
	 * The total number of vertices in the mesh. This number corresponds to the number of indices if this is an indexed mesh.
	 */
	private final int vertexCount;

	/**
	 * Creates a new mesh with the given VAO and vertex count.
	 * 
	 * @param vao The VAO used to describe this mesh.
	 * @param vertexCount  The number of vertices present in the mesh.
	 */
	public Mesh(VAO vao, int vertexCount) {
		this.vao = vao;
		this.vertexCount = vertexCount;
	}

	/**
	 * Returns the VAO backing this mesh.
	 * 
	 * @return {@link VAO} The VAO describing this mesh.
	 */
	public VAO getVAO() {
		return vao;
	}

	/**
	 * Returns this mesh's vertex count.
	 * 
	 * @return <b>int</b> The vertex count of this mesh.
	 */
	public int getVertexCount() {
		return vertexCount;
	}

	/**
	 * Renders this mesh to the current framebuffer.
	 */
	public void draw() {
		glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
	}

	/**
	 * Renders this mesh to the current framebuffer.
	 * 
	 * @param instances The number of instances of the mesh to render.
	 */
	public void drawInstanced(int instances) {
		glDrawElementsInstanced(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0, instances);
	}

	/**
	 * {@inheritDoc}
	 */
	public void destroy() {
		vao.destroy();
	}

}
