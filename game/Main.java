import display.Window;
import main.Engine;
import main.Scene;
import main.SceneManager;
import resources.GameResources;
import scene.Game;
import scene.MainMenu;
import settings.SettingsManager;

public class Main {

    public static void main(String[] args) {
        Window window = new Window("Catan");
        Engine engine = new Engine(window, GameResources.class, MainMenu.class);

        Scene.registerGlobal(new SettingsManager("./config/config.xml"));

        SceneManager sceneManager = engine.getSceneManager();
        sceneManager.registerScene(MainMenu.class);
        sceneManager.registerScene(Game.class);

        engine.start();
    }

}
