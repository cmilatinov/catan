package display;

public interface DisplayModeChangedCallback {

    /**
     * Invoked whenever a frame's display mode is changed.
     *
     * @param newMode The frame's new display mode.
     */
    void invoke(DisplayMode newMode);

}
