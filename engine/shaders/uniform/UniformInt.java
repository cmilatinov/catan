package shaders.uniform;

import shaders.Uniform;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;

public class UniformInt extends Uniform {
	
	private int location = -1;
	
	public UniformInt(String name) {
		super(name);
	}
	
	public void retrieveLocation(int programID) {
		location = glGetUniformLocation(programID, name);
	}
	
	public void set(int value) {
		glUniform1i(location, value);
	}
	
}
