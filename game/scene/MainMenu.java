package scene;

import main.Scene;
import resources.GameResources;
import resources.Resource;
import ui.UIButton;
import ui.UIComponent;
import ui.UIConstraints;
import ui.UISprite;
import ui.constraints.*;

import static ui.UIDimensions.DIRECTION_BOTTOM;

public class MainMenu extends Scene {

    @Override
    public void initialize() {
        UIComponent container = getUiManager().getContainer();

        UISprite mainMenuSprite = new UISprite(GameResources.get(Resource.TEXTURE_MAIN_MENU));
        mainMenuSprite.setInteractive(false);
        AspectCoverConstraint constraint = new AspectCoverConstraint(21.0f / 9.0f);
        UIConstraints backgroundConstraints = new UIConstraints()
                .setAll(constraint);
        container.add(mainMenuSprite, backgroundConstraints);

        UIComponent buttonContainer = new UIComponent();
        UIConstraints buttonContainerConstraints = new UIConstraints()
                .setX(new PixelConstraint(50))
                .setY(new PixelConstraint(50, DIRECTION_BOTTOM))
                .setWidth(new RelativeConstraint(0.35f))
                .setHeight(new ParentMinusDiffConstraint(100));
        container.add(buttonContainer, buttonContainerConstraints);

        // Setup constraints for the Load Game Button
        UIButton loadGameButton = new UIButton("Load Game")
                .setOnClick(() -> loadNewScene(Game.class));
        UIConstraints loadConstraints = new UIConstraints()
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(120, DIRECTION_BOTTOM))
                .setHeight(new PixelConstraint(80));

        // Register the Load Game button
        buttonContainer.add(loadGameButton, loadConstraints);

        // Setup constraints for Exit Button
        UIButton exitGame = new UIButton("Exit Game")
                .setOnClick(() -> getWindow().close());
        UIConstraints exitConstraints = new UIConstraints()
                .setX(new CenterConstraint())
                .setY(new PixelConstraint(0, DIRECTION_BOTTOM))
                .setHeight(new PixelConstraint(80));
        // Register Exit button
        buttonContainer.add(exitGame, exitConstraints);
    }

}
