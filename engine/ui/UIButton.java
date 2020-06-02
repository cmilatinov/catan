package ui;

import ui.constraints.CenterConstraint;
import ui.constraints.RelativeConstraint;

import java.awt.*;

public class UIButton extends UIQuad {
    UIText text = new UIText(new Font("Arial", Font.PLAIN, 18), "");

    public UIButton(){
        text.setColor(UIColor.BLACK);
        UIConstraints constraints = new UIConstraints()
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(new RelativeConstraint(0.6f))
                .setHeight(new RelativeConstraint(0.6f));
        this.add(text, constraints);
    }

    public UIText setText(String text) {
        return this.text.setText(text);
    }

    public UIText setFont(Font font) {
        return this.text.setFont(font);
    }

    public UIText setTextColor(UIColor color) {
        return this.text.setColor(color);
    }
}
