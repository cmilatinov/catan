package objects;

public class TexturedMesh implements GameObject {
	
	private final Mesh mesh;
	private final Texture texture;
	
	public TexturedMesh(Mesh mesh, Texture texture) {
		this.mesh = mesh;
		this.texture = texture;
	}
	
	public Mesh getMesh() {
		return mesh;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public void destroy() {
		mesh.destroy();
		texture.destroy();
	}
	
}
