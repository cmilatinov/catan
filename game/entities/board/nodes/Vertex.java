package entities.board.nodes;

import entities.Player;
import entities.board.Piece;
import objects.TexturedMesh;
import resources.GameResources;
import resources.Resource;

public class Vertex extends Node{
    public Vertex() {
        super(GameResources.get(Resource.MODEL_TILE_FOREST), Node.VERTEX);
    }

    @Override
    public void settle(Player owner) {
        Piece piece = null;
        float scale = 0.0f;

        if(isEmpty()) {
            setOwner(owner);
            piece = Piece.create(Piece.SETTLEMENT, owner.getColor());
            scale = 0.06f;
        } else if(getOwner().getID() == owner.getID()) {
            piece = Piece.create(Piece.CITY, getOwner().getColor());
            scale = 0.07f;
        }

        if(piece != null) {
            piece.setPosition(getPosition());
            piece.scale(scale);
            setPiece(piece);
        }
    }

    /**
     * Gets piece value
     * @return 1 if Settlement 2 otherwise
     */
    public int getPieceValue() {
        return Piece.getPieceValue(getPiece().type);
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

    /**
     * Method to determine whether or not the vertices nearby are occupied.
     * @return True if there is a building on a nearby vertex.
     */
    public boolean isPieceNearby() {
        for(Node node : getNearbyNodes())
            for(Node deepNode : node.getNearbyNodes())
                if(null != deepNode.getPiece())
                    return true;

        return false;
    }

    @Override
    public void destroy() {

    }

    @Override
    public float getRadius() {
        return 0.2f;
    }
}
