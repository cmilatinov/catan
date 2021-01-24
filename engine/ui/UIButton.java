package ui;

import ui.animation.UIAnimationMetrics;
import ui.animation.UIInterpolators;
import ui.constraints.CenterConstraint;
import ui.constraints.ParentMinusDiffConstraint;

import java.awt.*;

public class UIButton extends UIQuad {

    private Runnable onClick;

    private final UIText textComponent = new UIText(new Font("Arial", Font.BOLD, 35), "")
            .setColor(UIColor.WHITE);

    private final UIQuad innerBox = new UIQuad();
    private final UIConstraints innerBoxConstraints = new UIConstraints()
            .setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setWidth(new ParentMinusDiffConstraint(4))
            .setHeight(new ParentMinusDiffConstraint(4));

    public UIButton(String text) {
        super.setColor(new UIColor(0.3f, 0.3f, 0.3f, 1.0f));
        setBorderRadius(5);
        setBorderWidth(2);
        add(innerBox, innerBoxConstraints);

        innerBox.setColor(new UIColor(0.6f, 0.6f, 0.6f, 1.0f));
        innerBox.add(textComponent, null);
        innerBox.setInteractive(false);

        textComponent.setColor(UIColor.WHITE);
        textComponent.setText(text);

    }

    public void onMouseEnter() {
        this.animator().animate(new UIAnimationMetrics(40, 0, 1.2f, 0.0f), UIInterpolators.EASE_IN_OUT, 0.2f);
        setColor(new UIColor(0.5f, 0.5f, 0.5f, 1.0f));
    }

    public void onMouseExit() {
        this.animator().animate(new UIAnimationMetrics(0, 0, 1.0f, 0.0f), UIInterpolators.EASE_IN_OUT, 0.2f);
        setColor(new UIColor(0.6f, 0.6f, 0.6f, 1.0f));
    }

    public void onMouseClick() {
        if (onClick != null)
            onClick.run();
    }

    public UIButton setColor(UIColor color) {
        innerBox.setColor(color);
        return this;
    }

    public UIButton setBorderColor(UIColor color) {
        super.setColor(color);
        return this;
    }

    public UIButton setBorderWidth(int width) {
        innerBoxConstraints
                .setWidth(new ParentMinusDiffConstraint(width * 2))
                .setHeight(new ParentMinusDiffConstraint(width * 2));
        innerBox.setBorderRadius(Math.max(getBorderRadius() - width, 0));
        return this;
    }

    public UIButton setText(String text) {
        textComponent.setText(text);
        return this;
    }

    public UIButton setFont(Font font) {
        textComponent.setFont(font);
        return this;
    }

    public UIButton setTextColor(UIColor color) {
        textComponent.setColor(color);
        return this;
    }

    public UIButton setOnClick(Runnable onClick) {
        this.onClick = onClick;
        return this;
    }

}