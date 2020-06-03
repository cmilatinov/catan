package main;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {

    private final Map<String, Scene> scenes = new HashMap<>();
    private final Engine engine;

    public SceneManager(Engine engine) {
        this.engine = engine;
    }

    /**
     * Load a Scene by it's tag
     * @param tag the scene's tag
     */
    public void loadScene(String tag){
        Scene scene = this.scenes.get(tag);
        if(null == scene) {
            System.out.println("Scene " + tag + "Not found!");
            return;
        }
        engine.setCurrentScene(scene);
    }

    public void loadScene(Class<? extends Scene> sceneClass) {
        this.loadScene(sceneClass.getCanonicalName());
    }

    /**
     * Add a Scene to the manager
     * @param scene the scene to add
     */
    public void addScene(Scene scene) {
        scene.setup(engine.getWindow(), this);
        scene.initialize();
        this.scenes.put(scene.getClass().getCanonicalName(), scene);
    }

    /**
     * Remove a scene from the manager
     * @param scene the scene to remove
     */
    public void removeScene(Scene scene) {
        this.removeScene(scene.getTag());
    }

    /**
     * Remove a scene by it's class
     * @param sceneClass the class of the scene
     */
    public void removeScene(Class<Scene> sceneClass) {
        this.removeScene(sceneClass.getCanonicalName());
    }

    /**
     * Remove a scene from the manager by it's tag
     * @param tag the tag of the scene
     */
    public void removeScene(String tag) {
        this.scenes.remove(tag);
    }
}
