package main;

import camera.Camera;
import camera.CameraFPS;
import display.Window;
import entities.Entity;
import input.KeyActionCallback;
import input.MouseClickCallback;
import lights.Light;
import objects.*;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.joml.Vector3f;
import physics.PhysicsManager;
import ui.UIManager;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static objects.GameScript.State;

/**
 * Represents a collection of entities, lights and UI elements that make up a 3D scene.
 */
@SuppressWarnings("unused")
public class Scene implements FreeableObject {

    /**
     * A list of all entities active in this scene. Entities not registered to the scene will NOT appear in this list,
     * and in turn, will not be updated or rendered.
     */
    private final List<Entity> entityList = new ArrayList<>();

    /**
     * A list of all lights present in the scene. Lights not registered to the scene will NOT appear in this list,
     * and in turn, will not have any lighting effects on objects in this scene.
     */
    private final List<Light> lights = new ArrayList<>();

    /**
     * A mapped version of the {@link #entityList} ordered by mesh and then by texture.
     */
    private final Map<Mesh, Map<Texture, List<Entity>>> entityMap = new HashMap<>();

    /**
     * A list of {@link GameScript}s that persist across scenes. A {@link GameScript} defines logic pertaining to components of a scene.
     */
    private static final ListOrderedMap<Class<? extends GameScript>, GameScript> globalGameScripts = new ListOrderedMap<>();

    /**
     * A list of {@link GameScript}s attached to this scene. A {@link GameScript} defines logic pertaining to components of a scene.
     */
    private final ListOrderedMap<Class<? extends GameScript>, GameScript> gameScripts = new ListOrderedMap<>();

    /**
     * The {@link UIManager} in charge of properly sizing the UI.
     */
    private UIManager uiManager;

    /**
     * The {@link SceneManager} that handles scene switching.
     */
    private SceneManager sceneManager;

    /**
     * The {@link PhysicsManager} in charge of handle physics-specific calculations.
     */
    private final PhysicsManager physics = new PhysicsManager(this);

    /**
     * The {@link Window} to which this scene is to be drawn.
     */
    private Window window;

    /**
     * The {@link Texture} to render as a skybox for the scene. Null to indicate that no skybox should be drawn.
     */
    private Texture skybox = null;

    /**
     * The {@link Camera} through which the view of the scene is rendered. This {@link Camera} instance may change
     * at any time during this {@link Scene}'s lifespan.
     */
    private Camera camera;

    /**
     * The callback to invoke whenever a click event was not handled by the {@link UIManager}.
     */
    private MouseClickCallback onSceneClick;

    /**
     * The reference to the scene's on click
     */
    private int onClickRef;

    /**
     * Initializes the scene to a functional state. This method MUST be called before the scene can be used.
     *
     * @param window The {@link Window} instance to attach to this scene.
     */
    public void setup(Window window, SceneManager sceneManager) {
        this.window = window;
        this.sceneManager = sceneManager;
        this.uiManager = new UIManager(window);
        this.camera = new CameraFPS(70, window).translate(new Vector3f(0, 0, 1));
        this.onClickRef = window.mouse().registerMouseClickCallback(this::onClick);
    }

    /**
     * This initialization method is called when the scene is registered. It is used to allow for the
     * implementation of custom logic at startup.
     */
    public void initialize() {
    }

    /**
     * Indicates if this scene has been properly initialized.
     *
     * @return <b>boolean</b> True if the scene was properly initialized, false otherwise.
     */
    protected boolean isReady() {
        return this.window != null;
    }

    /**
     * Returns the {@link Window} to which this scene is to be drawn.
     *
     * @return {@link Window} The {@link Window} to which this scene is to be drawn.
     */
    public Window getWindow() {
        return window;
    }

    /**
     * Returns this scene's {@link UIManager} instance used to modify the UI displayed in the scene.
     *
     * @return {@link UIManager} This scene's {@link UIManager}.
     */
    public UIManager getUiManager() {
        return uiManager;
    }

    /**
     * Returns this scene's {@link PhysicsManager} instance used to run physics calculations for the scene.
     *
     * @return {@link PhysicsManager} This scene's {@link PhysicsManager}.
     */
    public PhysicsManager physics() {
        return this.physics;
    }

    /**
     * Get the active {@link Camera} instance through which the scene is rendered.
     *
     * @return {@link Camera} The camera whose view will be rendered to the scene's attached window.
     */
    public Camera getCamera() {
        return this.camera;
    }

    /**
     * Returns a list of all entities registered to this scene.
     *
     * @return {@link List<Entity>} A collection of entities registered to this scene.
     */
    public List<Entity> getEntityList() {
        return entityList;
    }

    /**
     * Returns an ordered map of all entities registered to this scene.
     *
     * @return {@link Map} A map of entities registered to this scene ordered by mesh and texture.
     */
    public Map<Mesh, Map<Texture, List<Entity>>> getEntityMap() {
        return entityMap;
    }

