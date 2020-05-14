package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import camera.Camera;
import display.Window;
import entities.Entity;
import gui.GUI;
import lights.Light;
import objects.Mesh;
import objects.Texture;
import render.EntityRenderer;
import render.GUIRenderer;
import render.SkyboxRenderer;
import resources.GameResources;
import resources.Resource;

public class GameController {
	
	private final List<GUI> guis = new ArrayList<GUI>();
	private final Map<Mesh, Map<Texture, List<Entity>>> entities = new HashMap<Mesh, Map<Texture, List<Entity>>>();
	private final List<Light> lights = new ArrayList<Light>();
	
	private final GUIRenderer guiRenderer;
	private final EntityRenderer entityRenderer;
	private final SkyboxRenderer skyboxRenderer;
	
	private Texture skybox = null;
	
	public GameController(Window window) {
		guiRenderer = new GUIRenderer(window);
		entityRenderer = new EntityRenderer();
		skyboxRenderer = new SkyboxRenderer(GameResources.get(Resource.MESH_SKYBOX));
	}
	
	public void update(double delta) {
		for(Mesh m : entities.keySet())
			for(Texture t : entities.get(m).keySet())
				for(Entity e : entities.get(m).get(t))
					if(e.shouldUpdate())
						e.update(delta);
	}
	
	public void renderScene(Camera cam) {
		if(skybox != null)
			skyboxRenderer.render(cam, skybox);
		entityRenderer.render(cam, entities, lights);
		guiRenderer.render(cam, guis);
	}
	
	public void registerGUI(GUI gui) {
		guis.add(gui);
	}
	
	@SuppressWarnings("serial")
	public void registerEntity(Entity ent) {
		Texture texture = ent.getModel().getTexture();
		Mesh mesh = ent.getModel().getMesh();
		
		if(!entities.containsKey(mesh))
			entities.put(ent.getModel().getMesh(), new HashMap<Texture, List<Entity>> () {{
				put(ent.getModel().getTexture(), new ArrayList<Entity>() {{ add(ent); }});
			}});
		
		else if(!entities.get(mesh).containsKey(texture))
			entities.get(mesh).put(texture, new ArrayList<Entity>() {{ add(ent); }});
		
		else
			entities.get(mesh).get(texture).add(ent);
	}
	
	public void registerLight(Light light) {
		lights.add(light);
	}
	
	public void removeGUI(GUI gui) {
		guis.remove(gui);
	}
	
	public void removeEntity(Entity ent) {
		Texture texture = ent.getModel().getTexture();
		Mesh mesh = ent.getModel().getMesh();
		
		if(!entities.containsKey(mesh))
			return;
		
		if(!entities.get(mesh).containsKey(texture))
			return;
		
		entities.get(mesh).get(texture).remove(ent);
	}
	
	public void removeLight(Light light) {
		lights.remove(light);
	}
	
	public void setSkyboxTexture(Texture skybox) {
		this.skybox = skybox;
	}
	
}
