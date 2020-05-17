package scripts;

import objects.GameObject;
import org.joml.Vector2f;
import resources.GameResources;
import resources.Resource;
import ui.UIConstraints;
import ui.UISprite;
import ui.constraints.CenterConstraint;
import ui.constraints.PixelConstraint;

public class PlayerHand extends GameObject {
    private static final int FACTOR_RADIUS = 1;
    private static final int FACTOR_ELEVATION = 1 / 12;
    // Cards
    UISprite[] cards = new UISprite[10];

    @Override
    public void initialize() {
        for (int i = 0; i < cards.length; i++) {
            int r = (int) Math.floor(Math.random() * 5);
            switch (r) {
                case 0 -> cards[i] = new UISprite(GameResources.get(Resource.TEXTURE_CARD_BRICK));
                case 1 -> cards[i] = new UISprite(GameResources.get(Resource.TEXTURE_CARD_STONE));
                case 2 -> cards[i] = new UISprite(GameResources.get(Resource.TEXTURE_CARD_SHEEP));
                case 3 -> cards[i] = new UISprite(GameResources.get(Resource.TEXTURE_CARD_FOREST));
                case 4 -> cards[i] = new UISprite(GameResources.get(Resource.TEXTURE_CARD_WHEAT));
            }
            var constraint = new UIConstraints()
                    .setHeightWidth(new PixelConstraint(150))
                    .setX(new CenterConstraint(i * 50))
                    .setY(new CenterConstraint(-100))
                    .setRotation(90);
            getScene().getUiManager().getContainer().add(cards[i], constraint);

        }
    }

    @Override
    public void destroy() {

    }
}
