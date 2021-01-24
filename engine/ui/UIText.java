package ui;

import objects.Texture;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static org.lwjgl.opengl.GL11.*;

public class UIText extends UIComponent {

    private final SimpleAttributeSet attrs = new SimpleAttributeSet();
    private final JTextPane textPane;
    boolean shouldUpdateImage = true;

    public UIText(Font font, String text) {
        textPane = new JTextPane();
        textPane.setFont(font);
        textPane.setEditorKit(new VerticalCenterEditorKit());
        StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_CENTER);
        StyledDocument doc = (StyledDocument) textPane.getDocument();
        textPane.setText(text);
        textPane.setBackground(new Color(0, 0, 0, 0));

        doc.setParagraphAttributes(0,doc.getLength() - 1, attrs, false);
        setInteractive(false);
    }

    public UIText setText(String text) {
        this.textPane.setText(text);
        StyledDocument doc = (StyledDocument) textPane.getDocument();
        doc.setParagraphAttributes(0,doc.getLength() - 1, attrs, false);
        this.shouldUpdateImage = true;
        return this;
    }

    public UIText setFont(Font font) {
        this.textPane.setFont(font);
        this.shouldUpdateImage = true;
        return this;
    }

    public UIText setColor(UIColor color) {
        this.textPane.setForeground(new Color(color.getR(), color.getG(), color.getB(), color.getA()));
        this.shouldUpdateImage = true;
        return this;
    }

    public void drawImageToTexture(Texture texture) {

        // Create new image of argb format
        BufferedImage image = new BufferedImage(dimensions.getWidth(), dimensions.getHeight(), TYPE_INT_ARGB);

        // Create graphics for the image
        Graphics g = image.createGraphics();

        // Set label dimensions
        textPane.setSize(dimensions.getWidth(), dimensions.getHeight());

        // Paint the label using graphics
        textPane.paint(g);

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

class VerticalCenterEditorKit extends StyledEditorKit {

    public ViewFactory getViewFactory() {
        return new StyledViewFactory();
    }

    static class StyledViewFactory implements ViewFactory {

        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new LabelView(elem);
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new ParagraphView(elem);
                } else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return new CenteredBoxView(elem, View.Y_AXIS);
                } else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                } else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }

            return new LabelView(elem);
        }

    }
}

class CenteredBoxView extends BoxView {
    public CenteredBoxView(Element elem, int axis) {

        super(elem,axis);
    }
    protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {

        super.layoutMajorAxis(targetSpan,axis,offsets,spans);
        int textBlockHeight = 0;
        int offset = 0;

        for (int span : spans) {
            textBlockHeight = span;
        }
        offset = (targetSpan - textBlockHeight) / 2;
        for (int i = 0; i < offsets.length; i++) {
            offsets[i] += offset;
        }

    }
}
