package physics;

import entities.Entity;
import main.Scene;
import org.joml.Vector3f;
import physics.colliders.SphereCollider;

/**
 * Performs physics calculations for a given scene.
 */
public class PhysicsManager {

    /**
     * The scene to which this physics manager is bound.
     */
    private final Scene scene;

    /**
     * Creates a new physics manager bound to the given scene.
     *
     * @param scene The scene for which to compute various physics.
     */
    public PhysicsManager(Scene scene) {
        this.scene = scene;
    }

    /**
     * Cast a ray originating from the coordinates of the mouse on the frame and determine if it collides with an entity in the scene.
     *
     * @return An entity hit by the raycast, null if not found.
     */
    public Entity raycastFromCamera() {
        Vector3f ray = scene.getWindow().mouse().getRayAtMouseCoords(scene.getCamera());
        for (Entity entity : scene.getEntityList()) {
            if (entity instanceof SphereCollider) {
                SphereCollider collider = (SphereCollider) entity;
                boolean doesIntersect = hitSphere(scene.getCamera().getPosition(), ray, entity.getPosition(), collider.getRadius());
                if (doesIntersect) {
                    return entity;
                }
            }
        }
        return null;
    }

    /**
     * Determines if a ray intersects with a given sphere.
     *
     * @param origin       The point of origin of the ray.
     * @param direction    A vector describing the direction of the ray.
     * @param sphereCenter The center point of the sphere.
     * @param radius       The radius of the sphere.
     * @return True if the ray intersects the sphere, false otherwise.
     */
    private boolean hitSphere(Vector3f origin, Vector3f direction, Vector3f sphereCenter, float radius) {
        Vector3f oc = origin.sub(sphereCenter);
        float a = direction.dot(direction);
        float b = (float) (2.0 * oc.dot(direction));
        float c = oc.dot(oc) - radius * radius;
        float discriminant = b * b - 4 * a * c;
        return discriminant > 0;
    }

}
