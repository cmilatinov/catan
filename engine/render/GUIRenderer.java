package render;

import java.util.Comparator;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import camera.Camera;
import display.Window;
import gui.GUI;
import objects.Mesh;
import resources.GameResources;
import resources.Resource;
import shaders.gui.GUIShader;

public class GUIRenderer {
	
	private final GUIShader shader;
	private final Mesh guiMesh;
	
	private final Window window;
	
	public GUIRenderer(Window window) {
		this.shader = new GUIShader();
		this.guiMesh = GameResources.get(Resource.MESH_GUI);
		this.window = window;
	}
	
	public void render(Camera cam, List<GUI> guis) {
		shader.use();
		shader.textureSampler.set(0);
		shader.projectionMatrix.set(cam.getProjectionMatrix());
		
		guiMesh.getVAO().bind(0, 1);
		
		guis.sort(Comparator.comparingInt(GUI::getElevation));
		
		for(GUI gui : guis) {
				
			gui.getTexture().bindToUnit(0);
			Matrix4f transform = createGUITransform(cam, gui);
			shader.modelMatrix.set(transform);
			guiMesh.draw();
			
		}
		
		guiMesh.getVAO().unbind(0, 1);
		shader.stop();
	}
	
	private Matrix4f createGUITransform(Camera cam, GUI gui) {
		
		Matrix4f result = new Matrix4f();
		
		Vector2f pos = gui.getPosition();
		
		float dist = -0.2f + 0.001f * gui.getElevation();
		float height = 2.0f * -dist * (float)Math.tan(Math.toRadians(cam.getFovY() * 0.5f));
		float width = cam.getAspect() * height;
		
		result.translate(new Vector3f((width / 2) * ((pos.x * 2 / window.getWidth()) - 1), (height / 2) * (1 - (pos.y * 2 / window.getHeight())), -1.0f + dist));
		result.rotate((float)Math.toRadians(gui.getRotation()), new Vector3f(0, 0, -1));
		
		
		float scale = gui.getSize() * height / (2.0f * window.getHeight());
		result.scale(new Vector3f(scale, scale, 1f));
		
		return result;
		
	}
	
}
