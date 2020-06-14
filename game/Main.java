import main.Engine;
import main.SceneManager;
import scene.Game;
import scene.MainMenu;

public class Main {
    public static void main(String[] args) throws Exception {
        var engine = new Engine();

        SceneManager sceneManager = new SceneManager(engine);
        sceneManager.addScene(new Game());
        sceneManager.addScene(new MainMenu());
        sceneManager.loadScene(Game.class);

        engine.run();
    }
}
