package ui;

import display.Window;
import objects.FreeableObject;
import org.joml.Vector2i;
import ui.constraints.PixelConstraint;

import java.awt.*;
import java.util.Iterator;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class UIManager implements FreeableObject {

    private final UIText framerate;

    private final UIComponent root;

    private final Window window;

    private final int mouseMoveHandle;

    UIComponent lastHoveredComponent = null;

    private float time = 0;
    private int fps = 0;

    public UIManager(Window window) {
        this.mouseMoveHandle = window.mouse().registerMouseMoveCallback(this::onMouseMove);
        this.root = new UIComponent(new UIDimensions()
                .setX(0)
                .setY(0)
                .setWidth(window.getWidth())
                .setHeight(window.getHeight())
                .setElevation(0))
                .setVisible(false);
        this.window = window;

        // FPS
        framerate = new UIText(new Font("Arial", Font.BOLD, 30), "");
        framerate.setColor(UIColor.GREEN);
        UIConstraints fpsConstraints = new UIConstraints()
                .setX(new PixelConstraint(4, UIDimensions.DIRECTION_RIGHT))
                .setY(new PixelConstraint(4, UIDimensions.DIRECTION_BOTTOM))
                .setWidth(new PixelConstraint(100))
                .setHeight(new PixelConstraint(30));
        root.add(framerate, fpsConstraints);
    }

    public boolean onMouseClick(int button, int action, int mods) {
        Vector2i mouseCoords = window.mouse().getMousePosition();
        UIComponent uiComponent = findUIComponentFromCoords(mouseCoords.x, mouseCoords.y);
        if (uiComponent == null)
            return false;

        if (action == GLFW_PRESS)
            uiComponent.onMouseClick();

        return true;
    }

    /**
     * This method is used as a {@link input.MouseMoveCallback} on the attached window.
     *
     * @param x  The new x-coordinate of the mouse cursor.
     * @param y  The new y-coordinate of the mouse cursor.
     * @param dx The difference in horizontal movement since the last call.
     * @param dy The difference in vertical movement since the last call.
     */
    private void onMouseMove(double x, double y, double dx, double dy) {
        UIComponent uiComponent = findUIComponentFromCoords((int) x, (int) y);
        if (lastHoveredComponent != null && lastHoveredComponent != uiComponent) {
            lastHoveredComponent.onMouseExit();
        }
        if (uiComponent != null) {
            uiComponent.onMouseEnter();
            lastHoveredComponent = uiComponent;
        }
    }

    /**
     * Returns the top-most UI component whose bounding box encompasses the given coordinates.
     * Non-interactive components are ignored.
     *
     * @param x The x-coordinate relative to the window.
     * @param y The y-coordinate relative to the window.
     * @return {@link UIComponent} The component at the specified coordinates or null if none is found.
     */
    private UIComponent findUIComponentFromCoords(int x, int y) {
        UIComponent found = null;
        Iterator<UIComponent> iterator = root.children.iterator();
        while (iterator.hasNext()) {
            UIComponent uiComponent = iterator.next();

            if (!uiComponent.isInteractive()) {
                continue;
            }

            int topY = uiComponent.getDimensions().getTopY();
            int topX = uiComponent.getDimensions().getLeftX();

            int bottomY = uiComponent.getDimensions().getBottomY();
            int bottomX = uiComponent.getDimensions().getRightX();
            if (topX < x && topY < y && bottomX > x && bottomY > y) {
                if (found == null
                        || (found.getDimensions().getElevation() < uiComponent.getDimensions().getElevation()
                        || found.getDimensions().getElevationInParent() < uiComponent.getDimensions().getElevationInParent())
                ) {
                    found = uiComponent;
                    iterator = found.children.iterator();
                }
            }
        }
        return found;
    }

    /**
     * Returns the root component of the interface. Its size is the same as the window bound to this manager.
     *
     * @return {@link UIComponent} The interface container component.
     */
    public UIComponent getContainer() {
        return root;
    }

    /**
     * Update method used to run UI computation logic every frame.
     *
     * @param delta The amount of time that has passed since the last rendered frame in seconds.
     */
    public void update(double delta) {
        time += delta;
        fps++;
        if (time > 1.0f) {
            framerate.setText(Integer.toString(fps));
            time = fps = 0;
        }
        root.update(delta);
    }

    /**
     * {@inheritDoc}
     */
    public void destroy() {
        window.mouse().removeMouseMoveCallback(mouseMoveHandle);
        root.children.clear();
    }

}


