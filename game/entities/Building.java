package entities;

import gameplay.TileTypes;
import objects.TexturedMesh;
import resources.GameResources;
import resources.Resource;

import java.util.HashMap;

public class Building extends EntityStatic{

	public enum BuildingType{
		CITY,
		SETTLEMENT,
		ROAD
	}

	private Building(TexturedMesh model) {
		super(model);
		scale(0.05f);
	}

	public static Building create(BuildingType type, Resource color) {
		var model = new TexturedMesh(GameResources.get(Resource.getBuildingMesh(type)), GameResources.get(color));
		return new Building(model);
	}

	@Override
	public boolean shouldRender() {
		return true;
	}

	@Override
	public void destroy() {

	}
}
