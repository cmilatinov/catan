package scripts;

import main.SceneManager;
import objects.GameResourceFactory;
import objects.GameScript;
import objects.Texture;
import scene.Game;
import ui.*;
import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static resources.GameResources.TEXTURE_PATH;

public class MainMenu extends GameScript {
    @Override
    public void initialize() {
        SceneManager sceneManager = this.getScene().getSceneManager();
        Texture mainMenuTexture = GameResourceFactory.loadTexture2D(TEXTURE_PATH + "mainmenu.jpg", GL_LINEAR, true);
        UISprite mainMenuSprite = new UISprite(mainMenuTexture);
        var backgroundConstraints = new UIConstraints()
                .setX(new PixelConstraint(0, UIDimensions.DIRECTION_LEFT))
                .setY(new PixelConstraint(0, UIDimensions.DIRECTION_TOP))
                .setWidth(new RelativeConstraint(1.0f))
                .setHeight(new RelativeConstraint(1.0f));
        getScene().getUiManager().getContainer().add(mainMenuSprite, backgroundConstraints);
        var loadGameButton = new UIButton() {
            @Override
            public void onMouseHoverExit() {
                this.setColor(UIColor.DARK_GRAY);
            }

            @Override
            public void onMouseHover() {
                this.setColor(UIColor.LIGHT_GRAY);
            }

            @Override
            public void onMouseClick() {
                sceneManager.loadScene(Game.class);
            }
        };
        // Setup constraints for the Load Game Button
        UIConstraints constraints = new UIConstraints()
                .setX(new PixelConstraint(30, UIDimensions.DIRECTION_LEFT))
                .setY(new PixelConstraint(30, UIDimensions.DIRECTION_TOP))
                .setWidth(new RelativeConstraint(0.1f))
                .setHeight(new RelativeConstraint(0.1f));
        // Register the Load Game button
        loadGameButton.setColor(UIColor.WHITE);
        loadGameButton.setText("Load Game");
        mainMenuSprite.add(loadGameButton, constraints);

        var exitGame = new UIButton() {
            @Override
            public void onMouseHoverExit() {
                this.setColor(UIColor.DARK_GRAY);
            }

            @Override
            public void onMouseHover() {
                this.setColor(UIColor.LIGHT_GRAY);
            }

            @Override
            public void onMouseClick() {
                getWindow().close();
            }
        };
        // Setup constraints for Exit Button
        UIConstraints constraints2 = new UIConstraints()
                .setX(new PixelConstraint(30, UIDimensions.DIRECTION_LEFT))
                .setY(new PixelConstraint(200, UIDimensions.DIRECTION_TOP))
                .setWidth(new RelativeConstraint(0.1f))
                .setHeight(new RelativeConstraint(0.1f));
        // Register Exit button
        exitGame.setColor(UIColor.WHITE);
        exitGame.setText("Exit Game");
        mainMenuSprite.add(exitGame, constraints2);
    }

    @Override
    public void destroy() {

    }
}
