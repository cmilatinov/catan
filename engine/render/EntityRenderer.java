package render;

import camera.Camera;
import entities.Entity;
import lights.Light;
import objects.FreeableObject;
import objects.Mesh;
import objects.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import shaders.entity.ShaderEntity;

import java.util.List;
import java.util.Map;

public class EntityRenderer implements FreeableObject {
	
	private final ShaderEntity shader;
	
	public EntityRenderer() {
		shader = new ShaderEntity();
		shader.use();
		shader.projectionViewMatrix.set(new Matrix4f());
		shader.modelMatrix.set(new Matrix4f());
		shader.textureSampler.set(0);
		shader.stop();
	}

	public void render(Camera cam, Map<Mesh, Map<Texture, List<Entity>>> entities, List<Light> lights) {
		
		shader.use();
		shader.projectionViewMatrix.set(cam.createProjectionViewMatrix());
		shader.numLights.set(lights.size());
		
		Vector3f[] positions = new Vector3f[lights.size()];
		for(int i = 0; i < positions.length; i++)
			positions[i] = lights.get(i).getPosition();
		
		Vector3f[] colors = new Vector3f[lights.size()];
		for(int i = 0; i < colors.length; i++)
			colors[i] = lights.get(i).getColor();
		
		shader.lightPositions.set(positions);
		shader.lightColors.set(colors);
		shader.cameraPos.set(cam.getPosition());
		
		for(Mesh mesh : entities.keySet()) {
			
			mesh.getVAO().bind(0, 1, 2);
			 
			for(Texture texture : entities.get(mesh).keySet()) {
				
				texture.bindToUnit(0);

				// TODO instanced rendering to alleviate the need for this loop
				for (Entity entity : entities.get(mesh).get(texture)) {
					if(!entity.shouldRender())
						continue;
					
					shader.modelMatrix.set(entity.getTransform());
					mesh.draw();
				}
			}
			
			mesh.getVAO().unbind(0, 1, 2);
		}
		
		shader.stop();
		
	}
	
	public void destroy() {
		shader.destroy();
	}
	
}
