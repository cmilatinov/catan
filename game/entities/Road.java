package entities;

import objects.TexturedMesh;
import resources.GameResources;
import resources.Resource;

public class Road extends Entity {

	private Road(TexturedMesh model) {
		super(model);
	}

	public static Road create(Resource type)
	{
		var model = new TexturedMesh(GameResources.get(Resource.MESH_ROAD), GameResources.get(type));
		return new Road(model);
	}

	@Override
	public boolean shouldRender() {
		return true;
	}

	@Override
	public void destroy() {

	}
}
