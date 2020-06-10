package ui;

import gameplay.ResourceType;
import objects.GameScript;
import objects.Texture;
import resources.GameResources;
import resources.Resource;
import ui.constraints.AbsoluteConstraint;
import ui.constraints.AspectConstraint;
import ui.constraints.PixelConstraint;

import java.util.ArrayList;

public class PlayerHandUI extends GameScript {
    ArrayList<UISprite> cards = new ArrayList<UISprite>();

    public void addCard(ResourceType type) {
        UISprite card = new UISprite(getTextureFromType(type));
        UIConstraints constraints = new UIConstraints()
                .setX(new AbsoluteConstraint(100 + (cards.size() * 50), UIDimensions.DIRECTION_LEFT))
                .setY(new AbsoluteConstraint(100, UIDimensions.DIRECTION_BOTTOM))
                .setHeight(new PixelConstraint(200))
                .setWidth(new AspectConstraint(1));

        System.out.println(card.getDimensions());

        cards.add(card);
        getScene().getUiManager().getContainer().add(card, constraints);
    }

    public Texture getTextureFromType (ResourceType type)
    {
        return switch (type) {
            case FOREST -> GameResources.get(Resource.TEXTURE_CARD_FOREST);
            case WHEAT -> GameResources.get(Resource.TEXTURE_CARD_WHEAT);
            case SHEEP -> GameResources.get(Resource.TEXTURE_CARD_SHEEP);
            case BRICK -> GameResources.get(Resource.TEXTURE_CARD_BRICK);
            case STONE -> GameResources.get(Resource.TEXTURE_CARD_STONE);

            case KNIGHT -> GameResources.get(Resource.TEXTURE_CARD_KNIGHT);
            default -> null;
        };
    }

    @Override
    public void destroy() {

    }
}
