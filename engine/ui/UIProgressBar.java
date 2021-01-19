package ui;

import ui.constraints.*;

public class UIProgressBar extends UIQuad {

    private final UIConstraints innerProgressBarConstraints = new UIConstraints()
            .setX(new PixelConstraint(0))
            .setY(new PixelConstraint(0))
            .setWidth(new RelativeConstraint(0.0f))
            .setHeight(new RelativeConstraint(1.0f));

    public UIProgressBar() {
        setColor(UIColor.BLACK);
        setBorderRadius(5);

        UIQuad innerContainer = new UIQuad();
        innerContainer.setColor(UIColor.WHITE);
        UIConstraints innerContainerConstraints = new UIConstraints()
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(new ParentMinusDiffConstraint(6))
                .setHeight(new ParentMinusDiffConstraint(6));
        add(innerContainer, innerContainerConstraints);

        UIConstraints innerProgressConstraints = new UIConstraints()
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(new ParentMinusDiffConstraint(10))
                .setHeight(new ParentMinusDiffConstraint(10));
        UIComponent innerProgress = new UIComponent();
        innerContainer.add(innerProgress, innerProgressConstraints);

        UIQuad innerProgressBar = new UIQuad();
        innerProgressBar.setColor(UIColor.RED);
        innerProgress.add(innerProgressBar, innerProgressBarConstraints);
    }

    @Override
    public void update(double delta) {
        super.update(delta);
    }

    public void setProgress(float progress) {
        innerProgressBarConstraints.setWidth(new RelativeConstraint(progress));
    }

}
