package lights;

import org.joml.Vector3f;

public class Light {
	
	private Vector3f color, pos;
	
	public Light(Vector3f color, Vector3f pos) {
		this.color = color;
		this.pos = pos;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public Vector3f getPosition() {
		return pos;
	}

	public void setPosition(Vector3f pos) {
		this.pos = pos;
	}

	
}
