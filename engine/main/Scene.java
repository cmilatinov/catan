package main;

import camera.Camera;
import camera.CameraFPS;
import display.Window;
import entities.Entity;
import input.KeyCallback;
import input.MouseClickCallback;
import lights.Light;
import objects.GameScript;
import objects.InjectableScript;
import objects.Mesh;
import objects.Texture;
import org.joml.Vector3f;
import physics.PhysicsManager;
import render.EntityRenderer;
import render.SkyboxRenderer;
import ui.UIManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Scene {

    private final ArrayList<Entity> allEntities = new ArrayList<>();
    private final Map<Mesh, Map<Texture, List<Entity>>> entities = new HashMap<>();
    private final List<Light> lights = new ArrayList<>();
    private final HashMap<Class<GameScript>, GameScript> gameScripts = new HashMap<>();

    private UIManager uiManager;
    private final EntityRenderer entityRenderer;
    private final SkyboxRenderer skyboxRenderer;
    private PhysicsManager physics;

    private Window attachedWindow;

    protected SceneManager sceneManager;

    private Texture skybox = null;

    private Camera camera;

    public Scene() {
        entityRenderer = new EntityRenderer();
        skyboxRenderer = new SkyboxRenderer();
    }

    /**
     * Tag to identify current scene
     *
     * @return the tag
     */
    public String getTag() {
        return "default";
    }

    protected boolean isValid() {
        return this.attachedWindow != null;
    }

    /**
     * Initialize the Scene so that all of it's subsystems are ready
     *
     * @param window the window to attach to the scene
     */
    public void setup(Window window, SceneManager sceneManager) {
        this.attachedWindow = window;
        this.sceneManager = sceneManager;
        this.uiManager = new UIManager(window);
        this.physics = new PhysicsManager(this);
        camera = new CameraFPS(70, window).translate(new Vector3f(0, 0, 1));
    }

    /**
     * Do any initialization for the scene here
     */
    public abstract void initialize();

    /**
     * Get the scene manager attached to this scene
     * @return the scene manager
     */
    public SceneManager getSceneManager() {
        return sceneManager;
    }

    /**
     * Return the Window for the current scene
     *
     * @return the reference to the window
     */
    public Window getWindow() {
        return attachedWindow;
    }

    /**
     * Return the UI Manager for the current scene
     *
     * @return the reference to the UI Manager
     */
    public UIManager getUiManager() {
        return uiManager;
    }

    /**
     * Access the physics subsystem
     *
     * @return the reference to the PhysicsManager
     */
    public PhysicsManager physics() {
        return this.physics;
    }

    /**
     * Set the current camera for the scene
     *
     * @param camera the new camera for the scene
     */
    public void setCamera(Camera camera) {
        if (this.camera != null) {
            this.camera.destroy();
        }
        this.camera = camera;
    }

    /**
     * Get the current camera for the scene
     *
     * @return The Camera Reference
     */
    public Camera getCamera() {
        return this.camera;
    }

    /**
     * Return all the entity's registered to this scene
     *
     * @return a list of all entities
     */
    public ArrayList<Entity> getEntities() {
        return this.allEntities;
    }

    /**
     * Run any attached GameScripts which handle game/ui logic
     *
     * @param delta the time delta between frames
     * @throws Exception Exception for any of the GameScripts
     */
    public void update(double delta) throws Exception {
        if (!isValid()) {
            throw new RuntimeException("Attempting to update the scene when it hasn't been setup (Scene::setup(Window window))");
        }

        camera.update(delta);
        for (GameScript gameScript : gameScripts.values()) {
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
                    injectDependencies(gameScript);
                    gameScript.initialize();
                    gameScript.setState(GameScript.State.TO_UPDATE);
                }
            }
        }
    }

    protected void injectDependencies(GameScript gameScript) throws IllegalAccessException {
        Field[] fields = gameScript.getClass().getDeclaredFields();
        for (Field field: fields ) {
            InjectableScript annotation = field.getAnnotation(InjectableScript.class);
            GameScript classToInject = gameScripts.get(field.getType());
            if(null != annotation && null != classToInject) {
                field.setAccessible(true);
                field.set(gameScript, classToInject);
            }
        }
    }

    /**
     * Render all render-able entities
     */
    public void renderScene() {
        if (skybox != null)
            skyboxRenderer.render(this.camera, skybox);
        entityRenderer.render(this.camera, entities, lights);
    }

    /**
     * Shortcut to register key up actions
     *
     * @param code     the keycode
     * @param callback the callback to execute
     */
    public void registerKeyUpAction(int code, KeyCallback callback) {
        this.getWindow().keyboard().registerKeyUp(code, callback);
    }

    /**
     * Shortcut to register key down actions
     *
     * @param code     the keycode
     * @param callback the callback to execute
     */
    public void registerKeyDownAction(int code, KeyCallback callback) {
        this.getWindow().keyboard().registerKeyDown(code, callback);
    }

    public void registerMouseClickAction(MouseClickCallback callback) {
        this.getWindow().mouse().registerMouseClickCallback(callback);
    }

    public void register(GameScript object) {
        object.setContext(this);
        gameScripts.put((Class<GameScript>) object.getClass(), object);
    }

    /**
     * Register a Entity to the scene
     *
     * @param ent the entity to be registered
     */
    public void register(Entity ent) {
        Texture texture = ent.getModel().getTexture();
        Mesh mesh = ent.getModel().getMesh();

        allEntities.add(ent);

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

    /**
     * Register a light source to the scene
     *
     * @param light the light to be registerd
     */
    public void register(Light light) {
        lights.add(light);
    }

    /**
     * Remove a entity from the scene
     *
     * @param ent the entity the remove
     */
    public void remove(Entity ent) {
        Texture texture = ent.getModel().getTexture();
        Mesh mesh = ent.getModel().getMesh();

        allEntities.remove(ent);

        if (!entities.containsKey(mesh))
            return;

        if (!entities.get(mesh).containsKey(texture))
            return;

        entities.get(mesh).get(texture).remove(ent);
    }

    /**
     * Remove a light from the scene
     *
     * @param light the light to be removed
     */
    public void remove(Light light) {
        lights.remove(light);
    }

    /**
     * Set the skybox texture to be used for this scene;
     *
     * @param skybox the skybox texture to use
     */
    public void setSkyboxTexture(Texture skybox) {
        this.skybox = skybox;
    }

    /**
     * Call any destruction logic for objects in this scene
     */
    public void cleanup() {
        for (Mesh m : entities.keySet()) {
            m.destroy();
            for (Texture t : entities.get(m).keySet()) {
                t.destroy();
            }
        }
        entityRenderer.destroy();
        camera.destroy();
        uiManager.cleanup();
        //NOTE: Scene shouldn't be responsible for the destruction of the window
    }

}
