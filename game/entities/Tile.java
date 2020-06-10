package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import board.Node;
import gameplay.ResourceType;
import objects.TexturedMesh;
import org.joml.Vector2f;
import org.joml.Vector3f;
import physics.colliders.SphereCollider;
import resources.GameResources;
import resources.Resource;

public class Tile extends Entity implements SphereCollider {
	private Vector2f hexCoords;
	private EntityStatic token;

	private ResourceType type;
	private ArrayList<Node> vertices;
	private int value = -1;

	private boolean embargoed;
	
	public Tile(TexturedMesh model, ResourceType type) {
		super(model);
		
		this.type = type;
		vertices = new ArrayList<Node>();
		embargoed = false;
	}

	public void setHexCoords(Vector2f coords) {
		hexCoords = coords;
		calculatePixelCoords();
	}
	
	private void calculatePixelCoords() {
		float z = (3 * hexCoords.y) / 2;
		float x = (float) (Math.sqrt(3)* (hexCoords.y/2 + hexCoords.x));

		setPosition(new Vector3f(x, 0, z));

		if(type != ResourceType.DESERT) {
			token.setPosition(getPosition());
			token.translate(new Vector3f(0, 0.1f, 0));
			token.scale(0.2f);
		}
	}

	public List<Node> getOccupiedVertices() {
		return vertices.stream().filter(v -> v.getBuilding() != null).collect(Collectors.toList());
	}

	public void addVertex(Node v) {
		vertices.add(v);
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
		token = new EntityStatic(GameResources.get(Resource.getToken(value)));
	}

	public Entity getToken() {
		return token;
	}
	
	public ResourceType getType() {
		return this.type;
	}

	public boolean shouldRender() {
		return true;
	}

	public boolean isEmbargoed() {return embargoed;}

	public void setEmbargoed(boolean embargoed) {
		this.embargoed = embargoed;
	}

	@Override
	public void destroy() {
		this.getModel().destroy();
	}

	@Override
	public float getRadius() {
		return 0.5f;
	}
}
