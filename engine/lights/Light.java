package lights;

import org.joml.Vector3f;

/**
 * Represents an omnidirectional light with a certain color and position.
 */
public class Light {

	/**
	 * The light's color in RGB format.
	 */
	private Vector3f color;

	/**
	 * The light's position in three-dimensional space.
	 */
	private Vector3f pos;

	/**
	 * Creates a new light with the given parameters.
	 * @param color The color of the light.
	 * @param pos The position of the light.
	 */
	public Light(Vector3f color, Vector3f pos) {
		this.color = color;
		this.pos = pos;
	}

	/**
	 * Return this light's color.
	 * @return {@link Vector3f} The light's color in RGB format where values range from 0.0 to 1.0.
	 */
	public Vector3f getColor() {
		return color;
	}

	/**
	 * Sets this light's color.
	 * @param color The new color in RGB format where values range from 0.0 to 1.0.
	 * @return {@link Light} This same instance of the class.
	 */
	public Light setColor(Vector3f color) {
		this.color = color;
		return this;
	}

	/**
	 * Return this light's position.
	 * @return {@link Vector3f} The light's position in three-dimensional space where the zero vector is the origin.
	 */
	public Vector3f getPosition() {
		return pos;
	}

	/**
	 * Set this light's position.
	 * @param pos The new position in three-dimensional space where the zero vector is the origin.
	 * @return {@link Light} This same instance of the class.
	 */
	public Light setPosition(Vector3f pos) {
		this.pos = pos;
		return this;
	}

}
