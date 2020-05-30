package shaders.uniform;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform3fv;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import shaders.Uniform;

public class UniformVector3fArray extends Uniform {
	
	private final int locations[];

	public UniformVector3fArray(String name, int size) {
		super(name);
		this.locations = new int[size];
	}
	
	public void retrieveLocation(int programID) {
		for(int i = 0; i < locations.length; i++)
			locations[i] = glGetUniformLocation(programID, name + "[" + i + "]");
	}
	
	public void set(int index, Vector3f data) {
		if(index < 0 || index >= locations.length)
			return;
		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(3);
		data.get(buffer);
		glUniform3fv(locations[index], buffer);
	}
	
	public void set(Vector3f[] data) {
		for(int i = 0; i < data.length; i++) {
			FloatBuffer buffer = BufferUtils.createFloatBuffer(3);
			data[i].get(buffer);
			if(i < locations.length) {
				glUniform3fv(locations[i], buffer);
			}
		}
	}
	
	public int getSize() {
		return locations.length;
	}
	
}
