package ui.components;

import resources.GameResources;
import resources.Resource;
import ui.*;
import ui.constraints.CenterConstraint;
import ui.constraints.RelativeConstraint;

public class TradeMenu extends UIComponent {

    private static class Panel extends UIQuad {
        @Override
        public UIComponent setVisible(boolean visible) {
            for (UISprite sprite : types) {
                sprite.setVisible(visible);
            }
            return super.setVisible(visible);
        }

        UISprite brick = new UISprite(GameResources.get(Resource.TEXTURE_CARD_BRICK));
        UISprite wheat = new UISprite(GameResources.get(Resource.TEXTURE_CARD_WHEAT));
        UISprite stone = new UISprite(GameResources.get(Resource.TEXTURE_CARD_STONE));
        UISprite sheep = new UISprite(GameResources.get(Resource.TEXTURE_CARD_SHEEP));

        private final UISprite[] types = new UISprite[] {
                brick, wheat, stone, sheep
        };

        private UIConstraints getSpriteConstraints(float relative)
        {
            return new UIConstraints()
                .setX(new CenterConstraint())
                .setY(new RelativeConstraint(relative))
                .setHeight(new RelativeConstraint(0.2f))
                .setWidth(new RelativeConstraint(1f));
        }

        public Panel() {
            float offset = 0;
            for (UISprite sprite : types) {
                this.add(sprite, getSpriteConstraints(offset));
                offset += 0.20f;
            }
        }
    }

    private boolean isActive;

    UIColor background =  new UIColor(0.77f, 0.65f, 0.4f, 1);

    private final Panel ourPanel = new Panel();
    private final Panel otherPanel = new Panel();

    public TradeMenu()
    {
        ourPanel.setColor(background);
        ourPanel.setVisible(false);
        otherPanel.setColor(background);
        otherPanel.setVisible(false);

        this.add(ourPanel, new UIConstraints()
                .setX(new CenterConstraint(-200))
                .setY(new CenterConstraint())
                .setHeight(new RelativeConstraint(1f))
                .setWidth(new RelativeConstraint(1f)));
        this.add(otherPanel, new UIConstraints()
                .setX(new CenterConstraint(200))
                .setY(new CenterConstraint())
                .setHeight(new RelativeConstraint(1f))
                .setWidth(new RelativeConstraint(1f)));
    }

    public UIConstraints getConstraints() {
        return new UIConstraints()
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setHeight(new RelativeConstraint(0.60f))
                .setWidth(new RelativeConstraint(0.20f));
    }

    public void toggle() {
        isActive = !isActive;
        ourPanel.setVisible(isActive);
        otherPanel.setVisible(isActive);
    }
}
