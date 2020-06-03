package scene;

import entities.*;
import lights.Light;
import main.Scene;
import org.joml.Vector3f;
import resources.GameResources;
import resources.Resource;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_FILL;

public class GameManager extends Scene{


    public GameManager() {

    }

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
        registerKeyUpAction(GLFW_KEY_2, (int mods) -> sceneManager.loadScene(GameManager.class));

        Entity table = Table.create()
                .scale(10)
                .translate(new Vector3f(0, -0.07f, 0));

        register(table);

        Light sun = new Light(new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(500, 1000, 500));
        Light sun2 = new Light(new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(-500, 1000, 500));
        register(sun);
        register(sun2);

        register(new scripts.GameManager());
    }
}
