package scripts;

import objects.GameScript;
import ui.UIColor;
import ui.UIConstraints;
import ui.UIDimensions;
import ui.UIQuad;
import ui.constraints.AspectConstraint;
import ui.constraints.CenterConstraint;
import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;

public class UI extends GameScript {

    @Override
    public void stop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void initialize() {
        UIQuad box = new UIQuad();
        box.setColor(UIColor.DARK_GRAY);
        box.setBorderRadius(20);
        UIConstraints constraints = new UIConstraints()
                .setX(new PixelConstraint(20, UIDimensions.DIRECTION_LEFT))
                .setY(new PixelConstraint(20, UIDimensions.DIRECTION_TOP))
                .setWidth(new RelativeConstraint(0.15f))
                .setHeight(new AspectConstraint(1));
        getScene().getUiManager().getContainer().add(box, constraints);

        UIQuad box2 = new UIQuad();
        box2.setColor(UIColor.RED);
        UIConstraints constraints2 = new UIConstraints()
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(new PixelConstraint(30))
                .setHeight(new PixelConstraint(30));
        box.add(box2, constraints2);
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void destroy() {

    }
}
