package ca.informi.wagoneer.component;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

public class PhysicsComponent extends PoolableComponent {

	public static void apply(final BodyDef source, final BodyDef dest) {
		dest.active = source.active;
		dest.allowSleep = source.allowSleep;
		dest.angle = source.angle;
		dest.angularDamping = source.angularDamping;
		dest.angularVelocity = source.angularVelocity;
		dest.awake = source.awake;
		dest.bullet = source.bullet;
		dest.fixedRotation = source.fixedRotation;
		dest.gravityScale = source.gravityScale;
		dest.linearDamping = source.linearDamping;
		dest.linearVelocity.set(source.linearVelocity);
		dest.position.set(source.position);
		dest.type = source.type;
	}

	public static void apply(final BodyDef.BodyType type, final PhysicsComponent dest) {
		switch (type) {
		case DynamicBody:
			apply(dynamicBodyDef, dest.bodyDef);
			dest.mass = 1.f;
			break;

		case KinematicBody:
			apply(kinematicBodyDef, dest.bodyDef);
			dest.mass = 0.f;
			break;

		case StaticBody:
			apply(staticBodyDef, dest.bodyDef);
			dest.mass = 0.f;
			break;
		}
	}

	private static BodyDef dynamicBodyDef;
	private static BodyDef kinematicBodyDef;
	private static BodyDef staticBodyDef;
	private static BodyDef defaultBodyDef;
	{
		dynamicBodyDef = new BodyDef();
		dynamicBodyDef.type = BodyType.DynamicBody;

		kinematicBodyDef = new BodyDef();
		kinematicBodyDef.type = BodyType.KinematicBody;

		staticBodyDef = new BodyDef();
		staticBodyDef.awake = false;
		staticBodyDef.type = BodyType.StaticBody;

		defaultBodyDef = staticBodyDef;
	}

	public BodyDef bodyDef = defaultBodyDef;
	public Array<FixtureDef> fixtures;
	public float mass;

	@Override
	public void reset() {
		apply(defaultBodyDef, bodyDef);
		for (final FixtureDef fixtureDef : fixtures) {
			fixtureDef.shape.dispose();
		}
		fixtures.clear();
	}
}
