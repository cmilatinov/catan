package camera;

import objects.GameResource;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Represents a camera perspective, later rendered to the screen or to a buffer.
 */
@SuppressWarnings("unused")
public abstract class Camera implements GameResource {

    /**
     * This camera object's aspect ratio. Usually 16:9, varies with resolution.
     */
    private float aspect;

    /**
     * This camera's vertical and horizontal FOV values. Vertical FOV is calculated from horizontal FOV.
     */
    private float fovx, fovy;

    /**
     * The position and rotation of this camera in three-dimensional space. The zero vector represents the origin and no rotation.
     */
    protected Vector3f pos, rot;

    /**
     * This camera's projection matrix used to define how it flattens three-dimensional onto a two-dimensional surface.
     */
    private Matrix4f proj;

    /**
     * This camera's view matrix used to move the camera in space by moving the visible objects around it instead.
     */
    private Matrix4f view;

    /**
     * Creates a new camera with the given aspect ratio
     *
     * @param aspect The aspect ratio of the camera.
     * @param fov    The horizontal FOV of the camera in degrees.
     */
    public Camera(float aspect, float fov) {
        this.aspect = aspect;
        this.fovx = fov;
        this.pos = new Vector3f();
        this.rot = new Vector3f();
        updateProjectionMatrix();
        updateViewMatrix();
    }

    /**
     * Updates the camera's projection matrix based on FOV and aspect ratio values.
     */
    private void updateProjectionMatrix() {
        fovy = (float) (2 * Math.atan(Math.tan(Math.toRadians(fovx) / 2) / aspect));
        proj = new Matrix4f().perspective(fovy, aspect, 0.1f, 1000f);
        fovy = (float) Math.toDegrees(fovy);
    }

    /**
     * Updates the camera's view matrix based on transformation values.
     */
    private void updateViewMatrix() {
        view = new Matrix4f();

        view.rotate((float) (rot.x * Math.PI / 180), new Vector3f(1, 0, 0));
        view.rotate((float) (rot.y * Math.PI / 180), new Vector3f(0, 1, 0));
        view.rotate((float) (rot.z * Math.PI / 180), new Vector3f(0, 0, 1));

        Vector3f invPos = new Vector3f();
        pos.mul(-1.0f, invPos);
        view.translate(invPos);
    }

    /**
     * Sets this camera's aspect ratio.
     *
     * @param aspect The new aspect ratio.
     * @return {@link Camera} This same instance of the class.
     */
    public Camera setAspect(float aspect) {
        this.aspect = aspect;
        updateProjectionMatrix();
        return this;
    }

    /**
     * Sets the camera's horizontal FOV in degrees.
     *
     * @param fov The new FOV in degrees.
     * @return {@link Camera} This same instance of the class.
     */
    public Camera setFOV(float fov) {
        this.fovx = fov;
        updateProjectionMatrix();
        return this;
    }

    /**
     * Sets the position of the camera to the specified vector.
     *
     * @param pos The new position to set.
     * @return {@link Camera} This same instance of the class.
     */
    public Camera setPosition(Vector3f pos) {
        if (pos != null)
            this.pos = pos;
        return this;
    }

    /**
     * Sets the rotation of the camera to the specified vector.
     *
     * @param rot The new Euler rotation to set.
     * @return {@link Camera} This same instance of the class.
     */
    public Camera setRotation(Vector3f rot) {
        if (rot != null)
            this.rot = rot;
        return this;
    }

    /**
     * Moves the camera using the specified translation vector.
     *
     * @param translation The translation to be applied on the entity.
     * @return {@link Camera} This same instance of the class.
     */
    public Camera translate(Vector3f translation) {
        if (pos != null)
            pos.add(translation);
        return this;
    }

    /**
     * Rotates the camera using the specified rotation vector.
     *
     * @param rotation The rotation to be applied to the camera.
     * @return {@link Camera} This same instance of the class.
     */
    public Camera rotate(Vector3f rotation) {
        if (rotation != null)
            rot.add(rotation);
        return this;
    }

