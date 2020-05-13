package camera;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import objects.GameObject;

public abstract class Camera implements GameObject {

	private float aspect, fovx, fovy;
	
	protected Vector3f pos, rot;
	
	private Matrix4f proj, view;
	
	public Camera(float aspect, float fov) {
		this.aspect = aspect;
		this.fovx = fov;
		this.pos = new Vector3f();
		this.rot = new Vector3f();
		updateProjectionMatrix();
		updateViewMatrix();
	}
	
	private void updateProjectionMatrix() {
		fovy = (float) (2 * Math.atan(Math.tan(Math.toRadians(fovx) / 2) / aspect));
		proj = new Matrix4f().perspective(fovy, aspect, 0.1f, 1000f);
		fovy = (float) Math.toDegrees(fovy);
	}
	
	private void updateViewMatrix() {
		view = new Matrix4f();

		view.rotate((float) (rot.x * Math.PI / 180), new Vector3f(1, 0, 0));
		view.rotate((float) (rot.y * Math.PI / 180), new Vector3f(0, 1, 0));
		view.rotate((float) (rot.z * Math.PI / 180), new Vector3f(0, 0, 1));
		
		Vector3f invPos = new Vector3f();
		pos.mul(-1.0f, invPos);
		view.translate(invPos);
	}
	
	public Camera setAspect(int n, int d) {
		this.fovx = (float) n / d;
		return this;
	}
	
	public Camera setFOV(float fov) {
		this.fovx = fov;
		return this;
	}
	
	public Camera setPosition(Vector3f pos) {
		if(pos != null)
			this.pos = pos;
		return this;
	}
	
	public Vector3f getRotation() {
		return new Vector3f(rot);
	}
	
	public Vector3f getPosition() {
		return new Vector3f(pos);
	}
	
	public float getFovX() {
		return fovx;
	}
	
	public float getFovY() {
		return fovy;
	}
	
	public float getAspect() {
		return aspect;
	}
	
	public Camera setRotation(Vector3f rot) {
		if(rot != null)
			this.rot = rot;
		return this;
	}
	
	public Camera translate(Vector3f pos) {
		if(pos != null)
			this.pos = this.pos.add(pos);
		return this;
	}
	
	public Camera rotate(Vector3f rot) {
		if(rot != null)
			this.rot = this.rot.add(rot);
		return this;
	}
	
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
	
	public Camera reset() {
		this.rot = new Vector3f();
		this.pos = new Vector3f();
		return this;
	}
	
	public Vector3f forward() {
		Matrix4f transform = new Matrix4f();
		
		transform.rotate((float) ((rot.y - 90) * Math.PI / 180), new Vector3f(0, 1, 0));
		transform.rotate((float) (-rot.x * Math.PI / 180), new Vector3f(0, 0, 1));
		
		Vector4f vec = transform.transform(new Vector4f(1, 0, 0, 1));

		return new Vector3f(vec.x / vec.w, vec.y / vec.w, -vec.z / vec.w);
	}
	
	public Vector3f right() {
		return forward().cross(new Vector3f(0, 1, 0)).normalize();
	}
	
	public Matrix4f createProjectionViewMatrix() {
		updateViewMatrix();
		Matrix4f result = new Matrix4f(proj);
		result.mul(view);
		return result;
	}
	
	public Matrix4f getProjectionMatrix() {
		return new Matrix4f(proj);
	}
	
	public Matrix4f getViewMatrix() {
		return new Matrix4f(view);
	}
	
	public abstract void update(double delta);
	public abstract void destroy();
	
}
