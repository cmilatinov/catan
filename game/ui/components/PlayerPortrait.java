package ui.components;

import resources.Resource;
import ui.UIColor;
import ui.UIConstraints;
import ui.UIDimensions;
import ui.UIQuad;
import ui.constraints.CenterConstraint;
import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;

import static resources.Resource.*;

public class PlayerPortrait extends UIQuad {

    UIQuad portrait = new UIQuad();
    UIColor transparent = new UIColor(0, 0, 0, 0);

    private final int offset;

    public PlayerPortrait(int offset)
    {
        this.offset = offset;
        // This UIQuad acts like the border
        this.setColor(transparent);
        this.setBorderRadius(15);
        UIConstraints constraints = new UIConstraints()
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setHeight(new RelativeConstraint(0.90f))
                .setWidth(new RelativeConstraint(0.90f));
        this.add(portrait, constraints);
    }

    public enum PortraitColor {
        BLUE,
        RED,
        GREEN,
        PURPLE,
        ORANGE,
        UNKNOWN
    }

    public UIConstraints getUIConstraints()
    {
        return  new UIConstraints()
                .setX(new PixelConstraint(30 + offset, UIDimensions.DIRECTION_LEFT))
                .setY(new PixelConstraint(30, UIDimensions.DIRECTION_TOP))
                .setWidth(new RelativeConstraint(0.10f))
                .setHeight(new RelativeConstraint(0.10f));
    }

    public void setActiveBorder(boolean status)
    {
        if (status) { // Show border
            this.setColor(UIColor.BLACK);
        } else {
            this.setColor(transparent);
        }
    }

    public void setColorFromResource(Resource resource)
    {
        PortraitColor color = colorFromResource(resource);
        var uiColor = switch (color) {
            case BLUE -> UIColor.BLUE;
            case RED -> UIColor.RED;
            case GREEN -> UIColor.GREEN;
            case PURPLE -> UIColor.PURPLE;
            case ORANGE -> UIColor.ORANGE;
            case UNKNOWN -> UIColor.BLACK;
        };

        portrait.setColor(uiColor);
    }

    public PortraitColor colorFromResource(Resource resource)
    {
        return switch (resource) {
            case TEXTURE_COLOR_BLUE -> PortraitColor.BLUE;
            case TEXTURE_COLOR_RED -> PortraitColor.RED;
            case TEXTURE_COLOR_GREEN -> PortraitColor.GREEN;
            case TEXTURE_COLOR_PURPLE -> PortraitColor.PURPLE;
            case TEXTURE_COLOR_ORANGE -> PortraitColor.ORANGE;
            default -> PortraitColor.UNKNOWN;
        };
    }
}
