package ui.components;

import resources.Resource;
import ui.*;
import ui.constraints.CenterConstraint;
import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;

public class TradeMenu extends UIQuad {

    UIColor transparent = new UIColor(0, 0, 0,0);
    UIColor background =  new UIColor(0.77f, 0.65f, 0.4f, 1);

    public TradeMenu()
    {
        // This UIQuad acts like the border
        this.setColor(transparent);
        this.setBorderRadius(15);
    }

    public UIConstraints getConstraints() {
        return new UIConstraints()
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setHeight(new RelativeConstraint(0.60f))
                .setWidth(new RelativeConstraint(0.20f));
    }

    public void setActiveBorder(boolean status)
    {
        if (status) { // Show border
            this.setColor(background);
        } else {
            this.setColor(transparent);
        }
    }
}
