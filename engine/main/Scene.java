package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import camera.Camera;
import camera.CameraFPS;
import display.Window;
import entities.Entity;
import gui.GUI;
import input.KeyCallback;
import lights.Light;
import objects.FreeableObject;
import objects.Mesh;
import objects.Texture;
import org.joml.Vector3f;
import render.EntityRenderer;
import render.GUIRenderer;
import render.SkyboxRenderer;
import resources.GameResources;
import resources.Resource;
import ui.UIManager;

public class Scene {

    private final List<GUI> guis = new ArrayList<GUI>();
    private final Map<Mesh, Map<Texture, List<Entity>>> entities = new HashMap<Mesh, Map<Texture, List<Entity>>>();
    private final List<Light> lights = new ArrayList<Light>();

    private final GUIRenderer guiRenderer;
    private final UIManager uiManager;
    private final EntityRenderer entityRenderer;
    private final SkyboxRenderer skyboxRenderer;
    public final Window attachedWindow;

    private Texture skybox = null;

    private Camera camera;

    public Scene(Window window, UIManager uiManager) {
        this.attachedWindow = window;
        this.uiManager = uiManager;

        camera = new CameraFPS(70, window).translate(new Vector3f(0, 0, 1));

        guiRenderer = new GUIRenderer(window);
        entityRenderer = new EntityRenderer();
        skyboxRenderer = new SkyboxRenderer(GameResources.get(Resource.MESH_SKYBOX));
    }

    public Window getWindow() {
        return attachedWindow;
    }

    public UIManager getUiManager() {
        return uiManager;
    }

    public void setCamera(Camera camera)
    {
        this.camera = camera;
    }

    public Camera getCamera()
    {
        return this.camera;
    }

    public void update(double delta) {
        camera.update(delta);
        for (Mesh m : entities.keySet())
            for (Texture t : entities.get(m).keySet())
                for (Entity e : entities.get(m).get(t))
                    if (e.shouldUpdate())
                        e.update(delta);
    }

    public void renderScene() {
        if (skybox != null)
            skyboxRenderer.render(this.camera, skybox);
        entityRenderer.render(this.camera, entities, lights);
        guiRenderer.render(this.camera, guis);
    }

    public void registerKeyUpAction(int code, KeyCallback callback) {
        this.getWindow().keyboard().registerKeyUp(code, callback);
    }

    public void registerKeyDownAction(int code, KeyCallback callback) {
        this.getWindow().keyboard().registerKeyDown(code, callback);
    }

    public void registerGUI(GUI gui) {
        guis.add(gui);
    }

    public void cleanup()
    {
        for (Mesh m : entities.keySet())
        {
            m.destroy();
            for (Texture t : entities.get(m).keySet()) {
                t.destroy();
            }
        }

        entityRenderer.destroy();
        camera.destroy();
    }

    @SuppressWarnings("serial")
    public void registerEntity(Entity ent) {
        Texture texture = ent.getModel().getTexture();
        Mesh mesh = ent.getModel().getMesh();

        if (!entities.containsKey(mesh))
            entities.put(ent.getModel().getMesh(), new HashMap<Texture, List<Entity>>() {{
                put(ent.getModel().getTexture(), new ArrayList<Entity>() {{
                    add(ent);
                }});
            }});

        else if (!entities.get(mesh).containsKey(texture))
            entities.get(mesh).put(texture, new ArrayList<Entity>() {{
                add(ent);
            }});

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

        if (!entities.containsKey(mesh))
            return;

        if (!entities.get(mesh).containsKey(texture))
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
