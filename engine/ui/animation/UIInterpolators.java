package ui.animation;

import static java.lang.Math.cos;
import static java.lang.Math.PI;

/**
 * A collection of commonly used interpolators when creating animations.
 */
public class UIInterpolators {

    /**
     * Animation speed remains constant through time.
     */
    public static final UIInterpolator LINEAR = x -> x;

    /**
     * Animation speed starts slow and increases over time. Speed is highest at the very end of the animation.
     * Opposite of {@link #EASE_OUT}.
     */
    public static final UIInterpolator EASE_IN = x -> x * x;

    /**
     * Animation speed starts fast and decreases over time. Speed is highest at the very start of the animation.
     * Opposite of {@link #EASE_IN}.
     */
    public static final UIInterpolator EASE_OUT = x -> 1 - (x - 1) * (x - 1);

    /**
     * Animation speed starts slow, increases until the animation is halfway done at which point the animation speed is fastest.
     * Then, the speed slowly decreases until the end of the animation.
     */
    public static final UIInterpolator EASE_IN_OUT = x -> (1 - (float)cos(PI * x)) / 2;

}
