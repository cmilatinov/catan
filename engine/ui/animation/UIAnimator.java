package ui.animation;

import static ui.animation.UIAnimation.UIMetrics;

public class UIAnimator {

    private UIAnimation anim = null;

    public void update(double delta) {
        if (anim != null)
            anim.update(delta);
    }

    public boolean shouldUpdate() {
        if (anim == null)
            return false;
        return anim.isRunning();
    }

    public void setAnimation(UIAnimation anim) {
        this.anim = anim;
    }

    public void animate(UIMetrics end, UIInterpolator interpolator, float duration) {
        UIMetrics start = anim.getCurrentMetrics();
        anim = new UIAnimation(start, end, interpolator, duration);
        anim.start();
    }

    public void play() {
        if (anim != null)
            anim.start();
    }

    public void stop() {
        if (anim != null)
            anim.start();
    }

    public UIMetrics getCurrentAnimationMetrics() {
        return anim != null ? anim.getCurrentMetrics() : null;
    }

}
