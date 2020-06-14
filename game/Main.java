import main.Engine;
import main.Scene;
import main.SceneManager;
import scene.Game;
import scene.MainMenu;
import settings.SettingsManager;

public class Main {
    public static void main(String[] args) throws Exception {
        var engine = new Engine();

        Scene.registerGlobal(new SettingsManager("./config/config.xml"));

        SceneManager sceneManager = new SceneManager(engine);
        sceneManager.addScene(new Game());
        sceneManager.addScene(new MainMenu());
        sceneManager.loadScene(Game.class);

        engine.run();
    }
}
