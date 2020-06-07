package camera;

import display.Window;
import input.MouseMoveCallback;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MathUtil;

import static org.lwjgl.glfw.GLFW.*;

public class PanCamera extends Camera {

    private final Window window;
    private final int mouseMoveCallback;
    private final int mouseScrollCallback;

    private Vector3f dragStart = null;

    double delta = 0;
    float speed = 10.0f;

    public PanCamera(float fov, Window window) {
        super((float) window.getWidth() / (float) window.getHeight(), fov);
        this.window = window;
        this.mouseMoveCallback = window.mouse().registerMouseMoveCallback(this::onMouseMove);
        this.mouseScrollCallback = window.mouse().registerMouseScrollCallback(this::onMouseScroll);
        window.mouse().setSensitivity(0.2f);
    }

    private void onMouseScroll(double v, double v1) {

    }


    protected void onMouseMove(double x, double y, double dx, double dy) {
        if(dragStart == null && window.mouse().isMouseDown(GLFW_MOUSE_BUTTON_LEFT)) {
            dragStart = window.mouse().getRayAtMouseCoords(this);
            System.out.println("Drag start: " + dragStart);
        }
        if(dragStart != null) {
            Vector3f dragPos = window.mouse().getRayAtMouseCoords(this);
            Vector3f direction = dragPos.sub(dragStart);
            direction.y = 0;
            new Matrix4f().translate(direction)
                    .transformPosition(pos);
        }
        if(window.mouse().isMouseUp(GLFW_MOUSE_BUTTON_LEFT)) {
            dragStart = null;
        }
        if (window.mouse().isMouseDown(GLFW_MOUSE_BUTTON_RIGHT)) {
            rotate(new Vector3f((float) dy, (float) dx, 0));
            rot.x = clamp(rot.x, 10, 60);
        }
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
        pos.lerp(dest, (float)delta);
    }

    @Override
    public void destroy() {
        window.mouse().removeMouseMoveCallback(mouseMoveCallback);
        window.mouse().removeMouseScrollCallback(mouseScrollCallback);
    }
}
