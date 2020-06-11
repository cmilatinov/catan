package states;

import board.Node;
import board.nodes.Side;
import board.nodes.Vertex;
import entities.Entity;
import entities.Player;
import org.joml.Vector3f;
import scripts.GameManager;

public class StateSetup implements GameState {
    int iterations;

    public StateSetup(int iterations) {
        currentPhase = phases.SETTLEMENT;
        this.iterations = iterations;
    }

    private phases currentPhase;

    private enum phases {
        SETTLEMENT,
        ROAD
    }

    @Override
    public void onClick(GameManager context, Entity clicked, Player player) {
        if(currentPhase == phases.SETTLEMENT) {
            if(!(clicked instanceof Vertex))
                return;

            Vertex clickedVertex = ((Vertex) clicked);
            if(clickedVertex.isNodeFree() && !clickedVertex.isBuildingNearby()) {
                // Registers the settlement.
                clickedVertex.settle(player);
                context.getScene().register(clickedVertex.getBuilding());

                currentPhase = phases.ROAD;

                if(context.isPlayersReversed()) {
                    context.rewardPlayerOnNode(clickedVertex.getPosition(), player);
                }
            }

        } else if (currentPhase == phases.ROAD) {
            if (!(clicked instanceof Side))
                return;

            Side clickedSide = ((Side) clicked);

            if(clickedSide.isNodeFree() && clickedSide.isAlliedBuildingNearby(player)) {
                clickedSide.settle(player);
                context.getScene().register(clickedSide.getBuilding());

                currentPhase = phases.SETTLEMENT;
                iterations --;

                context.nextTurn();
            }
        }

        if(iterations == 0) {
            if(context.isPlayersReversed()) {
                context.setGameState(new StateRolling());
            } else {
                iterations = context.getPlayerCount();
            }

            context.reversePlayers();
        }
    }

    @Override
    public void onSpace(GameManager context) {

    }
}
