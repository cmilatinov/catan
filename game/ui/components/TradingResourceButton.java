package ui.components;

import ui.*;
import ui.constraints.CenterConstraint;
import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;

public class TradingResourceButton extends UIButton {

    UIQuad portrait = new UIQuad();
    private final int offset;

    public TradingResourceButton(int offset)
    {
        this.offset = offset;
        // This UIQuad acts like the border
        this.setColor(new UIColor(0, 0, 0, 1));
        this.setBorderRadius(15);
        UIConstraints constraints = new UIConstraints()
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setHeight(new RelativeConstraint(0.90f))
                .setWidth(new RelativeConstraint(0.90f));
        this.add(portrait, constraints);
    }

    public UIConstraints getUIConstraints()
    {
        return  new UIConstraints()
                .setX(new PixelConstraint(30, UIDimensions.DIRECTION_LEFT))
                .setY(new PixelConstraint(30 + offset, UIDimensions.DIRECTION_TOP))
                .setWidth(new RelativeConstraint(0.10f))
                .setHeight(new RelativeConstraint(0.10f));
    }
}
