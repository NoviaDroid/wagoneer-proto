package ca.informi.wagoneer.oo.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public abstract class Box2DObject extends PhysicsObject {

	public final Body body;
	private final Vector2 force = new Vector2();

	public Box2DObject(final Vector2 size, final Vector2 position, final float angle) {
		final World world = game.getWorld();
		body = world.createBody(getBodyDef(position, angle));
		final Array<FixtureDef> fixtureDefs = getFixtureDefs(size);
		for (final FixtureDef fixtureDef : fixtureDefs) {
			body.createFixture(fixtureDef);
			fixtureDef.shape.dispose();
		}
		body.setUserData(this);
	}

	@Override
	public void addOrientedForce(final float f) {
		force.set(0.f, f);
		force.rotateRad(getAngleRadians());
		body.applyForceToCenter(force, true);
	}

	public void addOrientedForce(final Vector2 f) {
		force.set(f);
		force.rotateRad(getAngleRadians());
		body.applyForceToCenter(force, true);
	}

	@Override
	public void addTorque(final float f) {
		body.applyTorque(f, true);
	}

	@Override
	public float getAngleRadians() {
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
	@Override
	public boolean isAlwaysVisible() {
		return false;
	}

	@Override
	public void setAngleRadians(final float angle) {
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
		game.getWorld()
			.destroyBody(body);
	}

	protected abstract BodyDef getBodyDef(final Vector2 position, float angle);

	protected abstract Array<FixtureDef> getFixtureDefs(final Vector2 size);

}
