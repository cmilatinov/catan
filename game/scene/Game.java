package scene;

import camera.Camera;
import gameplay.DevelopmentCards;
import camera.CameraPan;
import entities.Entity;
import entities.Table;
import lights.Light;
import main.Scene;
import org.joml.Vector3f;
import resources.GameResources;
import resources.Resource;
import scripts.GameManager;
import scripts.Tiles;
import settings.Settings;
import ui.*;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Game extends Scene {
    private Tiles tiles;
    private Settings settings;

    public Game() {

    }

    @Override
    public void initialize() {
        Camera camera = new CameraPan(90f, getWindow());
        camera.setPosition(new Vector3f(0, 7f, 10));
        camera.setRotation(new Vector3f(20, 0,0));
        setCamera(camera);

        settings = new Settings("./config/config.xml");

        // Skybox
        setSkyboxTexture(GameResources.get(Resource.TEXTURE_SKYBOX));

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

        Entity table = Table.create()
                .scale(10)
                .translate(new Vector3f(0, -0.07f, 0));

        register(table);

        Entity devCards = DevelopmentCards.create().scale(0.1f).translate(new Vector3f(4f, 0, 5f));
        register(devCards);

        Light sun = new Light(new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(500, 1000, 500));
        Light sun2 = new Light(new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(-500, 1000, 500));
        register(sun);
        register(sun2);

        register(settings);

        tiles = new Tiles(3);
        tiles.generateMap();
        register(tiles);

        register(new GameManager());
        register(new UI());
        register(new PlayerUI());
        register(new PlayerHandUI());
        register(new TradeMenuUI());
    }
}
