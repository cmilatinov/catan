package ui.animation;

import scripts.UI;

public class UIAnimation {

    public static final class UIMetrics {

        public final float x, y;
        public final float scale;
        public final float rotation;

        private UIMetrics(float x, float y, float scale, float rotation) {
            this.x = x;
            this.y = y;
            this.scale = scale;
            this.rotation = rotation;
        }

    }

    private final UIMetrics start;
    private final UIMetrics end;

    private UIMetrics current;

    private final UIInterpolator interpolator;

    private final float duration;

    private float animTime = 0;
    private boolean running = false;

    UIAnimation(UIMetrics start, UIMetrics end, UIInterpolator interpolator, float duration) {
        this.start = start;
        this.end = end;
        this.interpolator = interpolator;
        this.duration = duration;
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
    }

    public void update(double delta) {

        if (!running)
            return;

        animTime += delta;
        if (animTime > duration) {
            animTime = duration;
            running = false;
        }

        computeCurrentMetrics();

    }

    private void computeCurrentMetrics() {
        float progress = interpolator.interpolate(animTime / duration);
        float x = start.x + (end.x - start.x) * progress;
        float y = start.y + (end.y - start.y) * progress;
        float scale = start.scale + (end.scale - start.scale) * progress;
        float rotation = start.rotation + (end.rotation - start.rotation) * progress;

        current = new UIMetrics(x, y, scale, rotation);
    }

    public UIMetrics getCurrentMetrics() {
        return current;
    }

    public boolean isRunning() {
        return this.running;
    }
}
