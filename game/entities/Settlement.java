package entities;

import objects.TexturedMesh;
import resources.GameResources;
import resources.Resource;

public class Settlement extends EntityStatic {

    private Settlement(TexturedMesh model) {
        super(model);
    }

    public static Settlement create(Resource type)
    {
        var model = new TexturedMesh(GameResources.get(Resource.MESH_SETTLEMENT), GameResources.get(type));
        return new Settlement(model);
    }
}
