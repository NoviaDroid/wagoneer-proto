package ca.informi.wagoneer.oo.gameobject;

import com.badlogic.gdx.math.Vector2;

public abstract class PhysicsObject extends GameObject {

	public abstract void addOrientedForce(float f);

	public abstract void addOrientedForce(final Vector2 f);

	public abstract void addTorque(float f);

	public abstract float getAngularVelocity();

	public abstract Vector2 getVelocity();

	public abstract void setAngularVelocity(float avel);

	public abstract void setVelocity(Vector2 velocity);

}
