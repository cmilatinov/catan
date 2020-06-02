package render;

import org.joml.Matrix4f;

import camera.Camera;
import objects.Mesh;
import objects.Texture;
import shaders.skybox.ShaderSkybox;

public class SkyboxRenderer {

	private static Mesh mesh = null;

	private final ShaderSkybox shader;

	public SkyboxRenderer() {
		shader = new ShaderSkybox();
		shader.use();
		shader.textureSampler.set(0);
		shader.stop();
		if (mesh == null)
			mesh = initMesh();
	}

	private static Mesh initMesh() {

	}
	
	public void render(Camera cam, Texture skybox) {
		shader.use();
		Matrix4f projView = cam.getViewMatrix().setTranslation(0, 0, 0);
		projView = cam.getProjectionMatrix().mul(projView);
		shader.projectionViewMatrix.set(projView);
		
		mesh.getVAO().bind(0);
		skybox.bindToUnit(0);
		mesh.draw();
		mesh.getVAO().unbind(0);
		
		shader.stop();
	}
	
}
