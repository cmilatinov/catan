package entities;

import objects.TexturedMesh;
import org.joml.Vector3f;
import physics.colliders.SphereCollider;
import resources.GameResources;
import resources.Resource;

public class Side extends EntityToggleable implements SphereCollider {
    private Road road;
    private Player owner;
    private Vector3f rotation;

    public Side(TexturedMesh model) {
        super(model);
        rotation = new Vector3f();
    }

    public void createRoad(Player owner) {
        if(road != null)
            return;

        this.owner = owner;
        road = Road.create(owner.getColor());
        road.setPosition(getPosition());
        road.setRotation(getRotation());
        road.scale(0.45f);
        this.owner = owner;
    }

    public void createRoad() {
        road = Road.create(Resource.TEXTURE_COLOR_BLUE);
        road.setPosition(getPosition());
        road.setRotation(getRotation());
        road.scale(0.45f);
    }

    public Road getRoad() {
        return road;
    }

    @Override
    public void destroy() {

    }

    @Override
    public float getRadius() {
        return 0.2f;
    }
}