    /**
     * Returns the position vector of this camera.
     *
     * @return {@link Vector3f} A vector representing the position of the camera.
     */
    public Vector3f getPosition() {
        return new Vector3f(pos);
    }

    /**
     * Returns the Euler rotation of this camera.
     *
     * @return {@link Vector3f} A vector containing the Euler rotation of the camera.
     */
    public Vector3f getRotation() {
        return new Vector3f(rot);
    }

    /**
     * Returns the horizontal FOV of this camera.
     *
     * @return <b>float</b> The horizontal field of view of this camera, in degrees.
     */
    public float getFovX() {
        return fovx;
    }

    /**
     * Returns the vertical FOV of this camera.
     *
     * @return <b>float</b> The vertical field of view of this camera, in degrees.
     */
    public float getFovY() {
        return fovy;
    }

    /**
     * Returns this camera's aspect ratio.
     *
     * @return <b>float</b> The aspect ratio of the camera.
     */
    public float getAspect() {
        return aspect;
    }

    /**
     * Rotates the camera such that it faces the position specified.
     *
     * @param pos The point the camera is to look at.
     * @return {@link Camera} This same instance of the class.
     */
    public Camera lookAt(Vector3f pos) {
        Vector3f dir = new Vector3f();
        pos.sub(this.pos, dir);

        float pitch = (float) (Math.atan(dir.y / new Vector2f(dir.x, dir.z).length()) * 180 / Math.PI);
        float yaw = dir.z < 0 ?
                (float) (Math.atan(dir.x / dir.z) * 180 / Math.PI) :
                (float) (Math.atan(dir.x / dir.z) * 180 / Math.PI + 180);

        rot = new Vector3f(-pitch, -yaw, 0);

        return this;
    }

    /**
     * Resets the camera's rotation, and translation vectors to zero vectors.
     *
     * @return {@link Camera} This same instance of the class.
     */
    public Camera reset() {
        this.rot = new Vector3f();
        this.pos = new Vector3f();
        return this;
    }

    /**
     * Returns the forward facing vector based on the camera's current rotation.
     *
     * @return {@link Vector3f} A vector designating the forward direction of the camera.
     */
    public Vector3f forward() {
        Matrix4f transform = new Matrix4f();

        transform.rotate((float) ((rot.y - 90) * Math.PI / 180), new Vector3f(0, 1, 0));
        transform.rotate((float) (-rot.x * Math.PI / 180), new Vector3f(0, 0, 1));

        Vector4f vec = transform.transform(new Vector4f(1, 0, 0, 1));

        return new Vector3f(vec.x / vec.w, vec.y / vec.w, -vec.z / vec.w);
    }

    /**
     * Returns the right facing vector based on the camera's current rotation.
     *
     * @return {@link Vector3f} A vector designating the right direction of the camera.
     */
    public Vector3f right() {
        return forward().cross(new Vector3f(0, 1, 0)).normalize();
    }

    /**
     * Creates a projection-view matrix using the camera's current whereabouts.
     *
     * @return {@link Matrix4f} The resulting projection-view matrix.
     */
    public Matrix4f createProjectionViewMatrix() {
        updateViewMatrix();
        Matrix4f result = new Matrix4f(proj);
        result.mul(view);
        return result;
    }

    /**
     * Returns this camera's projection matrix.
     *
     * @return {@link Matrix4f} A matrix representing how objects should be flattened by this camera instance.
     */
    public Matrix4f getProjectionMatrix() {
        return new Matrix4f(proj);
    }

    /**
     * Returns this camera's view matrix.
     *
     * @return {@link Matrix4f} A matrix representing the camera's current whereabouts.
     */
    public Matrix4f getViewMatrix() {
        return new Matrix4f(view);
    }

    /**
     * Update method used to run camera logic every frame.
     *
     * @param delta The amount of time that has passed since the last rendered frame in seconds.
     */
    public abstract void update(double delta);

    /**
     * {@inheritDoc}
     */
    public abstract void destroy();

}
