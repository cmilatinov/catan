package shaders;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform2iv;

import java.nio.IntBuffer;

import org.joml.Vector2i;
import org.lwjgl.BufferUtils;

public class UniformVector2i extends Uniform {
	
	private int location = -1;

	public UniformVector2i(String name) {
		super(name);
	}
	
	public void retrieveLocation(int programID) {
		location = glGetUniformLocation(programID, name);
	}
	
	public void set(Vector2i value) {
		IntBuffer buffer = BufferUtils.createIntBuffer(2);
		value.get(buffer);
		glUniform2iv(location, buffer);
	}
	
}
