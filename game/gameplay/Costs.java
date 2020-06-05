package gameplay;

import entities.Building;

import java.util.HashMap;

public class Costs {
    private final HashMap<TileTypes, Integer> settlementCost = new HashMap<TileTypes, Integer>(){{
        put(TileTypes.BRICK, 1);
        put(TileTypes.FOREST, 1);
        put(TileTypes.WHEAT, 1);
        put(TileTypes.SHEEP, 1);
    }};
    private final HashMap<TileTypes, Integer> cityCost = new HashMap<TileTypes, Integer>(){{
        put(TileTypes.WHEAT, 2);
        put(TileTypes.STONE, 3);
    }};

    private final HashMap<TileTypes, Integer> roadCost = new HashMap<TileTypes, Integer>(){{
        put(TileTypes.BRICK, 1);
        put(TileTypes.FOREST, 1);
    }};

    private static final Costs INSTANCE = new Costs();

    public HashMap<TileTypes, Integer> getBuildingCost(Building.BuildingType type) {
        return switch(type) {
            case SETTLEMENT -> settlementCost;
            case CITY -> cityCost;
            case ROAD -> roadCost;
        };
    }

    public static Costs getInstance() {
        return INSTANCE;
    }
}