package ui.animation;

/**
 * Specifies an interpolation function governing the speed of an animation.
 */
public interface UIInterpolator {

    /**
     * This method determines the speed of the animation through time.
     *
     * @param progress The time progression of the animation as a ratio value between 0.0 and 1.0
     *                 where 0.0 indicates that the animation just started as opposed to 1.0 that
     *                 indicates the animation is finished.
     * @return <b>float</b> The associated movement progression value. This return value describes
     * the amount of movement that should be executed when the animation has progressed to the given
     * time.
     */
    public float interpolate(float progress);

}
