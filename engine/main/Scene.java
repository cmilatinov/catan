package main;

import camera.Camera;
import camera.CameraFPS;
import display.Window;
import entities.Entity;
import input.KeyCallback;
import input.MouseClickCallback;
import lights.Light;
import objects.*;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.joml.Vector3f;
import physics.PhysicsManager;
import render.EntityRenderer;
import render.SkyboxRenderer;
import ui.UIManager;

import java.lang.reflect.Field;
import java.util.*;

import static objects.GameScript.State;

public abstract class Scene {

    // Entities
    private final ArrayList<Entity> allEntities = new ArrayList<>();
    private final Map<Mesh, Map<Texture, List<Entity>>> entities = new HashMap<>();
    private final List<Light> lights = new ArrayList<>();
    @SuppressWarnings("rawtypes")
    private static final ListOrderedMap<Class, GameScript> globalGameScripts = new ListOrderedMap<>();
    @SuppressWarnings("rawtypes")
    private final ListOrderedMap<Class, GameScript> gameScripts = new ListOrderedMap<>();

    // Manager
    private UIManager uiManager;
    private PhysicsManager physics;

    // Renderers
    private final SkyboxRenderer skyboxRenderer;
    private final EntityRenderer entityRenderer;
    protected SceneManager sceneManager;

    // Other
    private Window attachedWindow;
    private Texture skybox = null;
    private Camera camera;

    private MouseClickCallback onSceneClick;

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
        this.camera = new CameraFPS(70, window).translate(new Vector3f(0, 0, 1));
        window.mouse().registerMouseClickCallback(this::onClick);
    }

    /**
     * Do any initialization for the scene here
     */
    public abstract void initialize();

    /**
     * Get the scene manager attached to this scene
     *
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
     * Set the callback invoked when a scene click occurs (click passed through UI)
     *
     * @param callback The new callback
     */
    public void setOnSceneClick(MouseClickCallback callback) {
        this.onSceneClick = callback;
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
        for (GameScript globalScripts : globalGameScripts.values()) {
            switch (globalScripts.getCurrentState()) {
                case TO_START -> {
                    globalScripts.start();
                    globalScripts.setState(State.TO_UPDATE);
                }
                case TO_STOP -> {
                    globalScripts.stop();
                    globalScripts.setState(State.STOPPED);
                }
                case TO_UPDATE -> {
                    globalScripts.update(delta);
                }
                case TO_INITIALIZE -> {
                    if (handleDependencies(globalScripts)) {
                        globalScripts.initialize();
                        globalScripts.setState(State.TO_UPDATE);
                    }
                }
                case RE_INITIALIZE -> {
                    globalScripts.initialize();
                    globalScripts.setState(State.TO_UPDATE);
                }
            }
        }

        for (GameScript gameScript : gameScripts.values()) {
            switch (gameScript.getCurrentState()) {
                case TO_START -> {
                    gameScript.start();
                    gameScript.setState(State.TO_UPDATE);
                }
                case TO_STOP -> {
                    gameScript.stop();
                    gameScript.setState(State.STOPPED);
                }
                case TO_UPDATE -> {
                    gameScript.update(delta);
                }
                case TO_INITIALIZE -> {
                    if (handleDependencies(gameScript)) {
                        gameScript.initialize();
                        gameScript.setState(State.TO_UPDATE);
                    }
                }
                case RE_INITIALIZE -> {
                    gameScript.initialize();
                    gameScript.setState(State.TO_UPDATE);
                }
            }
        }
    }

    private void onClick(int button, int action, int mods) {
        if (!uiManager.onMouseClick(button, action, mods) && onSceneClick != null)
            onSceneClick.invoke(button, action, mods);
    }

    /**
     * Handle dependency injection of other scripts and deferring the initialization
     * NOTE: only really handles one level of dependencies, will have to come back to this is we need
     * a more robust solution
     *
     * @param gameScript Script Object to handle
     * @return true if there is no need to defer
     * @throws IllegalAccessException if we fail to inject the dependency
     */
    protected boolean handleDependencies(GameScript gameScript) throws IllegalAccessException {
        Field[] fields = gameScript.getClass().getDeclaredFields();
        for (Field field : fields) {
            // Skip over things which are not GameScripts
            if (!(GameScript.class.isAssignableFrom(field.getType()))) {
                continue;
            }
            // Injection of the GameScript
            InjectableScript annotation = field.getAnnotation(InjectableScript.class);
            GameScript toInject = globalGameScripts.get(field.getType());
            if (null == toInject) {
                toInject = gameScripts.get(field.getType());
            }
            if (null != annotation && null != toInject) {
                field.setAccessible(true);
                field.set(gameScript, toInject);
            }
        }
        return true;
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

    /**
     * Shortcut to register mouse click actions
     *
     * @param callback the callback to register
     */
    public void registerMouseClickAction(MouseClickCallback callback) {
        this.getWindow().mouse().registerMouseClickCallback(callback);
    }

    /**
     * Register a gamescript to persist across all scenes
     *
     * @param object the global gamescript to register
     */
    public static void registerGlobal(GameScript object) {
        // we set the context when the scene is switched
        registerGameScript(object, globalGameScripts);
    }

    /**
     * Register a Game Script to be ran during the Scene
     *
     * @param object the Gamescript to register
     */
    public void register(GameScript object) {
        object.setContext(this);
        registerGameScript(object, gameScripts);
    }

    @SuppressWarnings("rawtypes")
    private static void registerGameScript(GameScript object, ListOrderedMap<Class, GameScript> source) {
        //Check if we have to modify the order
        InitializeSelfBefore[] annotations = object.getClass().getAnnotationsByType(InitializeSelfBefore.class);
        for (InitializeSelfBefore annotation : annotations) {
            if (null != annotation) {
                int scriptIndex = source.indexOf(annotation.clazz());
                if (scriptIndex != -1) {
                    source.put(scriptIndex, object.getClass(), object);
                    return;
                }
            }
        }
        // Insert the game script normally
        source.put(object.getClass(), object);
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

    public static Collection<GameScript> getGlobals() {
        return globalGameScripts.values();
    }

    public static GameScript getGlobalInstance(Class c) {
        return globalGameScripts.get(c);
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
