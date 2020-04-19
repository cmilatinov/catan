package render;

import org.joml.Matrix4f;

import camera.Camera;
import entities.Entity;
import objects.GameObject;
import objects.Mesh;
import objects.Texture;
import shaders.EntityShader;

public class EntityRenderer implements GameObject {
	
	private final EntityShader shader;
	
	public EntityRenderer() {
		shader = new EntityShader();
		shader.use();
		shader.projectionViewMatrix.set(new Matrix4f());
		shader.modelMatrix.set(new Matrix4f());
		shader.textureSampler.set(0);
		shader.stop();
	}
	
	
	public void render(Camera cam, Entity e) {
		
		Mesh mesh = e.getModel().getMesh();
		Texture texture = e.getModel().getTexture();
		
		shader.use();
		shader.projectionViewMatrix.set(cam.createProjectionViewMatrix());
		shader.modelMatrix.set(e.getTransform());
		
		mesh.getVAO().bind(0, 1, 2);
		texture.bindToTextureUnit(0);
		mesh.draw();
		mesh.getVAO().unbind(0, 1, 2);
		
		shader.stop();
		
	}
	
	public void destroy() {
		shader.destroy();
	}
	
}
