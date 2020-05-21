package main;

import camera.Camera;
import camera.CameraFPS;
import display.Window;
import entities.Entity;
import input.KeyCallback;
import lights.Light;
import objects.GameScript;
import objects.Mesh;
import objects.Texture;
import org.joml.Vector3f;
import render.EntityRenderer;
import render.SkyboxRenderer;
import resources.GameResources;
import resources.Resource;
import ui.UIManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {

    private final Map<Mesh, Map<Texture, List<Entity>>> entities = new HashMap<>();
    private final List<Light> lights = new ArrayList<>();
    private final List<GameScript> gameScripts = new ArrayList<>();

    private final UIManager uiManager;
    private final EntityRenderer entityRenderer;
    private final SkyboxRenderer skyboxRenderer;
    public final Window attachedWindow;

    private Texture skybox = null;

    private Camera camera;

    public Scene(Window window) {
        this.attachedWindow = window;
        this.uiManager = new UIManager(window);

        camera = new CameraFPS(70, window).translate(new Vector3f(0, 0, 1));

        entityRenderer = new EntityRenderer();
        skyboxRenderer = new SkyboxRenderer(GameResources.get(Resource.MESH_SKYBOX));
    }

    public Window getWindow() {
        return attachedWindow;
    }

    public UIManager getUiManager() {
        return uiManager;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return this.camera;
    }

    public void update(double delta) throws Exception {
        camera.update(delta);
        for (GameScript gameScript : gameScripts) {
            switch (gameScript.getCurrentState()) {
                case TO_START -> {
                    gameScript.start();
                    gameScript.setState(GameScript.State.TO_UPDATE);
                }
                case TO_STOP -> {
                    gameScript.stop();
                    gameScript.setState(GameScript.State.STOPPED);
                }
                case TO_UPDATE -> {
                    gameScript.update(delta);
                }
                case TO_INITIALIZE -> {
                    gameScript.initialize();
                    gameScript.setState(GameScript.State.TO_UPDATE);
                }
            }
        }
    }

    public void renderScene() {
        if (skybox != null)
            skyboxRenderer.render(this.camera, skybox);
        entityRenderer.render(this.camera, entities, lights);
    }

    public void registerKeyUpAction(int code, KeyCallback callback) {
        this.getWindow().keyboard().registerKeyUp(code, callback);
    }

    public void registerKeyDownAction(int code, KeyCallback callback) {
        this.getWindow().keyboard().registerKeyDown(code, callback);
    }

    public void cleanup() {
        for (Mesh m : entities.keySet()) {
            m.destroy();
            for (Texture t : entities.get(m).keySet()) {
                t.destroy();
            }
        }
        entityRenderer.destroy();
        camera.destroy();
        //NOTE: Scene shouldn't be responsible for the destruction of the window
    }

    public void register(GameScript object) {
        object.setContext(this);
        gameScripts.add(object);
    }

    public void register(Entity ent) {
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

    public void register(Light light) {
        lights.add(light);
    }

    public void remove(Entity ent) {
        Texture texture = ent.getModel().getTexture();
        Mesh mesh = ent.getModel().getMesh();

        if (!entities.containsKey(mesh))
            return;

        if (!entities.get(mesh).containsKey(texture))
            return;

        entities.get(mesh).get(texture).remove(ent);
    }

    public void remove(Light light) {
        lights.remove(light);
    }

    public void setSkyboxTexture(Texture skybox) {
        this.skybox = skybox;
    }

}
