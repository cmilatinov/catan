package objects;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glVertexAttribIPointer;

import java.util.HashMap;

/**
 * Represents a Vertex Array Object (VAO).
 */
public class VAO {

	/**
	 * The VAO's ID.
	 */
	private int id;

	/**
	 * The VBOs serving as attributes of the VAO.
	 */
	private HashMap<Integer, VBO> attributes = new HashMap<Integer, VBO>();

	/**
	 * Creates a Vertex Array Object (VAO) with the specified ID.
	 * 
	 * @param id The ID of the VAO.
	 */
	public VAO(int id) {
		this.id = id;
	}

	/**
	 * Binds the VAO for use and manipulation.
	 * 
	 * @return [{@link VAO}] This same instance of the class.
	 */
	public VAO bind() {
		glBindVertexArray(id);
		return this;
	}

	/**
	 * Unbinds the VAO.
	 * 
	 * @return [{@link VAO}] This same instance of the class.
	 */
	public VAO unbind() {
		glBindVertexArray(0);
		return this;
	}

	/**
	 * Binds the VAO as the current VAO and enables the use of the specified
	 * attribute lists.
	 * 
	 * @param attribs One or more attribute list indices.
	 * @return [{@link VAO}] This same instance of the class.
	 */
	public VAO bind(int... attribs) {
		bind();
		for (int i : attribs)
			glEnableVertexAttribArray(i);
		return this;
	}

	/**
	 * Disables the use of the specified attribute lists.
	 * 
	 * @param attribs One or more attribute list indices.
	 * @return [{@link VAO}] This same instance of the class.
	 */
	public void unbind(int... attribs) {
		for (int i : attribs)
			glDisableVertexAttribArray(i);
		unbind();
	}

	/**
	 * Returns the ID of the VAO.
	 * 
	 * @return [<b>int</b>] The ID of the VAO.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Store a floating-point array of data as a vertex attribute in the VAO.
	 * 
	 * @param attribIndex The attribute list index in which to store the data.
	 * @param data        The data to store.
	 * @param vectorSize  The size of a single vertex worth of data (eg. 3 for a 3D
	 *                    position).
	 * @param usage       The purpose of the attribute list (eg.
	 *                    {@link #GL_STATIC_DRAW} or {@link #GL_DYNAMIC_DRAW}).
	 * @return [{@link VAO}] This same instance of the class.
	 */
	public VAO storeFloatData(int attribIndex, float[] data, int vectorSize, int usage) {

		// Create the VBO.
		VBO buffer = VBO.create(GL_ARRAY_BUFFER);

		// Bind the buffer.
		buffer.bind();

		// Add it to the attribute lists.
		attributes.put(attribIndex, buffer);

		// Store the data in it.
		buffer.store(data, usage);

		// Make a pointer to the VBO in the VAO.
		glVertexAttribPointer(attribIndex, vectorSize, GL_FLOAT, false, 0, 0);

		// Unbind the VBO.
		buffer.unbind();

		return this;

	}

	/**
	 * Store a integer array of data as a vertex attribute in the VAO.
	 * 
	 * @param attribIndex The attribute list index in which to store the data.
	 * @param data        The data to store.
	 * @param vectorSize  The size of a single vertex worth of data (eg. 3 for a 3D
	 *                    position).
	 * @param usage       The purpose of the attribute list (eg.
	 *                    {@link #GL_STATIC_DRAW} or {@link #GL_DYNAMIC_DRAW}).
	 * @return [{@link VAO}] This same instance of the class.
	 */
	public VAO storeIntData(int attribIndex, int[] data, int vectorSize, int usage) {

		// Create the VBO.
		VBO buffer = VBO.create(GL_ARRAY_BUFFER);

		// Bind the buffer.
		buffer.bind();

		// Add it to the attribute lists.
		attributes.put(attribIndex, buffer);

		// Store the data in it.
		buffer.store(data, usage);

		// Make a pointer to the VBO in the VAO.
		glVertexAttribIPointer(attribIndex, vectorSize, GL_INT, 0, 0);

		// Unbind the VBO.
		buffer.unbind();

		return this;
	}

	/**
	 * Add a VBO to this VAO's attribute lists, to be deleted when this VAO is
	 * deleted.
	 * 
	 * @param attribIndex The attribute list index to bind the VBO to.
	 * @param vbo         The VBO object to associate to this VAO.
	 * @return [{@link VAO}] This same instance of the class.
	 */
	public VAO addVBO(int attribIndex, VBO vbo) {
		attributes.put(attribIndex, vbo);
		return this;
	}

	/**
	 * Deletes the VAO and all VBOs used as attributes.
	 */
	public void delete() {
		for (int key : attributes.keySet())
			attributes.get(key).delete();
		glDeleteVertexArrays(id);
	}

	/**
	 * Creates and returns a Vertex Array Object.
	 * 
	 * @return [{@link Vao}] The created object.
	 */
	public static VAO create() {
		int vaoID = glGenVertexArrays();
		return new VAO(vaoID);
	}

}
