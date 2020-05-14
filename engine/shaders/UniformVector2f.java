package shaders;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform2fv;

import java.nio.FloatBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

public class UniformVector2f extends Uniform {
	
	private int location = -1;

	public UniformVector2f(String name) {
		super(name);
	}
	
	public void retrieveLocation(int programID) {
		location = glGetUniformLocation(programID, name);
	}
	
	public void set(Vector2f value) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(2);
		value.get(buffer);
		glUniform2fv(location, buffer);
	}
	
}
