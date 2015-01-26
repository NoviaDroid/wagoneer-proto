package ca.informi.wagoneer.oo.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Box2DObject extends GameObject {

	public final Body body;
	private final Vector2 force = new Vector2();

	public Box2DObject(final BodyDef bodyDef, final FixtureDef[] fixtureDefs) {
		final World world = game.getWorld();
		body = world.createBody(bodyDef);
		for (FixtureDef fixtureDef : fixtureDefs) {
			body.createFixture(fixtureDef);
			fixtureDef.shape.dispose();
		}
		body.setUserData(this);
	}

	@Override
	public void addOrientedForce(float f) {
		force.set(0.f, f);
		force.rotateRad(getAngle());
		body.applyForceToCenter(force, true);
	}

	public void addOrientedForce(Vector2 f) {
		force.set(f);
		force.rotateRad(getAngle());
		body.applyForceToCenter(force, true);
	}

	@Override
	public void addTorque(float f) {
		body.applyTorque(f, true);
	}

	@Override
	public float getAngle() {
		return body.getAngle();
	}

	@Override
	public float getAngularVelocity() {
		return body.getAngularVelocity();
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}

	@Override
	public Vector2 getVelocity() {
		return body.getLinearVelocity();
	}

	/**
	 * If a subclass implements {@link Renderable}, we'll use an AABB query to
	 * determine visibility.
	 *
	 * @return
	 */
	public boolean isAlwaysVisible() {
		return false;
	}

	@Override
	public void setAngle(final float angle) {
		body.setTransform(body.getPosition(), angle);
	}

	@Override
	public void setAngularVelocity(final float avel) {
		body.setAngularVelocity(avel);
	}

	@Override
	public void setPosition(final Vector2 position) {
		body.setTransform(position, body.getAngle());
	}

	@Override
	public void setVelocity(final Vector2 velocity) {
		body.setLinearVelocity(velocity);
	}

	@Override
	protected void disposeInner() {
		game.getWorld().destroyBody(body);
	}

}
