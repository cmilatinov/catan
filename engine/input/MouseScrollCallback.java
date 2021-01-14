package input;

public interface MouseScrollCallback {

    /**
     * Invoked whenever a mouse scroll is captured by the window.
     *
     * @param x The scroll offset along the x-axis.
     * @param y The scroll offset along the y-axis.
     */
    void invoke(double x, double y);

}
