package gameplay;

import entities.Entity;
import objects.TexturedMesh;
import resources.GameResources;
import resources.Resource;

import java.util.ArrayList;

public class DevelopmentCards extends Entity {
    private ArrayList<ResourceType> cards = new ArrayList<ResourceType>();

    private final int KNIGHT_CARDS, ROAD_BUILDING, YEAR_OF_PLENTY, MONOPOLY, VICTORY_POINT;

    public DevelopmentCards(TexturedMesh model) {
        this(model, 14, 2, 2, 2, 5);
    }

    @Override
    public boolean shouldRender() {
        return true;
    }

    public DevelopmentCards(TexturedMesh model, int knights, int roadBuilding, int yearOfPlenty, int monopoly, int victoryPoint) {
        super(model);

        KNIGHT_CARDS = knights;
        ROAD_BUILDING = roadBuilding;
        YEAR_OF_PLENTY = yearOfPlenty;
        MONOPOLY = monopoly;
        VICTORY_POINT = victoryPoint;

        addCards(KNIGHT_CARDS, ResourceType.KNIGHT);
        addCards(ROAD_BUILDING, ResourceType.ROAD_BUILDING);
        addCards(YEAR_OF_PLENTY, ResourceType.YEAR_OF_PLENTY);
        addCards(MONOPOLY, ResourceType.MONOPOLY);
        addCards(VICTORY_POINT, ResourceType.VICTORY_POINT);
    }

    public static DevelopmentCards create() {
        return new DevelopmentCards(new TexturedMesh(GameResources.get(Resource.MESH_SETTLEMENT), GameResources.get(Resource.TEXTURE_COLOR_BLUE)));
    }

    public void addCards(int count, ResourceType cardType) {
        for(int i = 0; i < count; i++)
            cards.add(cardType);
    }

    @Override
    public void destroy() {

    }
}
