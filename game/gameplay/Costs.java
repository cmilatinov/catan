package gameplay;

import board.BuildingType;
import entities.Player;

import java.util.HashMap;
import java.util.Map;

public class Costs {
    private final HashMap<ResourceType, Integer> settlementCost = new HashMap<ResourceType, Integer>(){{
        put(ResourceType.BRICK, 1);
        put(ResourceType.FOREST, 1);
        put(ResourceType.WHEAT, 1);
        put(ResourceType.SHEEP, 1);
    }};
    private final HashMap<ResourceType, Integer> cityCost = new HashMap<ResourceType, Integer>(){{
        put(ResourceType.WHEAT, 2);
        put(ResourceType.STONE, 3);
    }};

    private final HashMap<ResourceType, Integer> roadCost = new HashMap<ResourceType, Integer>(){{
        put(ResourceType.BRICK, 1);
        put(ResourceType.FOREST, 1);
    }};

    private static final Costs INSTANCE = new Costs();

    public HashMap<ResourceType, Integer> getBuildingCost(BuildingType type) {
        return switch(type) {
            case SETTLEMENT -> settlementCost;
            case CITY -> cityCost;
            case ROAD -> roadCost;
        };
    }

    public boolean canBuyBuilding(Player player, BuildingType type) {
        for (Map.Entry<ResourceType, Integer> resource : Costs.getInstance().getBuildingCost(type).entrySet())
            if(player.getResourceCards(resource.getKey()) < resource.getValue())
                return false;
        return true;
    }

    public static Costs getInstance() {
        return INSTANCE;
    }
}