package physics;

import entities.Entity;
import main.Scene;
import org.joml.Vector3f;
import physics.colliders.SphereCollider;


public class PhysicsManager {
    private final Scene scene;

    public PhysicsManager(Scene scene) {
        this.scene = scene;
    }

    public Entity raycastFromCamera() {
        Vector3f ray = scene.getWindow().mouse().getRayAtMouseCoords(scene.getCamera());
        for(Entity entity : scene.getEntities()) {
            if(entity instanceof SphereCollider) {
                SphereCollider collider = (SphereCollider) entity;
                boolean doesIntersect = hitSphere(scene.getCamera().getPosition(), ray, entity.getPosition(), collider.getRadius());
                if(doesIntersect) {
                    return entity;
                }
            }
        }
        return null;
    }

    /**
     * @param origin the origin of the ray
     * @param direction the direction of the ray
     * @param sphereCenter the center vector of the sphere collider
     * @param radius the radius of the sphere collider
     * @return whether the ray intersects the sphere collider
     */
    protected boolean hitSphere(Vector3f origin, Vector3f direction, Vector3f sphereCenter, float radius) {
        Vector3f oc = origin.sub(sphereCenter);
        float a = direction.dot(direction);
        float b = (float) (2.0 * oc.dot(direction));
        float c = oc.dot(oc) - radius*radius;
        float discriminant =  b*b - 4*a*c;
        return discriminant > 0;
    }
}
