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

    public int getBuildingValue() {
        return switch(getBuilding().getType()) {
            case CITY -> 2;
            case SETTLEMENT -> 1;
            case ROAD -> 0;
        };
    }

    public void upgrade() {
        Building building = Building.create(BuildingType.CITY, getOwner().getColor());
        building.setPosition(getPosition());
        building.scale(0.07f);

        setBuilding(building);
    }

    @Override
    public void settle(Player owner) {
        if(!isNodeFree())
            return;

        setOwner(owner);

        Building building = Building.create(BuildingType.SETTLEMENT, owner.getColor());
        building.setPosition(getPosition());
        building.scale(0.06f);

        setBuilding(building);
    }

    @Override
    public float getRadius() {
        return 0.2f;
    }
}
