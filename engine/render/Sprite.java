package render;

import objects.Texture;

public interface Sprite {

    /**
     * Get the Texture object assigned to the sprite
     * @return The Texture
     */
    public Texture getTexture();

    /**
     * Set the texture to be useed by the sprite
     * @param texture The new Texture
     * @return Reference to the Sprite
     */
    public Sprite setTexture(Texture texture);

}
