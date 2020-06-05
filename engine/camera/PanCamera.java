package camera;

import display.Window;
import input.MouseMoveCallback;
import org.joml.Math;
import org.joml.Vector3f;
import org.lwjgl.system.MathUtil;

import static org.lwjgl.glfw.GLFW.*;

public class PanCamera extends Camera {

    private final Window window;
    private final int mouseMoveCallback;

    double delta = 0;
    float speed = 10.0f;

    public PanCamera(float fov, Window window) {
        super((float) window.getWidth() / (float) window.getHeight(), fov);
        this.window = window;
        this.mouseMoveCallback = window.mouse().registerMouseMoveCallback(onMouseMove());
        window.mouse().setSensitivity(0.2f);
    }

    public MouseMoveCallback onMouseMove() {
        return (double x, double y, double dx, double dy) -> {
            if(window.mouse().isMouseDown(GLFW_MOUSE_BUTTON_LEFT)) {

            }
            if (window.mouse().isMouseDown(GLFW_MOUSE_BUTTON_RIGHT)) {
                rotate(new Vector3f((float) dy, (float) dx, 0));
                rot.x = clamp(rot.x, 20, 60);
            }
        };
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
    }
}
