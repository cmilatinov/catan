package gameplay;

import java.util.HashMap;

public final class RoadCost {
    private final HashMap<TileTypes, Integer> cost = new HashMap<TileTypes, Integer>();

    private static final RoadCost INSTANCE = new RoadCost();

    public RoadCost() {
        cost.put(TileTypes.BRICK, 1);
        cost.put(TileTypes.FOREST, 1);
    }

    public HashMap<TileTypes, Integer> getCost() {
        return cost;
    }

    public static RoadCost getInstance() {
        return INSTANCE;
    }
}