    /**
     * Returns a list of all lights registered to this scene.
     *
     * @return {@link List<Light>} A collection of lights registered to this scene.
     */
    public List<Light> getLightList() {
        return lights;
    }


    /**
     * Sets this scene's active {@link Camera} instance. The active {@link Camera} is the camera through
     * which the scene is rendered.
     *
     * @param camera The new active {@link Camera} instance.
     */
    public void setCamera(Camera camera) {
        if (this.camera != null) {
            this.camera.destroy();
        }
        this.camera = camera;
    }

    /**
     * Sets the callback invoked when a mouse click was not handled by the UI.
     *
     * @param callback The new callback to invoke whenever the scene is clicked.
     */
    public void setOnSceneClick(MouseClickCallback callback) {
        this.onSceneClick = callback;
    }

    /**
     * Run any attached GameScripts which handle game/ui logic
     *
     * @param delta the time delta between frames
     */
    public void update(double delta) {
        if (!isReady())
            throw new RuntimeException("Attempting to update an unregistered scene. Call UIManager.registerScene before loading a new scene.");

        camera.update(delta);
        updateGameScripts(delta, globalGameScripts.values());
        updateGameScripts(delta, gameScripts.values());
        uiManager.update(delta);
    }

    /**
     * Update a set of {@link GameScript} instances.
     *
     * @param delta The amount of time that has passed since the last rendered frame in seconds.
     * @param scripts The list of scripts to update.
     */
    private void updateGameScripts(double delta, Collection<GameScript> scripts) {
        for (GameScript gameScript : scripts) {
            switch (gameScript.getCurrentState()) {
                case TO_START -> {
                    gameScript.start();
                    gameScript.setState(State.TO_UPDATE);
                }
                case TO_STOP -> {
                    gameScript.stop();
                    gameScript.setState(State.STOPPED);
                }
                case TO_UPDATE -> gameScript.update(delta);
                case TO_INITIALIZE -> {
                    if (!handleDependencies(gameScript))
                        throw new RuntimeException("Unable to resolve dependencies for: " + gameScript.getClass());
                    gameScript.initialize();
                    gameScript.setState(State.TO_UPDATE);
                }
                case RE_INITIALIZE -> {
                    gameScript.initialize();
                    gameScript.setState(State.TO_UPDATE);
                }
            }
        }
    }

    /**
     * Invoked whenever a mouse button is pressed or released. This method attempts to pass the click to the underlying {@link UIManager}.
     * If the {@link UIManager} fails to handle the click, the scene's {@link #onSceneClick} callback is invoked.
     *
     * @param button The mouse button that was actioned. (ex: {@link org.lwjgl.glfw.GLFW#GLFW_MOUSE_BUTTON_LEFT}, {@link org.lwjgl.glfw.GLFW#GLFW_MOUSE_BUTTON_RIGHT})
     * @param action Indicates whether the button was pressed or released. (ex: {@link org.lwjgl.glfw.GLFW#GLFW_PRESS}, {@link org.lwjgl.glfw.GLFW#GLFW_RELEASE})
     * @param mods The modifiers for the action. (ex: {@link org.lwjgl.glfw.GLFW#GLFW_MOD_CONTROL}, {@link org.lwjgl.glfw.GLFW#GLFW_MOD_SHIFT}, {@link org.lwjgl.glfw.GLFW#GLFW_MOD_ALT})
     */
    private void onClick(int button, int action, int mods) {
        if (!uiManager.onMouseClick(button, action, mods) && onSceneClick != null)
            onSceneClick.invoke(button, action, mods);
    }

    /**
     * Handle dependency injection of other scripts and deferring the initialization
     * NOTE: only really handles one level of dependencies, will have to come back to this is we need
     * a more robust solution
     *
     * @param gameScript The {@link GameScript} instance in which to inject dependencies.
     * @return True if all dependencies were injected correctly.
     */
    protected boolean handleDependencies(GameScript gameScript) {
        List<Field> fields = Arrays.stream(gameScript.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(InjectableScript.class))
                .collect(Collectors.toList());
        for (Field field : fields) {
            GameScript toInject = globalGameScripts.get(field.getType());
            if (toInject == null)
                toInject = gameScripts.get(field.getType());
            if (toInject != null) {
                field.setAccessible(true);
                try {
                    field.set(gameScript, toInject);
                } catch (IllegalAccessException e) {
                    return false;
                }
                field.setAccessible(false);
            }
        }
        return true;
    }

    /**
     * Loads a new scene instance from its class.
     *
     * @param sceneClass The {@link Scene} derived class to load.
     */
    public void loadNewScene(Class<? extends Scene> sceneClass) {
        sceneManager.loadScene(sceneClass);
    }

