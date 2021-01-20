package ui.animation;

/**
 * This class describes a transformation relative to a {@link ui.UIComponent}'s current position.
 */
public final class UIAnimationMetrics {

    /**
     * The x-component of the two-dimensional translation.
     */
    public final float x;

    /**
     * The y-component of the two-dimensional translation.
     */
    public final float y;

    /**
     * The relative scale of the component, 1.0 being the component's original size.
     */
    public final float scale;

    /**
     * The rotation of the component in degrees.
     */
    public final float rotation;

    /**
     * Constructs a collection of metrics describing a transformation on a {@link ui.UIComponent}.
     *
     * @param x        The x-component of the two-dimensional translation.
     * @param y        The y-component of the two-dimensional translation.
     * @param scale    The relative scale of the component, 1.0 being the component's original size.
     * @param rotation The rotation of the component in degrees.
     */
    public UIAnimationMetrics(float x, float y, float scale, float rotation) {
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.rotation = rotation;
    }

}