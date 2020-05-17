package objects;

import org.w3c.dom.Text;

import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Texture implements GameResource {

	private final int type;
	private final int texID;

	/**
	 * Creates a new texture with the given type and ID.
	 * 
	 * @param type The type of texture (e.g. {@link #GL_TEXTURE_2D} or
	 *             {@link #GL_TEXTURE_CUBE_MAP}).
	 * @param id   The ID of the texture.
	 */
	protected Texture(int type, int id) {
		this.type = type;
		this.texID = id;
	}

	/**
	 * Returns this texture's type (e.g. {@link #GL_TEXTURE_2D} or
	 * {@link #GL_TEXTURE_CUBE_MAP}).
	 * 
	 * @return [<b>int</b>] The texture type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Returns this texture's ID.
	 * 
	 * @return [<b>int</b>] The texture ID.
	 */
	public int getTextureID() {
		return texID;
	}

	/**
	 * Binds this texture to specified texture unit.
	 * 
	 * @param unit The texture unit to bind to (e.g. pass 0 for {@link #GL_TEXTURE0}).
	 * @return [{@link Texture}] The same instance of this class.
	 */
	public Texture bindToUnit(int unit) {
		glActiveTexture(GL_TEXTURE0 + unit);
		glBindTexture(type, texID);
		return this;
	}

	public Texture unbindToUnit(int unit) {
		glDeleteTextures(GL_TEXTURE0 + unit);
		glBindTexture(type, 0);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public void destroy() {
		glDeleteTextures(texID);
	}

}
