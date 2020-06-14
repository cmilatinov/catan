package gameplay;

import entities.Entity;
import objects.TexturedMesh;
import physics.colliders.SphereCollider;
import resources.GameResources;
import resources.Resource;

import java.util.ArrayList;

public class DevelopmentCards extends Entity implements SphereCollider {
    private ArrayList<ResourceType> cards = new ArrayList<ResourceType>();

    public DevelopmentCards(TexturedMesh model) {
        this(model, 14, 2, 2, 2, 5);
    }

    @Override
    public boolean shouldRender() {
        return true;
    }

    public DevelopmentCards(TexturedMesh model, int knights, int roadBuilding, int yearOfPlenty, int monopoly, int victoryPoint) {
        super(model);

        addCards(knights, ResourceType.KNIGHT);
        addCards(roadBuilding, ResourceType.ROAD_BUILDING);
        addCards(yearOfPlenty, ResourceType.YEAR_OF_PLENTY);
        addCards(monopoly, ResourceType.MONOPOLY);
        addCards(victoryPoint, ResourceType.VICTORY_POINT);
    }

    public static DevelopmentCards create() {
        return new DevelopmentCards(new TexturedMesh(GameResources.get(Resource.MESH_SETTLEMENT), GameResources.get(Resource.TEXTURE_COLOR_BLUE)));
    }

    public void addCards(int count, ResourceType cardType) {
        for(int i = 0; i < count; i++)
            cards.add(cardType);
    }

    public ResourceType drawCard() {
        return cards.get((int)(Math.random() * cards.size()));
    }

    public boolean isEmpty() {
        return cards.size() == 0;
    }

    @Override
    public void destroy() {

    }

    @Override
    public float getRadius() {
        return 0.5f;
    }
}
