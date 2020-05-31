package entities;

import gameplay.TileTypes;
import objects.TexturedMesh;

import java.util.ArrayList;

public class Vertex extends Entity{
	private Building building;
	private TileTypes port;
	private ArrayList<Side> sides;
	
	public Vertex(TexturedMesh model) {
		super(model);
	}

	@Override
	public boolean shouldRender() {
		return true;
	}

	public Building getBuilding() {
		return building;
	}
	
	public void setPort(TileTypes t) {
		port = t;
	}

	public void addSide(Side s) {
		sides.add(s);
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