package ca.informi.wagoneer.oo.gameobject;

import ca.informi.gdx.delegate.controller.Controller;
import ca.informi.wagoneer.oo.Game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Box2DObject extends GameObject {

	public final Body body;

	public Box2DObject(final BodyDef bodyDef, final FixtureDef fixtureDef) {
		final World world = Controller.instance.get(Game.class).world;
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		body.setUserData(this);

		fixtureDef.shape.dispose();
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
		final World world = Controller.instance.get(Game.class).world;
		world.destroyBody(body);
	}

}
