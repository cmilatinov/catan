import camera.Camera;
import camera.PanCamera;
import org.joml.Vector3f;
import scripts.Cards;
import scripts.Board;
import scripts.UI;
import main.Engine;
import main.Scene;
import resources.GameResources;
import resources.Resource;

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
        scene.getWindow().keyboard().registerKeyUp(GLFW_KEY_ESCAPE, (int mods) -> scene.getWindow().close());
        scene.getWindow().keyboard().registerKeyUp(GLFW_KEY_Q, (int mods) -> {
            if(!wireframe.get())
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            else
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            wireframe.set(!wireframe.get());
        });

        scene.register(new Board());
        scene.register(new UI());
        scene.register(new Cards());

        engine.run();
    }
}
