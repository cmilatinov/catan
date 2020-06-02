package ui;

import objects.Texture;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static org.lwjgl.opengl.GL11.*;

public class UIText extends UIComponent {

    private final JTextArea textArea;
    boolean shouldUpdateImage = true;

    public UIText(Font font, String text) {
        this.textArea = new JTextArea(text);
        this.textArea.setFont(font);
        this.textArea.setLineWrap(true);
        this.textArea.setWrapStyleWord(true);
        this.textArea.setBackground(new Color(0, 0, 0, 0));
        this.setIsInteractable(false);
    }

    public UIText setText(String text) {
        this.textArea.setText(text);
        this.shouldUpdateImage = true;
        return this;
    }

    public UIText setFont(Font font) {
        this.textArea.setFont(font);
        this.shouldUpdateImage = true;
        return this;
    }

    public UIText setColor(UIColor color) {
        this.textArea.setForeground(new Color(color.getR(), color.getG(), color.getB(), color.getA()));
        this.shouldUpdateImage = true;
        return this;
    }

    public void drawImageToTexture(Texture texture) {

        // Create new image of argb format
        BufferedImage image = new BufferedImage(dimensions.getWidth(), dimensions.getHeight(), TYPE_INT_ARGB);

        // Create graphics for the image
        Graphics g = image.createGraphics();

        // Set label dimensions
        textArea.setSize(dimensions.getWidth(), dimensions.getHeight());

        // Paint the label using graphics
        textArea.paint(g);

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
