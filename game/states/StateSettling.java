package states;

import board.BuildingType;
import board.nodes.Side;
import board.nodes.Vertex;
import entities.Entity;
import entities.Player;
import gameplay.Costs;
import gameplay.DevelopmentCards;
import observers.GameObserver.GameStates;
import scripts.GameManager;

public class StateSettling implements GameState {

    @Override
    public void onClick(GameManager context, Entity clicked, Player player) {
        if(clicked instanceof Vertex) {
            Vertex clickedVertex = ((Vertex) clicked);

            if(clickedVertex.isNodeFree() &&
               !clickedVertex.isBuildingNearby() &&
               clickedVertex.isRoadNearby(player) &&
               Costs.getInstance().canBuyBuilding(BuildingType.SETTLEMENT, player))
            {
                // Removes cards from player inventory
                Costs.getInstance().purchaseBuilding(BuildingType.SETTLEMENT, player);
                // Registers the settlement.
                clickedVertex.settle(player);
                context.getScene().register(clickedVertex.getBuilding());
            } else if (!clickedVertex.isNodeFree() &&
                       clickedVertex.getOwner() == player &&
                       Costs.getInstance().canBuyBuilding(BuildingType.CITY, player))
            {
                // Removes cards from player inventory
                Costs.getInstance().purchaseBuilding(BuildingType.CITY, player);

                // First clear the old building from the scene and then register the city.
                context.getScene().remove(clickedVertex.getBuilding());
                clickedVertex.upgrade();
                context.getScene().register(clickedVertex.getBuilding());
            }
        } else if (clicked instanceof Side) {
            Side clickedSide = ((Side) clicked);
            if(clickedSide.isNodeFree() &&
               (clickedSide.isAlliedBuildingNearby(player) || clickedSide.isAlliedRoadNearby(player)) &&
               Costs.getInstance().canBuyBuilding(BuildingType.ROAD, player)) {
                // Removes cards from player inventory
                Costs.getInstance().purchaseBuilding(BuildingType.ROAD, player);

                clickedSide.settle(player);
                context.getScene().register(clickedSide.getBuilding());
            }
        } else if (clicked instanceof DevelopmentCards) {
            DevelopmentCards deck = ((DevelopmentCards) clicked);

            if(!deck.isEmpty()) {
                player.addCards(deck.drawCard(), 1);
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