    /**
     * This method serves as a shortcut to register key up actions. See {@link input.KeyboardInput#registerKeyUp}.
     *
     * @param code     The keycode to bind the callback to.
     * @param callback The callback to execute when the key is released.
     */
    public void registerKeyUpAction(int code, KeyActionCallback callback) {
        this.getWindow().keyboard().registerKeyUp(code, callback);
    }

    /**
     * This method serves as a shortcut to register key down actions. See {@link input.KeyboardInput#registerKeyDown}.
     *
     * @param code     The keycode to bind the callback to.
     * @param callback The callback to execute when the key is pressed.
     */
    public void registerKeyDownAction(int code, KeyActionCallback callback) {
        this.getWindow().keyboard().registerKeyDown(code, callback);
    }

    /**
     * This method serves as a shortcut to register mouse click actions. See {@link input.MouseInput#registerMouseClickCallback}.
     *
     * @param callback The callback to invoke whenever a mouse button is activated.
     */
    public int registerMouseClickAction(MouseClickCallback callback) {
        return window.mouse().registerMouseClickCallback(callback);
    }

    /**
     * Registers a global {@link GameScript} to persisting across all scenes.
     *
     * @param script The game script to register.
     */
    public static void registerGlobal(GameScript script) {
        // we set the context when the scene is switched
        registerGameScript(script, globalGameScripts);
    }

    /**
     * Registers a {@link GameScript} to the scene.
     *
     * @param script The game script to register.
     */
    public void register(GameScript script) {
        script.setContext(this);
        registerGameScript(script, gameScripts);
    }

    private static void registerGameScript(GameScript object, ListOrderedMap<Class<? extends GameScript>, GameScript> source) {
        // Check if we have to modify the order
        InitializeSelfBefore[] annotations = object.getClass().getAnnotationsByType(InitializeSelfBefore.class);
        for (InitializeSelfBefore annotation : annotations) {
            if (annotation != null) {
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
     * Registers an {@link Entity} to the scene. Entities MUST be registered to a scene in order to be properly updated.
     *
     * @param ent The entity to register.
     */
    public void register(Entity ent) {
        Texture texture = ent.getModel().getTexture();
        Mesh mesh = ent.getModel().getMesh();

        entityList.add(ent);

        if (!entityMap.containsKey(mesh))
            entityMap.put(ent.getModel().getMesh(), new HashMap<>() {{
                put(ent.getModel().getTexture(), new ArrayList<>() {{
                    add(ent);
                }});
            }});
        else if (!entityMap.get(mesh).containsKey(texture))
            entityMap.get(mesh).put(texture, new ArrayList<>() {{
                add(ent);
            }});
        else
            entityMap.get(mesh).get(texture).add(ent);
    }

    /**
     * Registers a {@link Light} to the scene. Lights MUST be registered to a scene in order to affect visual lighting.
     *
     * @param light The light to be registered.
     */
    public void register(Light light) {
        lights.add(light);
    }

    /**
     * Removes an {@link Entity} from the scene.
     *
     * @param ent The entity instance to remove.
     */
    public void remove(Entity ent) {
        Texture texture = ent.getModel().getTexture();
        Mesh mesh = ent.getModel().getMesh();

        entityList.remove(ent);

        if (!entityMap.containsKey(mesh))
            return;

        if (!entityMap.get(mesh).containsKey(texture))
            return;

        entityMap.get(mesh).get(texture).remove(ent);
    }

    /**
     * Removes a {@link Light} from the scene.
     *
     * @param light The light instance to remove.
     */
    public void remove(Light light) {
        lights.remove(light);
    }

    /**
     * Set the skybox texture to be used for this scene.
     *
     * @param skybox The texture to render as a skybox for the scene.
     */
    public void setSkyboxTexture(Texture skybox) {
        this.skybox = skybox;
    }

    /**
     * Returns the texture to be used as a skybox for this scene.
     *
     * @return {@link Texture} The texture to render as a skybox for the scene.
     */
    public Texture getSkyboxTexture() {
        return skybox;
    }

    /**
     * Returns the collection of global {@link GameScript}s.
     *
     * @return The collection of global {@link GameScript}s.
     */
    public static Collection<GameScript> getGlobalScripts() {
        return globalGameScripts.values();
    }

    /**
     * Returns a specific global {@link GameScript} instance.
     *
     * @param scriptClass The class of the script to return.
     * @return The resulting {@link GameScript}.
     */
    @SuppressWarnings("unchecked")
    public static <T extends GameScript> T getGlobalScriptInstance(Class<T> scriptClass) {
        return (T) globalGameScripts.get(scriptClass);
    }

    /**
     * {@inheritDoc}
     */
    public void destroy() {
        camera.destroy();
        uiManager.destroy();
        window.mouse().removeMouseClickCallback(this.onClickRef);
        entityList.clear();
        entityMap.clear();
        lights.clear();
        gameScripts.clear();
    }

}
