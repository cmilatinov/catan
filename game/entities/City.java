package entities;

import objects.TexturedMesh;
import resources.GameResources;
import resources.Resource;

public class City extends EntityStatic {

    private City(TexturedMesh model) {
        super(model);
    }

    public static City create(Resource type)
    {
        var model = new TexturedMesh(GameResources.get(Resource.MESH_CITY), GameResources.get(type));
        return new City(model);
    }

    public static City create() {
        var model = new TexturedMesh(GameResources.get(Resource.MESH_CITY), GameResources.get(Resource.TEXTURE_COLOR_BLUE));
        return new City(model);
    }
}
