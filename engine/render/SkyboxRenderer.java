package render;

import camera.Camera;
import objects.GameResourceFactory;
import objects.Mesh;
import objects.Texture;
import org.joml.Matrix4f;
import shaders.skybox.ShaderSkybox;

public class SkyboxRenderer {

	private static final float SKYBOX_SIZE = 500;
	private static final int[] SKYBOX_MESH_INDICES = {
			0, 4, 6,
			0, 6, 2,
			0, 2, 3,
			0, 3, 1,
			2, 6, 7,
			2, 7, 3,
			4, 7, 6,
			4, 5, 7,
			0, 5, 4,
			0, 1, 5,
			1, 7, 5,
			1, 3, 7,
	};

	private static final float[] SKYBOX_MESH_VERTICES = {
			-SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
			-SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE,
			-SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE,
			-SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE,
			SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
			SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE,
			SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE,
			SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE
	};

	private static Mesh mesh = null;

	private final ShaderSkybox shader;

	public SkyboxRenderer() {
		shader = new ShaderSkybox();
		shader.use();
		shader.textureSampler.set(0);
		shader.stop();
		if (mesh == null)
			mesh = GameResourceFactory.loadMesh(SKYBOX_MESH_INDICES, SKYBOX_MESH_VERTICES);
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
