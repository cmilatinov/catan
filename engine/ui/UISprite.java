package ui;

import objects.Texture;
import org.joml.Vector2f;
import render.Sprite;

public class UISprite extends UIQuad implements Sprite {

    protected Texture sprite;

    protected boolean ignoreAlpha = true;

    public UISprite(Texture sprite) {
        super();
        this.sprite = sprite;
    }

    /**
     * Set whether the shader should ignore transparency
     * @param status the new status of this toggle
     * @return Reference to the Sprite
     */
    public Sprite shouldIgnoreAlpha(boolean status)
    {
        this.ignoreAlpha = status;
        return this;
    }

    /**
     * Returns where setting for whether the shader should render any alpha textures
     * @return int since we don't have any Uniform Type for booleans GL20
     */
    public int getIgnoreAlpha()
    {
        return this.ignoreAlpha ? 1 : 0;
    }

    @Override
    public Texture getTexture() {
        return this.sprite;
    }

    @Override
    public Sprite setTexture(Texture texture) {
        this.sprite = texture;
        return this;
    }
}
