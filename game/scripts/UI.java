package scripts;

import objects.GameScript;
import ui.*;
import ui.constraints.AspectConstraint;
import ui.constraints.CenterConstraint;
import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;

import java.awt.*;

public class UI extends GameScript {

    public UIQuad box;
    public UIText text;

    @Override
    public void stop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void initialize() {
        box = new UIQuad();
        box.setColor(UIColor.RED);
        UIConstraints constraints = new UIConstraints()
                .setX(new PixelConstraint(20, UIDimensions.DIRECTION_LEFT))
                .setY(new PixelConstraint(20, UIDimensions.DIRECTION_TOP))
                .setWidth(new RelativeConstraint(0.2f))
                .setHeight(new AspectConstraint(1));
        getScene().getUiManager().getContainer().add(box, constraints);

        text = new UIText(new Font("Comic Sans MS", Font.PLAIN, 18), "THIS IS A VERY LONG PARAGRAPH OF TEXT");
        text.setColor(UIColor.BLACK);
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
