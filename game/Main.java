import camera.Camera;
import camera.PanCamera;

import org.joml.Vector3f;
import scripts.PlayerHand;
import gameplay.Board;
import scripts.UI;

import main.Engine;
import main.Scene;
import org.joml.Vector3f;
import resources.GameResources;
import resources.Resource;
import scripts.PlayerHand;
import scripts.UI;
import ui.animation.UIAnimationMetrics;
import ui.animation.UIInterpolators;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    public static void main(String[] args) {
        var engine = new Engine();

        Scene scene = engine.getCurrentScene();
        Camera orbitCamera = new PanCamera(70, scene.getWindow())
                .rotate(new Vector3f(60, 0, 0))
                .translate(new Vector3f(0, 5, -5));
        scene.setCamera(orbitCamera);

        // Skybox
        scene.setSkyboxTexture(GameResources.get(Resource.TEXTURE_SKYBOX));
        // R to Reset Camera
        scene.registerKeyUpAction(GLFW_KEY_R, (int mods) -> scene.getCamera().reset());

        // Q to toggle wireframe
        AtomicBoolean wireframe = new AtomicBoolean(false);
        scene.registerKeyUpAction(GLFW_KEY_ESCAPE, (int mods) -> scene.getWindow().close());
        scene.registerKeyUpAction(GLFW_KEY_Q, (int mods) -> {
            if(!wireframe.get())
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            else
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            wireframe.set(!wireframe.get());
        });

        scene.registerKeyUpAction(GLFW_KEY_1, (int mods) -> {
            var test = engine.newScene();
            test.register(new PlayerHand());
            engine.setCurrentScene(test);
        });
        scene.registerKeyUpAction(GLFW_KEY_2, (int mods) -> engine.setCurrentScene(scene));

        // registering display
        scene.register(new Board(3));

        UI ui = new UI();
        scene.registerKeyUpAction(GLFW_KEY_G, (int mods) ->
                ui.box.animator().animate(new UIAnimationMetrics(0, 0, 1.0f, 0), UIInterpolators.EASE_IN_OUT, 0.7f));
        scene.registerKeyUpAction(GLFW_KEY_F, (int mods) ->
                ui.box.animator().animate(new UIAnimationMetrics(0, 0, 0.5f, 360), UIInterpolators.EASE_IN_OUT, 0.7f));
        scene.registerKeyUpAction(GLFW_KEY_V, (int mods) ->
                ui.text.setFont(new Font("Verdana", Font.ITALIC, 12)));
        scene.register(ui);
        scene.register(new PlayerHand());

        engine.run();
    }
}
