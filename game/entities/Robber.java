package entities;

import objects.TexturedMesh;
import resources.GameResources;
import resources.Resource;

public class Robber extends EntityStatic {

    private Robber(TexturedMesh model) {
        super(model);
    }

    public static Robber create (Resource type)
    {
        var model = new TexturedMesh(GameResources.get(Resource.MESH_ROBBER), GameResources.get(type));
        return new Robber(model);
    }
}
