package ui.components;

import scripts.GameManager;
import ui.*;
import ui.constraints.AspectConstraint;
import ui.constraints.CenterConstraint;
import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;

import java.awt.*;

public class GamePhase extends UIQuad {

    UIText text = new UIText(new Font("Arial", Font.PLAIN, 26), "");

    public GamePhase() {
        text.setColor(UIColor.WHITE);
        UIConstraints constraints =
                new UIConstraints()
                        .setX(new CenterConstraint())
                        .setY(new CenterConstraint())
                        .setWidth(new RelativeConstraint(1f))
                        .setHeight(new RelativeConstraint(1f));
        this.add(text, constraints);
    }

    public UIConstraints getConstraints()
    {
        return new UIConstraints()
                .setX(new PixelConstraint(30, UIDimensions.DIRECTION_LEFT))
                .setY(new PixelConstraint(300, UIDimensions.DIRECTION_TOP))
                .setWidth(new RelativeConstraint(0.15f))
                .setHeight(new RelativeConstraint(0.1f));
    }

    public void setCurrentStateName() {
        this.text.setText("Rolling");
    }
}
