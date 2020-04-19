package objects;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;

public class Mesh implements GameObject {
	
	private final VAO vao;
	private final int vertexCount;
	
	protected Mesh(VAO vao, int vertexCount) {
		this.vao = vao;
		this.vertexCount = vertexCount;
	}
	
	public VAO getVAO() {
		return vao;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	public void draw() {
		glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
	}
	
	public void destroy() {
		vao.destroy();
	}
	
}
