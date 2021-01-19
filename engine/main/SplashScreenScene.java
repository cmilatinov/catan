package main;

import objects.GameResourceFactory;
import objects.Texture;
import ui.UIConstraints;
import ui.UIDimensions;
import ui.UIProgressBar;
import ui.UISprite;
import ui.constraints.AspectConstraint;
import ui.constraints.CenterConstraint;
import ui.constraints.RelativeConstraint;

import static org.lwjgl.opengl.GL11.GL_LINEAR;

public class SplashScreenScene extends Scene {

    private final UIProgressBar progressBar = new UIProgressBar();
    private final String splashPath;

    public SplashScreenScene(String splashPath) {
        this.splashPath = splashPath;
    }

    @Override
    public void initialize() {
        if (splashPath == null)
            Engine.error(new RuntimeException("Splash path not set prior to initializing the scene."));

        Texture logoTexture = GameResourceFactory.loadTexture2D("./textures/gears.png", GL_LINEAR, false);
        UISprite logo = new UISprite(logoTexture);
        UIConstraints logoConstraints = new UIConstraints()
                .setX(new CenterConstraint())
                .setY(new RelativeConstraint(0.2f))
                .setHeight(new RelativeConstraint(0.4f))
                .setWidth(new AspectConstraint(1));
        getUiManager().getContainer().add(logo, logoConstraints);

        UIConstraints progressBarConstraints = new UIConstraints()
                .setX(new CenterConstraint())
                .setY(new RelativeConstraint(0.2f, UIDimensions.DIRECTION_BOTTOM))
                .setWidth(new RelativeConstraint(0.6f))
                .setHeight(new RelativeConstraint(0.1f));
        getUiManager().getContainer().add(progressBar, progressBarConstraints);
    }

    public SplashScreenScene setLoadingProgress(float progress) {
        progressBar.setProgress(progress);
        return this;
    }

}
