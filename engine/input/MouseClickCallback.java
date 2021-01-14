package input;

public interface MouseClickCallback {

    /**
     * Invoked whenever a mouse click is captured by the window.
     *
     * @param button The button that was clicked.
     * @param action The action performed, press or release.
     * @param mods   The modifiers affecting the mouse click.
     */
    public void invoke(int button, int action, int mods);

}
