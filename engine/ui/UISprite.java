package ui;

import objects.Texture;
import render.Sprite;

public final class UISprite extends UIComponent implements Sprite {

    protected Texture sprite;

    public UISprite(Texture sprite) {
        this.sprite = sprite;
    }

    public Texture getTexture() {
        return this.sprite;
    }

    public Sprite setTexture(Texture texture) {
        this.sprite = texture;
        return this;
    }

}
