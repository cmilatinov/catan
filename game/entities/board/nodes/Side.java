package entities.board.nodes;

import entities.Player;
import entities.board.Piece;
import objects.TexturedMesh;
import resources.GameResources;
import resources.Resource;

public class Side extends Node{

    public Side() {
        super(GameResources.get(Resource.MODEL_TILE_BRICK), Node.SIDE);
    }

    @Override
    public void settle(Player owner) {
        if(isEmpty()) {
            setOwner(owner);

            Piece piece = Piece.create(Piece.ROAD, owner.getColor());
            piece.setPosition(getPosition());
            piece.setRotation(getRotation());
            piece.scale(0.45f);

            setPiece(piece);
        }
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

    /**
     * Method to determine whether or not the vertices nearby are occupied by the player that wants to place a road down.
     * @return True if there is a building on a nearby vertex owned by the player that wants to place a road down.
     */
    public boolean isAlliedBuildingNearby(Player player) {
        for(Node node : getNearbyNodes()) {
            if(node.getOwner()== player)
                return true;
        }

        return false;
    }

    @Override
    public void destroy() {

    }

    @Override
    public float getRadius() {
        return 0.3f;
    }
}
