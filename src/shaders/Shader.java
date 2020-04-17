package shaders;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgramiv;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderiv;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import log.Logger;
import main.Engine;

public abstract class Shader {
	
	// Shader types
	private static final int SHADER_VERTEX = 0b1;
	private static final int SHADER_FRAGMENT = 0b10;
	private static final int SHADER_GEOMETRY = 0b100;
	
	// Holds this program's ID.
	private int program;
	
	// Holds which types of shaders belong to this program.
	private final int shaders;
	
	// The integers hold the shader IDs used for this shader.
	private int vertexShader, fragmentShader, geometryShader;
	
	// Used to indicate compilation errors.
	private String error = null;
	
	/**
	 * Creates a new shader program with the specified vertex shader, fragment shader, and uniform variables.
	 * @param vertexFile The file to compile as this shader program's vertex shader.
	 * @param fragmentFile The file to compile as this shader program's fragment shader.
	 * @param uniforms The uniform variables used in this shader program.
	 */
	public Shader(String vertexFile, String fragmentFile, Uniform... uniforms) {
		
		// We're using a vertex and fragment shader.
		shaders = SHADER_VERTEX | SHADER_FRAGMENT;
		
		// Create the program.
		program = glCreateProgram();
		
		// Create and compile the vertex shader.
		vertexShader = compileShader(vertexFile, GL_VERTEX_SHADER);
		if(vertexShader < 0) {
			Engine.LOGGER.log(Logger.ERROR, "Vertex shader compilation error : \n" + error);
			glDeleteProgram(program);
			Engine.stop(Engine.ERR_SHADER_COMPILATION);
		}
		
		// Create and compile the fragment shader.
		fragmentShader = compileShader(fragmentFile, GL_FRAGMENT_SHADER);
		if(fragmentShader < 0) {
			Engine.LOGGER.log(Logger.ERROR, "Fragment shader compilation error : \n" + error);
			glDeleteProgram(program);
			Engine.stop(Engine.ERR_SHADER_COMPILATION);
		}
		
		// Attach all shaders to the program.
		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);
		
		// Link the program.
		glLinkProgram(program);
		
	}
	
	/**
	 * Creates a new shader program with the specified vertex shader, fragment shader, geometry shader, and uniform variables.
	 * @param vertexFile The file to compile as this shader program's vertex shader.
	 * @param fragmentFile The file to compile as this shader program's fragment shader.
	 * @param uniforms The uniform variables used in this shader program.
	 */
	public Shader(String vertexFile, String fragmentFile, String geometryFile, Uniform... uniforms) {
		
		// We're using a vertex, fragment, and geometry shader.
		shaders = SHADER_VERTEX | SHADER_FRAGMENT | SHADER_GEOMETRY;
		
		// Create the program.
		program = glCreateProgram();
		
		// Create and compile the vertex shader.
		vertexShader = compileShader(vertexFile, GL_VERTEX_SHADER);
		if(vertexShader < 0) {
			Engine.LOGGER.log(Logger.ERROR, "Vertex shader compilation error : \n" + error);
			glDeleteProgram(program);
			Engine.stop(Engine.ERR_SHADER_COMPILATION);
		}
		
		// Create and compile the fragment shader.
		fragmentShader = compileShader(fragmentFile, GL_FRAGMENT_SHADER);
		if(fragmentShader < 0) {
			Engine.LOGGER.log(Logger.ERROR, "Fragment shader compilation error : \n" + error);
			glDeleteProgram(program);
			Engine.stop(Engine.ERR_SHADER_COMPILATION);
		}
		
		// Create and compile the geometry shader.
		geometryShader = compileShader(geometryFile, GL_GEOMETRY_SHADER);
		if(geometryShader < 0) {
			Engine.LOGGER.log(Logger.ERROR, "Geometry shader compilation error : \n" + error);
			glDeleteProgram(program);
			Engine.stop(Engine.ERR_SHADER_COMPILATION);
		}
		
		// Attach all shaders to the program.
		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);
		glAttachShader(program, geometryShader);
		
		// Bind the attributes used in the shader program.
		bindAttributes();
		
		// Link the program.
		glLinkProgram(program);
		glValidateProgram(program);
		
		// Check linking status
		IntBuffer status = BufferUtils.createIntBuffer(1);
		glGetProgramiv(program, GL_LINK_STATUS, status);
		
		// Linking failed
		if(status.get(0) == GL_FALSE) {
			Engine.LOGGER.log(Logger.ERROR, "Shader program linking error : \n" + glGetProgramInfoLog(program));
			glDeleteProgram(program);
			Engine.stop(Engine.ERR_SHADER_LINKING);
		}
		
		// Retrieve all uniform locations
		for(Uniform u : uniforms)
			u.retrieveLocation(program);
	}
	
	
	/**
	 * Reads and compiles a shader from the specified file.
	 * @param file The file from which to read and compile the shader.
	 * @param type The type of shader to compile.
	 * @return [<b>int</>] The shader handle if the compilation is successful, -1 otherwise.
	 */
	private int compileShader(String file, int type) {
		try {
			
			// Read shader file
			BufferedReader br = new BufferedReader(new InputStreamReader(Engine.class.getResourceAsStream(file)));
			StringBuilder sb = new StringBuilder();
			String line = "";
			while((line = br.readLine()) != null)
				sb.append(line + "\n");
			
			// Create the shader object
			int shader = glCreateShader(type);
			
			// Error creating shader object
			if(shader == 0)
				return -1;
			
			// Compile the shader source
			glShaderSource(shader, sb.toString());
			glCompileShader(shader);
			
			// Get compilation status
			IntBuffer status = BufferUtils.createIntBuffer(1);
			glGetShaderiv(shader, GL_COMPILE_STATUS, status);
			
			// Compilation failed
			if(status.get(0) == GL_FALSE) {
				error = glGetShaderInfoLog(shader);
				glDeleteShader(shader);
				return -1;
			}
			
			// Success!
			return shader;
			
		} catch (Exception e) {
			
			// Error reading the shader file
			error = e.getMessage();
			return -1;
		}
	}
	
	/**
	 * Used by child classes to bind any used attributes before the linking process.
	 */
	public abstract void bindAttributes();
	
	/**
	 * Binds a singular variable to an attribute index by name.
	 * @param index The attribute index to which the variable is to be bound.
	 * @param varName The name of the variable to bind.
	 */
	public void bindToAttribute(int index, String varName) {
		glBindAttribLocation(program, index, varName);
	}
	
	/**
	 * Destroys this shader program and its shader objects.
	 */
	public void destroy() {
		
		// Detach all used shaders.
		if((shaders & SHADER_VERTEX) > 0)
			glDetachShader(program, vertexShader);
		
		if((shaders & SHADER_FRAGMENT) > 0)
			glDetachShader(program, fragmentShader);
		
		if((shaders & SHADER_GEOMETRY) > 0)
			glDetachShader(program, geometryShader);
		
		// Delete all used shaders.
		if((shaders & SHADER_VERTEX) > 0)
			glDeleteShader(vertexShader);
		
		if((shaders & SHADER_FRAGMENT) > 0)
			glDeleteShader(fragmentShader);
		
		if((shaders & SHADER_GEOMETRY) > 0)
			glDeleteShader(geometryShader);
		
		// Delete shader program.
		glDeleteProgram(program);
	}
	
}