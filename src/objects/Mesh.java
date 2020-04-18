package objects;

public class Mesh {
	
	private VAO vao;
	
	private int vertexCount;
	
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
	
}
