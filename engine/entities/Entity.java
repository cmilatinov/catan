package entities;

import objects.GameResource;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import objects.TexturedMesh;

/**
 * Represents an in-game entity with a position, rotation, and scale.
 */
public abstract class Entity implements GameResource {

	/**
	 * Rotation, velocity and position of the entity.
	 */
	private Vector3f pos, rot;

	/**
	 * Scale of the entity.
	 */
	private float scale;

	/**
	 * Textured mesh to be rendered.
	 */
	private TexturedMesh model;

	/**
	 * Creates an entity using the specified mesh.
	 * 
	 * @param model The mesh of the entity.
	 */
	public Entity(TexturedMesh model) {
		this.pos = new Vector3f(0);
		this.rot = new Vector3f(0);
		this.scale = 1.0f;
		this.model = model;
	}

	/**
	 * Creates an entity with initial position, rotation, and scale.
	 * 
	 * @param pos   The initial position of the entity.
	 * @param rot   The initial rotation of the entity.
	 * @param scale The initial scale of the entity.
	 * @param model The mesh of the entity.
	 */
	public Entity(Vector3f pos, Vector3f rot, float scale, TexturedMesh model) {
		this.pos = new Vector3f(pos);
		this.rot = new Vector3f(rot);
		this.scale = scale;
		this.model = model;
	}

	/**
	 * Returns the textured mesh of the entity.
	 * 
	 * @return [{@link TexturedMesh}] The entity's textured mesh.
	 */
	public TexturedMesh getModel() {
		return this.model;
	}

	/**
	 * Sets the rotation of the entity to the specified vector.
	 * 
	 * @param rot The Euler rotation to set.
	 * @return [{@link Entity}] This same instance of the class.
	 */
	public Entity setRotation(Vector3f rot) {
		this.rot = new Vector3f(rot);
		return this;
	}

	/**
	 * Sets the position of the entity to the specified vector.
	 * 
	 * @param pos The position to set.
	 * @return [{@link Entity}] This same instance of the class.
	 */
	public Entity setPosition(Vector3f pos) {
		this.pos = new Vector3f(pos);
		return this;
	}

	/**
	 * Sets the scale of the entity to the specified value.
	 * 
	 * @param scale The scale to set the entity.
	 * @return [{@link Entity}] This same instance of the class.
	 */
	public Entity setScale(float scale) {
		this.scale = scale;
		return this;
	}

	/**
	 * Rotates the entity using the specified rotation vector.
	 * 
	 * @param rotation The rotation to be applied on the entity.
	 * @return [{@link Entity}] This same instance of the class.
	 */
	public Entity rotate(Vector3f rotation) {
		rot.add(rotation);
		return this;
	}

	/**
	 * Moves the entity using the specified translation vector.
	 * 
	 * @param translation The translation to be applied on the entity.
	 * @return [{@link Entity}] This same instance of the class.
	 */
	public Entity translate(Vector3f translation) {
		pos.add(translation);
		return this;
	}

	/**
	 * Scales the entity by a percentage of its current size.
	 * 
	 * @param scale The floating point quantity by which to scale the entity.
	 * @return [{@link Entity}] This same instance of the class.
	 */
	public Entity scale(float scale) {
		this.scale *= scale;
		return this;
	}

	/**
	 * A method to get the position of an object as a vector
	 * 
	 * @return [{@link Vector3f}] A vector representing the position of the object
	 */
	public Vector3f getPosition() {
		return pos;
	}

	/**
	 * Returns the x-component value of the entity's position.
	 * 
	 * @return [<b>float</>] The x-value of the entity's position.
	 */
	public float getPositionX() {
		return pos.x;
	}

	/**
	 * Returns the y-component value of the entity's position.
	 * 
	 * @return [<b>float</>] The y-value of the entity's position.
	 */
	public float getPositionY() {
		return pos.y;
	}

	/**
	 * Returns the z-component value of the entity's position.
	 * 
	 * @return [<b>float</>] The z-value of the entity's position.
	 */
	public float getPositionZ() {
		return pos.z;
	}

	/**
	 * Returns the Euler rotation of the entity.
	 * 
	 * @return [{@link Vector3f}] A vector containing the Euler rotation of the
	 *         entity;
	 */
	public Vector3f getRotation() {
		return rot;
	}

	/**
	 * Returns the pitch rotation of the entity.
	 * 
	 * @return [<b>float</b>] This entity's pitch in degrees.
	 */
	public float getPitch() {
		return rot.x;
	}

	/**
	 * Returns the yaw rotation of the entity.
	 * 
	 * @return [<b>float</b>] This entity's yaw in degrees.
	 */
	public float getYaw() {
		return rot.y;
	}

	/**
	 * Returns the roll rotation of the entity.
	 * 
	 * @return [<b>float</b>] This entity's roll in degrees.
	 */
	public float getRoll() {
		return rot.z;
	}

	/**
	 * Returns the entity's model matrix.
	 * 
	 * @return [{@link Matrix4f}] The model matrix of this entity.
	 */
	public Matrix4f getTransform() {

		// Identity matrix
		Matrix4f mat = new Matrix4f();
		mat.identity();

		// Position
		mat.translate(pos);

		// Rotation
		mat.rotate((float) Math.toRadians(rot.x), new Vector3f(1, 0, 0));
		mat.rotate((float) Math.toRadians(rot.y), new Vector3f(0, 1, 0));
		mat.rotate((float) Math.toRadians(rot.z), new Vector3f(0, 0, 1));

		// Scale
		mat.scale(scale);

		// Return result
		return mat;

	}

	public abstract void update(double delta);
	
	public abstract boolean shouldUpdate();
	public abstract boolean shouldRender();

}
