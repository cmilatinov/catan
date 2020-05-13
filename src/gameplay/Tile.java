package gameplay;

import java.util.ArrayList;

import entities.Entity;
import objects.TexturedMesh;

public class Tile extends Entity{
	
	private double[] hexagonalCoords;
	private double[] pixelCoords;
	
	private TileTypes type;
	private ArrayList<Vertex> vertices;
	private int value;
	
	public Tile(TexturedMesh model, TileTypes type) {
		super(model);
		
		this.type = type;
		this.value = (int)(Math.random()*12) + 1;
		vertices = new ArrayList<Vertex>();
	}
	
	public void setHexagonalCoords(double x, double y, double z) {
		hexagonalCoords = new double[] {x, y, z};
		
		calculatePixelCoords();
	}
	
	public void getHexagonalCoords() {
		System.out.println(hexagonalCoords[0] + " " + hexagonalCoords[1] + "  " + hexagonalCoords[2]);
	}
	
	public void getPixelCoords() {
		System.out.println(pixelCoords[0] + " " + pixelCoords[1]);
	}
	
	private void calculatePixelCoords() {
		double y = (3 * hexagonalCoords[2]) / 2;
		double x = (Math.sqrt(3)* (hexagonalCoords[2]/2 + hexagonalCoords[0]));
		
		pixelCoords = new double[] {x, y};
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
