package gui;

import objects.FreeableObject;
import org.joml.Vector2f;

import objects.GameObject;
import objects.Texture;

public class GUI implements FreeableObject {
	
	private final Texture texture;
	
	private Vector2f pos = new Vector2f();
	private float size;
	
	private int elevation;
	
	private float rot = 0;
	
	public GUI(Texture texture) {
		this.texture = texture;
	}
	
	public GUI setPosition(Vector2f pos) {
		this.pos = pos;
		return this;
	}
	
	public GUI setSize(float size) {
		this.size = size;
		return this;
	}
	
	public GUI setElevation(int elevation) {
		this.elevation = elevation;
		return this;
	}
	
	public GUI setRotation(float rot) {
		this.rot = rot;
		return this;
	}
	
	public GUI rotate(float rot) {
		this.rot += rot;
		return this;
	}
	
	public Vector2f getPosition() {
		return pos;
	}
	
	public float getSize() {
		return size;
	}
	
	public int getElevation() {
		return elevation;
	}
	
	public float getRotation() {
		return rot;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public void destroy() {
		texture.destroy();
	}
	
}
