package ui.animation;

/**
 * Controls a component's current animation.
 */
public class UIAnimator {

    /**
     * The animation currently being executed.
     */
    private UIAnimation anim = null;

    /**
     * This method is called once every frame to update the animation time
     * based on its speed.
     *
     * @param delta The time passed in seconds since the last rendered frame.
     */
    public void update(double delta) {
        if (anim != null)
            anim.update(delta);
    }

    /**
     * Indicates whether or not this animator needs to updated in the next frame.
     *
     * @return True if the animator needs to be updated on the next frame, false otherwise.
     */
    public boolean shouldUpdate() {
        if (anim == null)
            return false;
        return anim.isRunning();
    }

    /**
     * Indicates whether this animator is currently executing an animation.
     * Note that even if the animation is paused, this method will still return true.
     *
     * @return True if an animation is bound to this animator, false otherwise.
     */
    public boolean hasAnimation() {
        return anim != null;
    }

    /**
     * Begins an animation to a target state.
     *
     * @param target       The target metrics at which the animation is to end. This effectively describes
     *                     the final position the component is to achieve at the end of the animation.
     * @param interpolator The interpolation method to use for the animation. This parameter describes
     *                     the speed of the animation over time. Custom interpolators may be used.
     *                     A collection of commonly used interpolators can be found in the
     *                     {@link UIInterpolators} class.
     * @param duration     The duration of the animation in seconds.
     */
    public void animate(UIAnimationMetrics target, UIInterpolator interpolator, float duration) {
        UIAnimationMetrics start = anim != null ? anim.getCurrentMetrics() : new UIAnimationMetrics(0, 0, 1, 0);
        anim = new UIAnimation(start, target, interpolator, duration);
        anim.start();
    }

    /**
     * Starts the playback of the underlying animation. This method has no effect if no animation has
     * been assigned to this animator.
     */
    public void start() {
        if (anim != null)
            anim.start();
    }

    /**
     * Stops the playback of the underlying animation. This method has no effect if no animation has
     * been assigned to this animator.
     */
    public void stop() {
        if (anim != null)
            anim.stop();
    }

    /**
     * Returns the current interpolated animation metrics for the underlying animation.
     *
     * @return {@link UIAnimationMetrics} The metrics describing the current interpolated transformation
     * of the animation or null if no animation has been bound to this animator.
     */
    public UIAnimationMetrics getCurrentAnimationMetrics() {
        return anim != null ? anim.getCurrentMetrics() : null;
    }

}
