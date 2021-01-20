package objects;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

/**
 * Represents a single texture stored in video memory.
 */
public class Texture implements GameResource {

    /**
     * The texture type. (ex: {@link org.lwjgl.opengl.GL13#GL_TEXTURE_2D} or {@link org.lwjgl.opengl.GL13#GL_TEXTURE_CUBE_MAP}).
     */
    private final int type;

    /**
     * The internal texture ID assigned to this texture by OpenGL.
     */
    private final int texID;

    /**
     * Creates a new texture with the given type and ID.
     *
     * @param type The type of texture (ex: {@link org.lwjgl.opengl.GL13#GL_TEXTURE_2D} or {@link org.lwjgl.opengl.GL13#GL_TEXTURE_CUBE_MAP}).
     * @param id   The ID of the texture.
     */
    Texture(int type, int id) {
        this.type = type;
        this.texID = id;
    }

    /**
     * Returns this texture's type (ex: {@link org.lwjgl.opengl.GL13#GL_TEXTURE_2D} or {@link org.lwjgl.opengl.GL13#GL_TEXTURE_CUBE_MAP}).
     *
     * @return <b>int</b> The texture type.
     */
    public int getType() {
        return type;
    }

    /**
     * Returns this texture's ID.
     *
     * @return <b>int</b> The texture ID.
     */
    public int getTextureID() {
        return texID;
    }

    /**
     * Binds this texture to specified texture unit.
     *
     * @param unit The texture unit to bind to (e.g. pass 0 for {@link org.lwjgl.opengl.GL13#GL_TEXTURE0}).
     * @return {@link Texture} The same instance of this class.
     */
    public Texture bindToUnit(int unit) {
        glActiveTexture(GL_TEXTURE0 + unit);
        glBindTexture(type, texID);
        return this;
    }

    public static Texture create2D(int width, int height, int filtering) {
        int texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filtering);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filtering);
        return new Texture(GL_TEXTURE_2D, texID);
    }

    /**
     * {@inheritDoc}
     */
    public void destroy() {
        glDeleteTextures(texID);
    }

}
