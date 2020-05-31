package entities;

import objects.TexturedMesh;
import resources.GameResources;
import resources.Resource;

public class Side extends Entity{
    private Road road;
    private Player owner;

    public Side(TexturedMesh model) {
        super(model);
    }

    public void setRoad(Player owner) {
        road = Road.create(owner.getColor());
        road.setPosition(getPosition());
        road.scale(0.45f);
        this.owner = owner;
    }

    public void createRoad() {
        road = Road.create(Resource.TEXTURE_COLOR_BLUE);
        road.setPosition(getPosition());
        road.scale(0.45f);
    }

    public Road getRoad() {
        return road;
    }

    @Override
    public boolean shouldRender() {
        return true;
    }

    @Override
    public void destroy() {

    }
}
