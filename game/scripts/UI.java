package scripts;

import objects.GameScript;
import ui.*;
import ui.constraints.AspectConstraint;
import ui.constraints.CenterConstraint;
import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;

import java.awt.*;

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
                .setWidth(new RelativeConstraint(0.2f))
                .setHeight(new AspectConstraint(1));
        getScene().getUiManager().getContainer().add(box, constraints);

        UIText text = new UIText(new Font("Verdana", Font.BOLD, 30), "Hello");
        text.setColor(UIColor.CYAN);
        UIConstraints constraints2 = new UIConstraints()
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(new RelativeConstraint(0.6f))
                .setHeight(new RelativeConstraint(0.6f));
        box.add(text, constraints2);
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void destroy() {

    }
}
