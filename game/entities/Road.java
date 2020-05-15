package entities;

import objects.TexturedMesh;
import org.joml.Vector3f;
import resources.GameResources;
import resources.Resource;

public class Road extends Entity {

	private Road(TexturedMesh model) {
		super(model);
	}

	public Road(Vector3f pos, Vector3f rot, float scale, TexturedMesh model) {
		super(pos, rot, scale, model);
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
