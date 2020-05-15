package objects;

import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

/**
 * Represents a Vertex Buffer Object (VBO).
 */
public class VBO implements FreeableObject {

	/**
	 * The VBO's ID.
	 */
	private int id;

	/**
	 * The VBO's type (eg. {@link org.lwjgl.opengl.GL15#GL_ARRAY_BUFFER
	 * GL_ARRAY_BUFFER} or {@link org.lwjgl.opengl.GL15#GL_ELEMENT_ARRAY_BUFFER
	 * GL_ELEMENT_ARRAY_BUFFER}).
	 */
	private int type;

	/**
	 * Creates a Vertex Buffer Object (VBO) with the specified type and ID.
	 * 
	 * @param id   The ID of the VBO.
	 * @param type The type of VBO.
	 */
	private VBO(int id, int type) {
		this.id = id;
		this.type = type;
	}

	/**
	 * Binds the VBO for use and manipulation.
	 * 
	 * @return [{@link VBO}] This same instance of the class.
	 */
	public VBO bind() {
		glBindBuffer(type, id);
		return this;
	}

	/**
	 * Unbinds the VAO.
	 * 
	 * @return [{@link VBO}] This same instance of the class.
	 */
	public VBO unbind() {
		glBindBuffer(type, 0);
		return this;
	}

	/**
	 * Returns the ID of this VBO.
	 * 
	 * @return [<b>int</b>] The ID of the VBO.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Stores a floating point array of data in the VBO.
	 * 
	 * @param data  The data to store in the VBO.
	 * @param usage The purpose of the VBO (eg. {@link #GL_STATIC_DRAW} or
	 *              {@link #GL_DYNAMIC_DRAW}).
	 * @return [{@link VBO}] This same instance of the class.
	 */
	public VBO store(float[] data, int usage) {
		glBufferData(type, data, usage);
		return this;
	}

	/**
	 * Stores an integer array of data in the VBO.
	 * 
	 * @param data  The data to store in the VBO.
	 * @param usage The purpose of the VBO (eg. {@link #GL_STATIC_DRAW} or
	 *              {@link #GL_DYNAMIC_DRAW}).
	 * @return [{@link VBO}] This same instance of the class.
	 */
	public VBO store(int[] data, int usage) {
		glBufferData(type, data, usage);
		return this;
	}
	
	/**
	 * 
	 * @param offset The offset in the buffer to start loading the data at, in bytes.
	 * @param data The data to load in the buffer.
	 * @return
	 */
	public VBO storeSubData(int offset, float[] data) {
		glBufferSubData(type, offset, data);
		return this;
	}

	/**
	 * Allocates memory to the buffer object.
	 * 
	 * @param size  The size in bytes of the memory to allocate to this buffer.
	 * @param usage The purpose of the VBO (eg. {@link #GL_STATIC_DRAW} or
	 *              {@link #GL_DYNAMIC_DRAW}).
	 * @return [{@link VBO}] This same instance of the class.
	 */
	public VBO allocate(int size, int usage) {
		glBufferData(type, size, usage);
		return this;
	}

	/**
	 * Deletes the VBO.
	 */
	public void destroy() {
		glDeleteBuffers(id);
	}

	/**
	 * Creates and returns a Vertex Buffer Object (VBO).
	 * 
	 * @return [{@link VBO}] The created object.
	 */
	public static VBO create(int type) {
		int vboID = glGenBuffers();
		return new VBO(vboID, type);
	}

}
