package resources;

import main.Engine;
import objects.GameResource;
import objects.GameResourceFactory;
import objects.TexturedMesh;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Defines a resource loader that loads a specific game resources at startup.
 */
public abstract class GameResourceLoader {

    /**
     * The list of resources loaded by this loader instance in a mapped form, where the key is an integer resource ID.
     */
    private static final Map<Integer, GameResource> resources = new HashMap<>();

    /**
     * The callback executed when progress is reported by the resource loader.
     */
    private Consumer<Float> onProgressReceived = null;

    /**
     * An instance referencing the engine calling this loader.
     */
    private final Engine engine;

    /**
     * Constructs a new {@link GameResourceLoader} for the given {@link Engine} instance.
     *
     * @param engine The engine calling this loader.
     */
    public GameResourceLoader(Engine engine) {
        this.engine = engine;
    }

    /**
     * Loads all needed resources. This method should report progress using {@link #reportProgress} when different load stages are completed.
     */
    public abstract void loadResources();

    /**
     * Returns the path to the image to be displayed during the loading process.
     *
     * @return {@link String} The path to the splash screen image.
     */
    public String getSplashImage() {
        return "";
    }

    /**
     * Sets this loader's progress reporting callback.
     *
     * @param onProgressReceived The callback to execute when progress is reported by {@link #loadResources}.
     */
    public void setOnProgressReceived(Consumer<Float> onProgressReceived) {
        this.onProgressReceived = onProgressReceived;
    }

    /**
     * Called by {@link #loadResources} when progress stages are reached, passing the fraction of progress achieved.
     *
     * @param progress The progress achieved ranging from 0.0 (just started) to 1.0 (fully completed).
     *                 Progress fractions outside this range will be rounded to the nearest bound.
     */
    protected void reportProgress(float progress) {
        engine.enqueueLoadOperation(() -> {
            if (this.onProgressReceived != null)
                this.onProgressReceived.accept(progress);
        });
    }

    protected void loadMesh(int id, String filepath) {
        engine.enqueueLoadOperation(() -> resources.put(id, GameResourceFactory.loadOBJ(filepath)));
    }

    protected void loadTexture2D(int id, String filepath, int filtering, boolean mipmap) {
        engine.enqueueLoadOperation(() -> resources.put(id, GameResourceFactory.loadTexture2D(filepath, filtering, mipmap)));
    }

    protected void loadTextureCubeMap(int id, String filepath) {
        engine.enqueueLoadOperation(() -> resources.put(id, GameResourceFactory.loadTextureCubeMap(filepath)));
    }

    protected void loadModel(int id, int mesh, int texture) {
        engine.enqueueLoadOperation(() -> resources.put(id, new TexturedMesh(get(mesh), get(texture))));
    }

    @SuppressWarnings("unchecked")
    public static <T extends GameResource> T get(int id) {
        return (T) resources.get(id);
    }


}
