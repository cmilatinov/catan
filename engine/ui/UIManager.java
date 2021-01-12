package ui;

import display.Window;
import ui.constraints.PixelConstraint;

import java.awt.*;
import java.util.Iterator;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class UIManager {

    private final UIText framerate;

    private final UIComponent root;

    private final UIRenderer renderer;

    private final Window window;

    private int mouseMoveHandle;

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
        this.renderer = new UIRenderer(window);
        this.window = window;

        // FPS
        framerate = new UIText(new Font("Arial", Font.BOLD, 30), "FPS");
        framerate.setColor(UIColor.GREEN);
        UIConstraints fpsConstraints = new UIConstraints()
                .setX(new PixelConstraint(4, UIDimensions.DIRECTION_LEFT))
                .setY(new PixelConstraint(4, UIDimensions.DIRECTION_BOTTOM))
                .setWidth(new PixelConstraint(100))
                .setHeight(new PixelConstraint(30));
        root.add(framerate, fpsConstraints);
    }

    public boolean onMouseClick(int button, int action, int mods) {
        var mouseCords = window.mouse().getMousePosition();
        UIComponent uiComponent = findUIComponentFromCoords(mouseCords.first, mouseCords.second);
        if (uiComponent == null)
            return false;

        if (action == GLFW_PRESS)
            uiComponent.onMouseClick();

        return true;
    }

    private void onMouseMove(double x, double y, double dx, double dy) {
        UIComponent uiComponent = findUIComponentFromCoords((int) x, (int) y);
        if (lastHoveredComponent != null && lastHoveredComponent != uiComponent) {
            lastHoveredComponent.onMouseHoverExit();
        }
        if (uiComponent != null) {
            uiComponent.onMouseHover();
            lastHoveredComponent = uiComponent;
        }
    }

    public UIComponent findUIComponentFromCoords(int x, int y) {
        UIComponent found = null;
        Iterator<UIComponent> iterator = root.children.iterator();
        while (iterator.hasNext()) {
            UIComponent uiComponent = iterator.next();

            if (!uiComponent.isInteractable()) {
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

    public UIComponent getContainer() {
        return root;
    }

    public void update(double delta) {
        time += delta;
        fps++;
        if (time > 1.0f) {
            framerate.setText(Integer.toString(fps));
            time = fps = 0;
        }
        root.update(delta);
    }

    public void render() {
        renderer.render(root);
    }

    public void cleanup() {
        window.mouse().removeMouseMoveCallback(mouseMoveHandle);
    }
}


