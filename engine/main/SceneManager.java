package main;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {

    private final Map<Class<? extends Scene>, Scene> scenes = new HashMap<>();
    private final Engine engine;

    public SceneManager(Engine engine) {
        this.engine = engine;
    }

    public void loadScene(Class<? extends Scene> sceneClass){
        Scene scene = scenes.get(sceneClass);
        if(null == scene) {
            System.out.println("Scene " + sceneClass.getCanonicalName() + " not found!");
            return;
        }
        scene.initialize();
        scene.setup();
        engine.setCurrentScene(scene);
    }

    public void registerScene(Scene scene) {
        scene.setup(engine.getWindow(), this);
        scenes.put(scene.getClass(), scene);
    }

    public void registerScene(Class<? extends Scene> sceneClass) {
        try {
            Scene scene = sceneClass.getConstructor().newInstance();
            registerScene(scene);
        } catch (Exception e) {
            Engine.error(e);
        }
    }

    public void removeScene(Class<? extends Scene> sceneClass) {
        scenes.remove(sceneClass);
    }

}
