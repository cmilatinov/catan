package render;

import camera.Camera;
import display.Window;
import main.Scene;
import objects.Texture;
import ui.UIRenderer;

public class SceneRenderer {

    private final EntityRenderer entityRenderer = new EntityRenderer();
    private final SkyboxRenderer skyboxRenderer = new SkyboxRenderer();
    private final UIRenderer uiRenderer;

    public SceneRenderer(Window window) {
        this.uiRenderer = new UIRenderer(window);
    }

    public void render(Scene scene) {
        Camera camera = scene.getCamera();
        Texture skybox = scene.getSkyboxTexture();
        if (skybox != null)
            skyboxRenderer.render(camera, skybox);
        entityRenderer.render(camera, scene.getEntityMap(), scene.getLightList());
        // TODO: add post-processing effects here
        uiRenderer.render(scene.getUiManager().getContainer());
    }

}
