package states;

import board.nodes.Side;
import board.nodes.Vertex;
import entities.Entity;
import org.joml.Vector3f;
import scripts.GameManager;

public class StateSetup implements GameState {
    GameManager gameManager;
    int iterations;

    public StateSetup(GameManager gm, int iterations) {
        this.gameManager = gm;
        iterations = iterations;
    }

    private phases currentPhase;

    private enum phases {
        SETTLEMENT,
        ROAD
    }

    @Override
    public void handle() {

    }

    @Override
    public void onClick(Entity clicked) {
        Vector3f result = new Vector3f();
//        if(currentPhase == phases.SETTLEMENT) {
//            if(!(clicked instanceof Vertex))
//                return;
//
//            Vertex clickedVertex = ((Vertex) clicked);
//            if(clickedVertex.getBuilding() == null) {
//                // Checks if the vertex is next to an occupied vertex
//                for(Vertex v : verticesOccupied) {
//                    v.getPosition().sub(clickedVertex.getPosition(), result);
//                    if(Math.abs(result.length() - 1) < 0.01)
//                        break;
//                }
//
//                if(Math.abs(result.length() - 1) > 0.01) {
//                    clickedVertex.settle(currentPlayer);
//                    verticesOccupied.add(clickedVertex);
//                    scene.register(clickedVertex.getBuilding());
//                    currentPhase = phases.ROAD;
//                }
//            }
//        } else if (currentPhase == phases.ROAD) {
//            if (!(clicked instanceof Side))
//                return;
//
//            Side clickedSide = ((Side) clicked);
//
//            if(clickedSide.getRoad() == null && clickedSide.checkOwner(currentPlayer)) {
//                sidesOccupied.add(clickedSide);
//                clickedSide.createRoad(currentPlayer);
//                scene.register(clickedSide.getRoad());
//
//                currentPhase = phases.SETTLEMENT;
//                iterations --;
//            }
//        }

        if(iterations == 0) {
            if(gameManager.isPlayersReversed()) {
                gameManager.setGameState(new StateSettling());
            } else {
                iterations = gameManager.getPlayerCount();
            }

            gameManager.reversePlayers();
        }
    }

    @Override
    public void onSpace() {

    }
}
