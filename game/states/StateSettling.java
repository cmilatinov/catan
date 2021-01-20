package states;

import entities.board.Piece;
import entities.board.nodes.Side;
import entities.board.nodes.Vertex;
import entities.Entity;
import entities.Player;
import observers.GameObserver.GameStates;
import scripts.GameManager;

public class StateSettling implements GameState {

    @Override
    public void onClick(GameManager context, Entity clicked, Player player) {
        if(clicked instanceof Vertex) {
            Vertex clickedVertex = ((Vertex) clicked);

            if(clickedVertex.isEmpty() &&
               !clickedVertex.isPieceNearby() &&
               clickedVertex.isRoadNearby(player) &&
               player.canAfford(Piece.getPrice(Piece.SETTLEMENT)))
            {
                player.purchasePiece(Piece.getPrice(Piece.SETTLEMENT));
                // Registers the settlement.
                clickedVertex.settle(player);
                context.getScene().register(clickedVertex.getPiece());
            } else if (!clickedVertex.isEmpty() &&
                       clickedVertex.getOwner() == player &&
                       player.canAfford(Piece.getPrice(Piece.CITY)))
            {
                player.purchasePiece(Piece.getPrice(Piece.CITY));
                // First clear the old building from the scene and then register the city.
                context.getScene().remove(clickedVertex.getPiece());
                clickedVertex.settle(player);
                context.getScene().register(clickedVertex.getPiece());
            }
        } else if (clicked instanceof Side) {
            Side clickedSide = ((Side) clicked);
            if(clickedSide.isEmpty() &&
               (clickedSide.isAlliedBuildingNearby(player) || clickedSide.isAlliedRoadNearby(player)) &&
                player.canAfford(Piece.getPrice(Piece.ROAD))) {
                // Removes cards from player inventory
                player.purchasePiece(Piece.getPrice(Piece.ROAD));

                clickedSide.settle(player);
                context.getScene().register(clickedSide.getPiece());
            }
        }
    }

    @Override
    public void onSpace(GameManager context) {
        context.nextTurn();
        context.setGameState(new StateRolling());
    }

    @Override
    public GameStates getStateName() {
        return GameStates.SETTLING;
    }
}
