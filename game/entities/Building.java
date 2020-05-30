package entities;

import gameplay.tiles.TileTypes;
import log.Logger;
import main.Engine;
import main.Scene;
import org.joml.Vector3f;

public class Building {

	private enum Type {
		SETTLEMENT,
		CITY;
	}

	private Type type;
	private Player owner;
	private City city;
	private Settlement settlement;

	private Vector3f pos;

	private Scene scene;

	public Building(Scene scene, Player owner) {
		type = Type.SETTLEMENT;

		settlement = Settlement.create();
		city = City.create();

		this.owner = owner;
	}

	public Building(Scene scene) {
		type = Type.SETTLEMENT;

		settlement = Settlement.create();
		city = City.create();

		scene.register(settlement.scale(0.040f));
		this.scene = scene;
	}

	public void setPosition(Vector3f pos) {
		city.setPosition(pos);
		settlement.setPosition(pos);
		this.pos = pos;
	}

	public Vector3f getPosition() {
		return pos;
	}
	
	public void upgrade() {
		if(type == Type.CITY) {
			Engine.LOGGER.log(Logger.ERROR, "Unable to upgrade a city.");
		}

		scene.remove(settlement);
		scene.register(city.scale(0.040f));

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
