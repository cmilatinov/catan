package shaders;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

public class UniformMatrix4f extends Uniform {
	
	private int location = -1;

	public UniformMatrix4f(String name) {
		super(name);
	}
	
	public void retrieveLocation(int programID) {
		location = glGetUniformLocation(programID, name);
	}
	
	public void set(Matrix4f value) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		value.get(buffer);
		glUniformMatrix4fv(location, false, buffer);
	}
	
}
