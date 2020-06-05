package entities;

import gameplay.Costs;
import gameplay.TileTypes;
import objects.TexturedMesh;
import org.joml.Vector3f;
import physics.colliders.SphereCollider;
import resources.Resource;

import java.util.ArrayList;
import java.util.Map;

public class Side extends EntityToggleable implements SphereCollider {
    private Road road;
    private Player owner;
    private Vector3f rotation;

    private ArrayList<Vertex> vertices;

    public Side(TexturedMesh model) {
        super(model);
        rotation = new Vector3f();
        vertices = new ArrayList<Vertex>();
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

    public boolean canSetRoad(Player owner) {
        for (Map.Entry<TileTypes, Integer> resource : Costs.getInstance().getBuildingCost(Building.BuildingType.ROAD).entrySet())
            if(owner.getResourceCards(resource.getKey()) < resource.getValue())
                return false;

        return checkOwner(owner);
    }

    public void createRoad() {
        road = Road.create(Resource.TEXTURE_COLOR_BLUE);
        road.setPosition(getPosition());
        road.setRotation(getRotation());
        road.scale(0.45f);
    }

    public void addVertex(Vertex v) {
        vertices.add(v);
    }

    public boolean checkOwner(Player currentPlayer) {
        for(Vertex v : vertices){
            if(v.getOwner() == currentPlayer)
                return true;
        }
        return false;
    }

    public ArrayList<Vertex> getVertices(){
        return vertices;
    }

    public Road getRoad() {
        return road;
    }

    @Override
    public void destroy() {

    }

    @Override
    public float getRadius() {
        return 0.5f;
    }
}
