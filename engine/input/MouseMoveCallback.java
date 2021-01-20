package input;

public interface MouseMoveCallback {

    /**
     * Invoked whenever a mouse movement is captured by the window.
     *
     * @param x  The x-position of the mouse relative to the window.
     * @param y  The y-position of the mouse relative to the window.
     * @param dx The difference in horizontal movement since the last movement.
     * @param dy The difference in vertical movement since the last movement.
     */
    public void invoke(double x, double y, double dx, double dy);

}
