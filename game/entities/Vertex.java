package entities;

import gameplay.TileTypes;
import main.Scene;
import objects.TexturedMesh;
import physics.colliders.SphereCollider;
import resources.Resource;
import entities.Building.BuildingType;

import java.util.ArrayList;

public class Vertex extends EntityToggleable implements SphereCollider {
	private Player owner;
	private TileTypes port;
	private ArrayList<Side> sides;

	private Building building;

	public Vertex(TexturedMesh model) {
		super(model);
	}
	
	public void setPort(TileTypes t) {
		port = t;
	}

	public void addSide(Side s) { sides.add(s); }

	public void settle() {
		building = Building.create(BuildingType.SETTLEMENT, Resource.TEXTURE_COLOR_BLUE);
		building.setPosition(getPosition());
	}

	public void settle(Player owner) {
		this.owner = owner;
		building = Building.create(BuildingType.SETTLEMENT, owner.getColor());
		building.setPosition(getPosition());
	}

	public void upgradeSettlement(Player player) {
		if(owner != player)
			return;

		Resource color = Resource.TEXTURE_COLOR_BLUE;
		if(owner != null)
			color = owner.getColor();
		building = Building.create(BuildingType.CITY, color);
		building.setPosition(getPosition());
	}

	public Building getBuilding(){ return building; }

	@Override
	public void destroy() {

	}

	@Override
	public float getRadius() {
		return 0.2f;
	}
}