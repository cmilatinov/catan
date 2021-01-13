package objects;

/**
 * Represents a mesh which is to be rendered using a specific texture
 */
public class TexturedMesh implements GameResource {

	/**
	 * The mesh to render.
	 */
	private final Mesh mesh;

	/**
	 * The texture to use for the render.
	 */
	private final Texture texture;

	/**
	 * Creates a new textured mesh object given the specified mesh and texture.
	 * 
	 * @param mesh    The mesh to use.
	 * @param texture The texture to use.
	 */
	public TexturedMesh(Mesh mesh, Texture texture) {
		this.mesh = mesh;
		this.texture = texture;
	}
	
	/**
	 * Returns the mesh to render.
	 * @return {@link Mesh} The mesh for this model.
	 */
	public Mesh getMesh() {
		return mesh;
	}
	
	/**
	 * Returns the texture to use for rendering.
	 * @return {@link Texture} The texture for this model.
	 */
	public Texture getTexture() {
		return texture;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void destroy() {
		mesh.destroy();
		texture.destroy();
	}

}
