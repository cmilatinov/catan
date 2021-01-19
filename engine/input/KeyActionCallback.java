package input;

public interface KeyActionCallback {

    /**
     * Invoked whenever an action is performed as the result of a key press or release.
     *
     * @param modifiers The modifiers affecting the key action in the form of bit flags.
     *                  (ex: {@link org.lwjgl.glfw.GLFW#GLFW_MOD_CONTROL},
     *                  {@link org.lwjgl.glfw.GLFW#GLFW_MOD_ALT},
     *                  {@link org.lwjgl.glfw.GLFW#GLFW_MOD_SHIFT})
     */
    public void invoke(int modifiers);

}
