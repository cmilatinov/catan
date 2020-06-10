package board.nodes;

import board.Building;
import board.BuildingType;
import board.Node;
import entities.Player;
import objects.TexturedMesh;

public class Vertex extends Node {
    /**
     * Constructor to create a new node of type vertex.
     * @param model - TexturedMesh you wish to use for the vertex.
     */
    public Vertex(TexturedMesh model) {
        super(model);

        scale(0.3f);
    }

    /**
     * Method to determine whether or not the vertices nearby are occupied.
     * @return True if there is a building on a nearby vertex.
     */
    public boolean isBuildingNearby() {
        for(Node node : getNearbyNodes())
            for(Node deepNode : node.getNearbyNodes())
                if(null != deepNode.getBuilding())
                    return true;

        return false;
    }

    /**
     * Method to determine whether or not there is a road nearby owned by the player that wants to settle.
     * @param player - Player object of the user wanting to settle.
     * @return True if there's a road nearby owned by the player that wants to settle.
     */
    public boolean isRoadNearby(Player player) {
        for(Node node : getNearbyNodes())
            if(node.getOwner() == player)
                return true;
        return false;
    }

    @Override
    public void settle(Player owner) {
        setOwner(owner);

        Building building;
        if(isNodeEmpty()) {
            building = Building.create(BuildingType.SETTLEMENT, owner.getColor());
        } else {
            building = Building.create(BuildingType.CITY, owner.getColor());
        }

        building.setPosition(getPosition());
        building.scale(0.45f);

        setBuilding(building);
    }

    @Override
    public float getRadius() {
        return 0.2f;
    }
}
