package gameplay.strategies;

import entities.Entity;
import entities.Player;
import entities.Side;
import entities.Vertex;
import main.Scene;
import org.joml.Vector3f;
import turnbased.GameStrategy;

import java.util.ArrayList;

public class Settling implements GameStrategy {

    private final ArrayList<Vertex> verticesOccupied;
    private final ArrayList<Side> sidesOccupied;

    private Scene scene;

    public Settling(ArrayList<Vertex> verticesOccupied, ArrayList<Side> sidesOccupied, Scene scene) {
        this.verticesOccupied = verticesOccupied;
        this.sidesOccupied = sidesOccupied;

        this.scene = scene;
    }

    @Override
    public boolean isTurnDone() {
        return false;
    }

    @Override
    public void onClick(Entity clicked, Player currentPlayer) {
        Vector3f result = new Vector3f();
        if(clicked instanceof Vertex) {

            Vertex clickedVertex = ((Vertex) clicked);
            if(clickedVertex.getBuilding() == null && clickedVertex.canSettle(currentPlayer)) {
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
                }
            }
        } else if ((clicked instanceof Side)) {

            Side clickedSide = ((Side) clicked);

            if(clickedSide.getRoad() == null && clickedSide.canSetRoad(currentPlayer)) {
                sidesOccupied.add(clickedSide);
                clickedSide.createRoad(currentPlayer);
                scene.register(clickedSide.getRoad());
            }

        }
    }
}
