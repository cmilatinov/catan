package ui.animation;

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

    public boolean hasAnimation() {
        return anim != null;
    }

    public void setAnimation(UIAnimation anim) {
        this.anim = anim;
    }

    public void animate(UIAnimationMetrics end, UIInterpolator interpolator, float duration) {
        UIAnimationMetrics start = anim != null ? anim.getCurrentMetrics() : new UIAnimationMetrics(0, 0, 1,0);
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

    public UIAnimationMetrics getCurrentAnimationMetrics() {
        return anim != null ? anim.getCurrentMetrics() : null;
    }

}
