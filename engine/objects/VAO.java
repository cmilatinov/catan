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
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Vertex Array Object (VAO).
 */
public class VAO implements GameObject {

	/**
	 * The VAO's ID.
	 */
	private int id;

	/**
	 * The VBOs attached to this VAO.
	 */
	private List<VBO> vbos = new ArrayList<VBO>();

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

		// Add it to the vbo list.
		vbos.add(buffer);

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

		// Add it to the vbo list.
		vbos.add(buffer);

		// Store the data in it.
		buffer.store(data, usage);

		// Make a pointer to the VBO in the VAO.
		glVertexAttribIPointer(attribIndex, vectorSize, GL_INT, 0, 0);

		// Unbind the VBO.
		buffer.unbind();

		return this;
	}
	
	/**
	 * Creates an instanced attribute for this VAO based on the currently bound VBO and the given arguments.
	 * 
	 * @param attribIndex The attribute list index in which to store the data.
	 * @param vectorSize The size of a single vertex worth of data (eg. 3 for a 3D position).
	 * @param dataType The type of data (eg. {@link #GL_FLOAT}).
	 * @param stride The stride of the data in bytes (size of data for a single instance).
	 * @param offset The offset of the data in bytes (always less than the stride).
	 * @return
	 */
	public VAO addInstancedAttribute(int attribIndex, int vectorSize, int dataType, int stride, int offset) {
		glVertexAttribPointer(attribIndex, vectorSize, dataType, false, stride, offset);
		glVertexAttribDivisor(attribIndex, 1);
		return this;
	}

	/**
	 * Add a VBO to this VAO, to be deleted when this VAO is deleted.
	 * 
	 * @param vbo The VBO object to associate to this VAO.
	 * @return [{@link VAO}] This same instance of the class.
	 */
	public VAO addVBO(VBO vbo) {
		vbos.add(vbo);
		return this;
	}

	/**
	 * Deletes the VAO and all VBOs used as attributes.
	 */
	public void destroy() {
		for (VBO vbo : vbos)
			vbo.destroy();
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
