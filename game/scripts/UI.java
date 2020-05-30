package scripts;

import objects.GameScript;
import ui.*;
import ui.constraints.AspectConstraint;
import ui.constraints.CenterConstraint;
import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;

import java.awt.*;

public class UI extends GameScript {

    UIQuad box;
    UIText text;

    float time = 0;

    @Override
    public void stop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void initialize() {
        box = new UIQuad() {
            @Override
            public void onMouseClick() {
                System.out.println("Clicked!");
            }

            @Override
            public void onMouseHover() {
                this.setColor(UIColor.LIGHT_GRAY);
            }

            @Override
            public void onMouseHoverExit() {
                this.setColor(UIColor.DARK_GRAY);
            }
        };

        box.setColor(UIColor.DARK_GRAY);
        box.setBorderRadius(20);
        UIConstraints constraints = new UIConstraints()
                .setX(new PixelConstraint(20, UIDimensions.DIRECTION_LEFT))
                .setY(new PixelConstraint(20, UIDimensions.DIRECTION_TOP))
                .setWidth(new RelativeConstraint(0.2f))
                .setHeight(new AspectConstraint(1));
        getScene().getUiManager().getContainer().add(box, constraints);

        text = new UIText(new Font("Comic Sans MS", Font.BOLD, 9), "THIS IS A VERY LONG PARAGRAPH OF TEXT");
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
        time = (time + (float)delta) % 2.0f;
        text.setColor(new UIColor(
                0.5f * (float) Math.sin(time * Math.PI) + 0.5f,
                0.5f * (float) Math.cos(time * Math.PI) + 0.5f,
                0.5f * (float) Math.sin((time + 1) * Math.PI) + 0.5f, 1));
    }

    @Override
    public void destroy() {

    }
}
