package input;

public interface KeyCallback {

    /**
     * Invoked whenever an action is performed as the result of a key press or release.
     *
     * @param modifiers The modifiers affecting the key action in the form of bit flags. (ex: CTRL, ALT, SHIFT)
     */
    public void invoke(int modifiers);

}
