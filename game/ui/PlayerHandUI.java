package ui;

import entities.board.Tile;
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

    public void addCard(int type) {
        UISprite card = new UISprite(getTextureFromType(type));
        UIConstraints constraints = new UIConstraints()
                .setX(new AbsoluteConstraint(100 + (cards.size() * 50), UIDimensions.DIRECTION_LEFT))
                .setY(new AbsoluteConstraint(100, UIDimensions.DIRECTION_BOTTOM))
                .setHeight(new PixelConstraint(200))
                .setWidth(new AspectConstraint(1));

        cards.add(card);
        getScene().getUiManager().getContainer().add(card, constraints);
    }

    public Texture getTextureFromType (int type)
    {
        return switch (type) {
            case Tile.WOOD -> GameResources.get(Resource.TEXTURE_CARD_FOREST);
            case Tile.WHEAT -> GameResources.get(Resource.TEXTURE_CARD_WHEAT);
            case Tile.SHEEP -> GameResources.get(Resource.TEXTURE_CARD_SHEEP);
            case Tile.BRICK -> GameResources.get(Resource.TEXTURE_CARD_BRICK);
            case Tile.STONE -> GameResources.get(Resource.TEXTURE_CARD_STONE);
            default -> null;
        };
    }

    @Override
    public void destroy() {

    }
}
