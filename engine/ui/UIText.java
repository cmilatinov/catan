package ui;

import objects.Texture;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static org.lwjgl.opengl.GL11.*;

public final class UIText extends UIComponent {

    private final JLabel label;
    boolean shouldUpdateImage = true;

    public UIText(Font font, String text) {
        this.label = new JLabel(text, SwingConstants.CENTER);
        this.label.setVerticalAlignment(SwingConstants.CENTER);
        this.label.setFont(font);
    }

    public UIText setText(String text) {
        this.label.setText(text);
        this.shouldUpdateImage = true;
        return this;
    }

    public UIText setFont(Font font) {
        this.label.setFont(font);
        this.shouldUpdateImage = true;
        return this;
    }

    public UIText setColor(UIColor color) {
        this.label.setForeground(new Color(color.getR(), color.getG(), color.getB(), color.getA()));
        this.shouldUpdateImage = true;
        return this;
    }

    public void drawImageToTexture(Texture texture) {

        // Create new image of argb format
        BufferedImage image = new BufferedImage(dimensions.getWidth(), dimensions.getHeight(), TYPE_INT_ARGB);

        // Create graphics for the image
        Graphics g = image.createGraphics();

        // Set label dimensions
        label.setSize(dimensions.getWidth(), dimensions.getHeight());

        // Paint the label using graphics
        label.paint(g);

        // Dispose of the graphics
        g.dispose();

        // Copy the result into the texture
        int[] imageData = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        glBindTexture(texture.getType(), texture.getTextureID());
        glTexImage2D(GL_TEXTURE_2D,
                0,
                GL_RGBA,
                image.getWidth(),
                image.getHeight(),
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                imageData);

    }

}
