package entities;

import gameplay.Costs;
import gameplay.TileTypes;
import objects.TexturedMesh;
import physics.colliders.SphereCollider;
import resources.Resource;
import entities.Building.BuildingType;

import java.util.ArrayList;
import java.util.Map;

public class Vertex extends EntityToggleable implements SphereCollider {
	private Player owner;
	private TileTypes port;
	private ArrayList<Side> sides;

	private Building building;
	private BuildingType buildingType;

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

	public Player getOwner() {
		return owner;
	}

	public boolean canUpgrade(Player player) {
		if(owner != player)
			return false;

		for (Map.Entry<TileTypes, Integer> resource : Costs.getInstance().getBuildingCost(BuildingType.CITY).entrySet())
			if(owner.getResourceCards(resource.getKey()) < resource.getValue())
				return false;

		return true;
	}

	public boolean canSettle(Player owner) {
		for (Map.Entry<TileTypes, Integer> resource : Costs.getInstance().getBuildingCost(BuildingType.SETTLEMENT).entrySet())
			if(owner.getResourceCards(resource.getKey()) < resource.getValue())
				return false;

		return true;
	}

	public void settle(Player owner) {
		this.owner = owner;

		buildingType = BuildingType.SETTLEMENT;
		building = Building.create(BuildingType.SETTLEMENT, owner.getColor());
		building.setPosition(getPosition());
	}

	public void upgradeSettlement(Player player) {
		Resource color = Resource.TEXTURE_COLOR_BLUE;
		if(owner != null)
			color = owner.getColor();

		buildingType = BuildingType.CITY;
		building = Building.create(BuildingType.CITY, color);
		building.setPosition(getPosition());
	}

	public Building getBuilding(){ return building; }

	@Override
	public void destroy() {

	}

	public int getBuildingValue() {
		return switch (buildingType) {
			case SETTLEMENT -> 1;
			case CITY -> 2;
			default -> 0;
		};
	}

	@Override
	public float getRadius() {
		return 0.2f;
	}
}