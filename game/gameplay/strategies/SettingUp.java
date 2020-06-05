package gameplay.strategies;

import entities.Entity;
import entities.Player;
import entities.Side;
import entities.Vertex;
import main.Scene;
import org.joml.Vector3f;
import turnbased.GameStrategy;

import java.util.ArrayList;

public class SettingUp implements GameStrategy {
    private final ArrayList<Vertex> verticesOccupied;
    private final ArrayList<Side> sidesOccupied;

    private Scene scene;

    private boolean turnDone;

    private enum phases {
        SETTLEMENT,
        ROAD
    }

    private phases currentPhase;

    public SettingUp(ArrayList<Vertex> verticesOccupied, ArrayList<Side> sidesOccupied, Scene scene) {
        this.verticesOccupied = verticesOccupied;
        this.sidesOccupied = sidesOccupied;

        this.scene = scene;

        currentPhase = phases.SETTLEMENT;

        resetTurn();
    }

    public void resetTurn() {
        turnDone = false;
    }

    @Override
    public boolean isTurnDone() {
        boolean state = turnDone;
        resetTurn();
        return state;
    }

    @Override
    public void onClick(Entity clicked, Player currentPlayer) {
        Vector3f result = new Vector3f();
        if(currentPhase == phases.SETTLEMENT) {
            if(!(clicked instanceof Vertex))
                return;

            Vertex clickedVertex = ((Vertex) clicked);
            if(clickedVertex.getBuilding() == null) {
                // Checks if the vertex is next to an occupied vertex
                for(Vertex v : verticesOccupied) {
                    v.getPosition().sub(clickedVertex.getPosition(), result);
                    if(Math.abs(result.length() - 1) < 0.01)
                        break;
                }

                if(Math.abs(result.length() - 1) > 0.01) {
                    clickedVertex.settle(currentPlayer);
                    verticesOccupied.add(clickedVertex);
                    scene.register(clickedVertex.getBuilding());
                    currentPhase = phases.ROAD;
                }
            }
        } else if (currentPhase == phases.ROAD) {
            if (!(clicked instanceof Side))
                return;

            Side clickedSide = ((Side) clicked);

            if(clickedSide.getRoad() == null && clickedSide.checkOwner(currentPlayer)) {
                sidesOccupied.add(clickedSide);
                clickedSide.createRoad(currentPlayer);
                scene.register(clickedSide.getRoad());

                currentPhase = phases.SETTLEMENT;
                turnDone = true;
            }

        }
    }
}
