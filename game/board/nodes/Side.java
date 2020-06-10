package board.nodes;

import board.Building;
import board.BuildingType;
import board.Node;
import entities.Player;
import objects.TexturedMesh;

public class Side extends Node {
    /**
     * Constructor to create a new node of type side.
     * @param model - TexturedMesh you wish to use for the side.
     */
    public Side(TexturedMesh model) {
        super(model);

        scale(0.1f);
    }

    /**
     * Method to determine whether or not the vertices nearby are occupied by the player that wants to place a road down.
     * @return True if there is a building on a nearby vertex owned by the player that wants to place a road down.
     */
    public boolean isAlliedBuildingNearby(Player player) {
        for(Node node : getNearbyNodes())
            if(node.getOwner() == player)
                return true;
        return false;
    }

    /**
     * Method to determine whether or not there is an allied road nearby.
     * @param player - Player object for the user wanting to put down a road.
     * @return True if there is an allied road nearby.
     */
    public boolean isAlliedRoadNearby(Player player) {
        for(Node node : getNearbyNodes())
            for(Node deepNode : node.getNearbyNodes())
                if(deepNode.getOwner() == player)
                    return true;

        return false;
    }

    @Override
    public void settle(Player owner) {
        if(!isNodeEmpty())
            return;

        setOwner(owner);

        Building building = Building.create(BuildingType.ROAD, owner.getColor());
        building.setPosition(getPosition());
        building.setRotation(getRotation());
        building.scale(0.45f);

        setBuilding(building);
    }

    @Override
    public float getRadius() {
        return 0.3f;
    }
}
