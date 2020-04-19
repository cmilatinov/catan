package objects;

import static org.lwjgl.opengl.GL40.*;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class Texture implements GameObject {
	
	private final int type;
	private final int texID;
	
	protected Texture(int type, int id) {
		this.type = type;
		this.texID = id;
	}
	
	public int getType() {
		return type;
	}
	
	public int getTextureID() {
		return texID;
	}
	
	public Texture bindToTextureUnit(int unit) {
		glActiveTexture(GL_TEXTURE0 + unit);
		glBindTexture(type, texID);
		return this;
	}
	
	public void destroy() {
		IntBuffer buffer = BufferUtils.createIntBuffer(1).put(texID);
		glDeleteTextures(buffer);
	}
	
}
