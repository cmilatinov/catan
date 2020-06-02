import gameplay.Board;
import main.Engine;
import main.Scene;
import main.SceneManager;
import resources.GameResources;
import resources.Resource;
import scene.Game;
import scene.MainMenu;
import scripts.PlayerHand;
import scripts.UI;
import ui.animation.UIAnimationMetrics;
import ui.animation.UIInterpolators;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    public static void main(String[] args) throws Exception {
        var engine = new Engine();
        SceneManager sceneManager = new SceneManager(engine);
        sceneManager.addScene(new Game());
        sceneManager.addScene(new MainMenu());
        sceneManager.loadScene(MainMenu.class);
        engine.run();
    }
}
