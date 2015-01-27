package ca.informi.wagoneer.oo.gameobject;

import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.wagoneer.Wagoneer;
import ca.informi.wagoneer.oo.RenderOptions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

public class LifeboatWagonObject extends WagonObject {

	private static Vector2 size = new Vector2(2, 2);
	private static final float THRUST = 5.f; // m / s^2
	private static final float TORQUE = 10.f; // rad / s^2

	private final ParticleRenderer engineEffectRenderer = new ParticleRenderer(this, new Vector2(0, -1.1f),
			Wagoneer.instance.resources.engineEffect.object, 0.5f, false);

	private boolean thrust;
	private boolean torqueLeft;
	private boolean torqueRight;

	public LifeboatWagonObject(final Vector2 position, final float angle) {
		super(Wagoneer.instance.resources.oryxAtlas.object.createSprite("runabout_white"), size, position, angle);
		delegates.add(WagonMessageType.CONTROL_ROTATE_LEFT, new WagonDelegate() {
			@Override
			public void handle(final WagonMessage message) {
				handleRotateLeft(message);
			}
		});
		delegates.add(WagonMessageType.CONTROL_ROTATE_RIGHT, new WagonDelegate() {
			@Override
			public void handle(final WagonMessage message) {
				handleRotateRight(message);
			}
		});
		delegates.add(WagonMessageType.CONTROL_THRUST, new WagonDelegate() {
			@Override
			public void handle(final WagonMessage message) {
				handleThrust(message);
			}
		});
		engineEffectRenderer.setFountainSpeedCone(-5.5f, 10.f);
	}

	@Override
	public void render(final RenderOptions opts) {
		engineEffectRenderer.render(opts);
		super.render(opts);
	}

	@Override
	public void update(final Interval interval) {
		super.update(interval);
		if (torqueLeft) {
			addTorque(TORQUE);
		}
		if (torqueRight) {
			addTorque(-TORQUE);
		}
		if (thrust && !hitches.isHitched(WagonHitch.AFT)) {
			engineEffectRenderer.setEmitting(true);
			addOrientedForce(THRUST);
		} else {
			engineEffectRenderer.setEmitting(false);
		}
		engineEffectRenderer.update(interval);
	}

	@Override
	protected void busWillUpdate() {
		torqueLeft = torqueRight = thrust = false;
	}

	@Override
	protected void createBodyFixtures(final Array<FixtureDef> fd, final Vector2 size) {
		final FixtureDef body = new FixtureDef();
		body.friction = 0.2f;
		body.restitution = 0.8f;
		body.density = 1.0f;

		final CircleShape bodyShape = new CircleShape();
		bodyShape.setRadius(size.x / 2.f);
		body.shape = bodyShape;

		fd.add(body);
	}

	@Override
	protected void disposeInner() {
		super.disposeInner();
		engineEffectRenderer.dispose();
	}

	protected void handleRotateLeft(final WagonMessage message) {
		torqueLeft = true;
	}

	protected void handleRotateRight(final WagonMessage message) {
		torqueRight = true;
	}

	protected void handleThrust(final WagonMessage message) {
		thrust = true;
	}

	@Override
	protected boolean willAcceptConnection(final int direction) {
		return (direction != WagonHitch.FORE);
	}

}
