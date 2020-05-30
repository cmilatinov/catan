package shaders.uniform;

import shaders.Uniform;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;

public class UniformFloat extends Uniform {
	
	private int location = -1;
	
	public UniformFloat(String name) {
		super(name);
	}
	
	public void retrieveLocation(int programID) {
		location = glGetUniformLocation(programID, name);
	}
	
	public void set(float value) {
		glUniform1f(location, value);
	}
	
}
