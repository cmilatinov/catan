package gameplay;

import java.util.ArrayList;

import gameplay.Board.TileTypes;

public class Tile{
	
	private TileTypes type;
	private ArrayList<Vertex> vertices;
	private int value;
	
	public Tile() {
		type = TileTypes.getRandomType();
		value = (int)(Math.random()*12) + 1; 
		vertices = new ArrayList<Vertex>();
	}
	
	public Tile(TileTypes type) {
		this.type = type;
		this.value = (int)(Math.random()*12) + 1;
		vertices = new ArrayList<Vertex>();
	}
	
	public Tile(TileTypes type, int value) {
		this.type = type;
		this.value = value;
		vertices = new ArrayList<Vertex>();
	}
	
	public int getValue() {
		return value;
	}
	
	public void addVertex(Vertex v) {
		vertices.add(v);
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public TileTypes getType() {
		return this.type;
	}
	
	public void rewardSettlers() {
		vertices.forEach(v -> {
			if(v.getBuilding() != null) {
				v.getBuilding().rewardOwner(type);
			}
		});
	}
	
}
