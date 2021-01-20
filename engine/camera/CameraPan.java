package camera;

import display.Window;
import input.MouseMoveCallback;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class CameraPan extends Camera {

    private final Window window;
    private final int mouseMoveCallback;
    private final int mouseScrollCallback;
    private final int mouseClickCallback;

    private Vector3f dragStart = null, dragEnd = null;

    double delta = 0;
    float speed = 25f;

    public CameraPan(float fov, Window window) {
        super((float) window.getWidth() / (float) window.getHeight(), fov);
        this.window = window;
        this.mouseMoveCallback = window.mouse().registerMouseMoveCallback(this::onMouseMove);
        this.mouseScrollCallback = window.mouse().registerMouseScrollCallback(this::onMouseScroll);
        this.mouseClickCallback = window.mouse().registerMouseClickCallback(this::onMouseClick);
        window.mouse().setSensitivity(0.2f);
    }

    private void onMouseClick(int button, int action, int mods) {
        if(button == GLFW_MOUSE_BUTTON_LEFT)
            if(action == GLFW_PRESS) {
                dragStart = window.mouse().getRayAtMouseCoords(this);
            } else {
                dragStart = null;
            }
    }

    private void onMouseScroll(double x, double y) {
        if(y < 0) {
            pos.add(new Vector3f(0, (float)delta * speed, 0));
        }
        if(y > 0) {
            pos.sub(new Vector3f(0, (float)delta * speed, 0));
        }
    }

    protected void onMouseMove(double x, double y, double dx, double dy) {

        if(dragStart != null) {
            dragEnd = new Vector3f((float)dx, 0, (float)dy);
        }

        if (window.mouse().isMouseDown(GLFW_MOUSE_BUTTON_RIGHT)) {
            rotate(new Vector3f((float) dy, (float) dx, 0));
            rot.x = clamp(rot.x, 10, 60);
        }
    }

    public boolean isDragging()
    {
        return dragStart != null;
    }

    private float clamp(float value, int a, int b)
    {
        return Math.max(a,Math.min(b,value));
    }

    @Override
    public void update(double delta) {
        this.delta = delta;

        var dest = new Vector3f(pos);
        if (window.keyboard().isKeyDown(GLFW_KEY_W)) {
            var up = forward();
            up.y = 0;
            up.normalize().mul(speed);
            dest.add(up);
        }

        if (window.keyboard().isKeyDown(GLFW_KEY_S)) {
            var down = forward();
            down.y = 0;
            down.normalize().mul(speed);
            dest.sub(down);
        }

        if (window.keyboard().isKeyDown(GLFW_KEY_A)) {
            dest.sub(right().normalize().mul(speed));
        }

        if (window.keyboard().isKeyDown(GLFW_KEY_D)) {
            dest.add(right().normalize().mul(speed));
        }

        if (isDragging() && dragEnd != null) {
            Vector3f result = dragEnd.sub(dragStart).mul(-3);
            result.y = 0;

            dragEnd = null;

            dest.add(result);
        }
        pos.lerp(dest, (float)delta);
    }

    @Override
    public void destroy() {
        window.mouse().removeMouseMoveCallback(mouseMoveCallback);
        window.mouse().removeMouseScrollCallback(mouseScrollCallback);
        window.mouse().removeMouseClickCallback(mouseClickCallback);
    }
}
