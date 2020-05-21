package entities;

import gameplay.TileTypes;
import log.Logger;
import main.Engine;
import objects.TexturedMesh;
import resources.GameResources;
import resources.Resource;

public class Building extends EntityStatic {

	private enum Type {
		SETTLEMENT,
		CITY;

	}

	private Type type;
	private final Player owner;

	private Building(TexturedMesh model, Player owner) {
		super(model);
		type = Type.SETTLEMENT;
		this.owner = owner;
	}

	public static Building create(Player owner, Resource type) {
		var model = new TexturedMesh(GameResources.get(Resource.MESH_SETTLEMENT), GameResources.get(type));
		return new Building(model, owner);
	}
	
	public void upgrade() {
		if(type == Type.CITY) {
			Engine.LOGGER.log(Logger.ERROR, "Unable to upgrade a city.");
		}
		
		type = Type.CITY;
	}
	
	public int getValue() {
		return type == Type.SETTLEMENT ? 1 : 2;
	}
	
	public Player getOwner() {
		return this.owner;
	}
	
	public void rewardOwner(TileTypes tileType) {
		this.owner.giveCards(tileType, getValue());
	}
	
}
