package scene;

import gameplay.Board;
import main.Scene;
import resources.GameResources;
import resources.Resource;
import scripts.PlayerHand;
import scripts.UI;
import ui.animation.UIAnimationMetrics;
import ui.animation.UIInterpolators;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_V;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_FILL;

public class Game extends Scene{

    @Override
    public void initialize() {
        // Skybox
        setSkyboxTexture(GameResources.get(Resource.TEXTURE_SKYBOX));
        // R to Reset Camera
        registerKeyUpAction(GLFW_KEY_R, (int mods) -> getCamera().reset());

        // Q to toggle wireframe
        AtomicBoolean wireframe = new AtomicBoolean(false);
        registerKeyUpAction(GLFW_KEY_ESCAPE, (int mods) -> getWindow().close());
        registerKeyUpAction(GLFW_KEY_Q, (int mods) -> {
            if(!wireframe.get())
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            else
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            wireframe.set(!wireframe.get());
        });

        registerKeyUpAction(GLFW_KEY_1, (int mods) -> sceneManager.loadScene(MainMenu.class));
        registerKeyUpAction(GLFW_KEY_2, (int mods) -> sceneManager.loadScene(Game.class));

        // registering display
        register(new Board(3));

        UI ui = new UI();
        registerKeyUpAction(GLFW_KEY_G, (int mods) ->
                ui.box.animator().animate(new UIAnimationMetrics(0, 0, 1.0f, 0), UIInterpolators.EASE_IN_OUT, 0.7f));
        registerKeyUpAction(GLFW_KEY_F, (int mods) ->
                ui.box.animator().animate(new UIAnimationMetrics(0, 0, 0.5f, 360), UIInterpolators.EASE_IN_OUT, 0.7f));
        registerKeyUpAction(GLFW_KEY_V, (int mods) ->
                ui.text.setFont(new Font("Verdana", Font.ITALIC, 12)));

        register(ui);
//        register(new PlayerHand());
    }
}
