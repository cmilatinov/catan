package gameplay.tiles;

import entities.Building;
import gameplay.tiles.TileTypes;
import objects.TexturedMesh;
import entities.Entity;

public class Vertex extends Entity{
	private Building building;
	private TileTypes port;
	
	public Vertex(TexturedMesh model) {
		super(model);

		scale(0.996f);
	}

	@Override
	public boolean shouldRender() {
		return false;
	}

	public Building getBuilding() {
		return building;
	}
	
	public void setPort(TileTypes t) {
		port = t;
	}
	
	public void setBuilding(Building building) {
		this.building = building;
		building.setPosition(getPosition());
	}
	
	public void upgradeBuilding() {
		building.upgrade();
	}
	
	public int getBuildingValue() {
		return building.getValue();
	}

	@Override
	public void destroy() {

	}
}