package gameplay;

import java.util.ArrayList;

import entities.Entity;
import objects.TexturedMesh;
import org.joml.Vector3f;

public class Tile extends Entity{
	
	private float[] hexagonalCoords;
	
	private final TileTypes type;
	private final ArrayList<Vertex> vertices;
	private int value;
	
	public Tile(TexturedMesh model, TileTypes type) {
		super(model);
		
		this.type = type;
		this.value = (int)(Math.random()*12) + 1;
		vertices = new ArrayList<Vertex>();
	}
	
	public void setHexagonalCoords(float x, float y, float z) {
		hexagonalCoords = new float[] {x, y, z};
		calculatePixelCoords();
	}
	
	public void getHexagonalCoords() {
		System.out.println(hexagonalCoords[0] + " " + hexagonalCoords[1] + "  " + hexagonalCoords[2]);
	}
	
	private void calculatePixelCoords() {
		float y = (3 * hexagonalCoords[2]) / 2;
		float x = (float) (Math.sqrt(3)* (hexagonalCoords[2]/2 + hexagonalCoords[0]));

		setPosition(new Vector3f(x, 0, y));
	}

	private void calculateVerticesCoords() {

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

	public void update(double delta) {
		
	}

	public boolean shouldUpdate() {
		return false;
	}

	public boolean shouldRender() {
		return true;
	}

	@Override
	public void destroy() {
		this.getModel().destroy();
	}
}
