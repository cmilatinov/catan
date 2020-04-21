package gameplay;

import entities.Building;
import gameplay.Board.TileTypes;

public class Vertex {
	private Building building;
	private TileTypes port;
	
	public Vertex() {
		
	}
	
	public Building getBuilding() {
		return building;
	}
	
	public void setPort(TileTypes t) {
		port = t;
	}
	
	public void setBuilding(Building building) {
		this.building = building;
	}
	
	public void upgradeBuilding() {
		building.upgrade();
	}
	
	public int getBuildingValue() {
		return building.getValue();
	}
}