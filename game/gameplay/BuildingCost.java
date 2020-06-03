package gameplay;

import entities.Building;

import java.util.HashMap;

public final class BuildingCost {
    private HashMap<TileTypes, Integer> settlementCost = new HashMap<TileTypes, Integer>();
    private HashMap<TileTypes, Integer> cityCost = new HashMap<TileTypes, Integer>();

    private static final BuildingCost INSTANCE = new BuildingCost();

    public BuildingCost() {
        settlementCost.put(TileTypes.BRICK, 1);
        settlementCost.put(TileTypes.FOREST, 1);
        settlementCost.put(TileTypes.WHEAT, 1);
        settlementCost.put(TileTypes.SHEEP, 1);

        cityCost.put(TileTypes.WHEAT, 2);
        cityCost.put(TileTypes.STONE, 3);
    }

    public HashMap<TileTypes, Integer> getBuildingCost(Building.BuildingType type) {
        return switch(type){
            case SETTLEMENT -> settlementCost;
            case CITY -> cityCost;
            default -> null;
        };
    }

    public static BuildingCost getInstance() {
        return INSTANCE;
    }
}
