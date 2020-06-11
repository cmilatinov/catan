package ui.components;

import observers.GameObserver.GameStates;
import ui.*;
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

    public void setCurrentStateName(GameStates event) {
        switch(event) {
            case ROLLING -> this.text.setText("Rolling");
            case STEALING -> this.text.setText("Stealing");
            case SETTING_UP -> this.text.setText("Setting up");
            case SETTLING -> this.text.setText("Settling");
        }
    }
}
