package resources;

import objects.*;

public class ProxyGameResource implements GameResource {

    private final GameResourceFactory loader;
    private final ResourceType type;
    private final Object[] params;

    @Override
    public void destroy() {

    }

    public enum ResourceType {
        MESH,
        TEXTURE_2D,
        TEXTURE_CUBE_MAP,
        MODEL
    }

    public ProxyGameResource(GameResourceFactory loader, ResourceType type, Object ...params)
    {
        this.loader = loader;
        this.type = type;
        this.params = params;
    }

    public GameResource resolve()
    {
        switch (type) {
            case MESH -> {
                return loader.loadOBJ((String)params[0]);
            }
            case TEXTURE_2D -> {
                return loader.loadTexture2D((String)params[0], (Integer)params[1], (Boolean)params[2]);
            }
            case TEXTURE_CUBE_MAP -> {
                return loader.loadTextureCubeMap((String)params[0]);
            }
            case MODEL -> {
                var mesh = (Mesh)GameResources.get((Resource)params[0]);
                var texture = (Texture)GameResources.get((Resource)params[1]);
                return new TexturedMesh(mesh, texture);
            }
        }
        return null;
    }
}
