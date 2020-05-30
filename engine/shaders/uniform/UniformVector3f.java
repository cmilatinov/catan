package shaders.uniform;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform3fv;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import shaders.Uniform;

public class UniformVector3f extends Uniform {
	
	private int location = -1;

	public UniformVector3f(String name) {
		super(name);
	}
	
	public void retrieveLocation(int programID) {
		location = glGetUniformLocation(programID, name);
	}
	
	public void set(Vector3f value) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(3);
		value.get(buffer);
		glUniform3fv(location, buffer);
	}
	
}
