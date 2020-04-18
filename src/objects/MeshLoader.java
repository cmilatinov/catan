package objects;

import static org.lwjgl.opengl.GL40.*;

public class MeshLoader {
	
	public Mesh loadMesh(int[] indices, float[] vertices) {
		
		// Create VAO
		VAO vao = VAO.create().bind();
		
		// Create indices VBO
		VBO indicesVBO = VBO.create(GL_ELEMENT_ARRAY_BUFFER)
				.bind()
				.store(indices, GL_STATIC_DRAW);
		
		// Add indices VBO to VAO
		vao.addVBO(-1, indicesVBO);
		
		// Store vertex data in attribute list 0
		vao.storeFloatData(0, vertices, 3, GL_STATIC_DRAW);
		
		// Unbind VAO
		vao.unbind();
		
		// Return new mesh object
		return new Mesh(vao, indices.length);
		
	}
	
}
