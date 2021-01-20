package ui.components;

import ui.UIButton;
import ui.UIColor;
import ui.UIConstraints;
import ui.UIDimensions;
import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;

public class TradeButton extends UIButton {
    private Runnable onClickRunnable;

    public TradeButton() {
        setColor(UIColor.WHITE);
        setText("Start Trade");
    }

    public UIConstraints getConstraints() {
        // Setup constraints for the Load Game Button
        return new UIConstraints()
                .setX(new PixelConstraint(30, UIDimensions.DIRECTION_LEFT))
                .setY(new PixelConstraint(30, UIDimensions.DIRECTION_BOTTOM))
                .setWidth(new RelativeConstraint(0.1f))
                .setHeight(new RelativeConstraint(0.1f));
    }

    @Override
    public void onMouseExit() {
        this.setColor(UIColor.WHITE);
    }

    @Override
    public void onMouseEnter() {
        this.setColor(UIColor.LIGHT_GRAY);
    }

    @Override
    public void onMouseClick() {
        if(onClickRunnable != null)
            onClickRunnable.run();
    }

    public void setOnMouseClickEvent(Runnable action) {
        onClickRunnable = action;
    }
}
