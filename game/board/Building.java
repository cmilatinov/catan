package board;

import entities.EntityStatic;
import objects.TexturedMesh;
import resources.GameResources;
import resources.Resource;

public class Building extends EntityStatic {

	private BuildingType type;

	private Building(TexturedMesh model) {
		super(model);
	}

	public static Building create(BuildingType type, Resource color) {
		Building building = switch (type) {
			case CITY -> new Building(new TexturedMesh(GameResources.get(Resource.MESH_CITY), GameResources.get(color)));
			case ROAD -> new Building(new TexturedMesh(GameResources.get(Resource.MESH_ROAD), GameResources.get(color)));
			case SETTLEMENT -> new Building(new TexturedMesh(GameResources.get(Resource.MESH_SETTLEMENT), GameResources.get(color)));
		};
		building.setType(type);

		return building;
	}

	public void setType(BuildingType type) {
		this.type = type;
	}

	public BuildingType getType() {
		return type;
	}

	@Override
	public boolean shouldRender() {
		return true;
	}

	@Override
	public void destroy() {

	}
}
