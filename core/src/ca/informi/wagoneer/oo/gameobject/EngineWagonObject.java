package ca.informi.wagoneer.oo.gameobject;

import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.wagoneer.Wagoneer;
import ca.informi.wagoneer.oo.RenderOptions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class EngineWagonObject extends WagonObject {

	private static Vector2 size = new Vector2(3, 3);
	private static final float THRUST_FULL = 100.f;
	private static final float THRUST_MANEUVER = 10.f;
	private boolean thrustAft;
	private boolean thrustAftManeuver;
	private boolean thrustFore;

	private static float fullThrustEffectScale = 1.2f;
	private static float zeroThrustEffectScale = 0.4f;
	private final ParticleRenderer aftEngineEffectRenderer = new ParticleRenderer(this, new Vector2(size).scl(0.f, -0.6f),
			Wagoneer.instance.resources.engineEffect.object, 1.f, false);
	private final ParticleRenderer foreEngineEffectRenderer = new ParticleRenderer(this, new Vector2(size).scl(0.f, 0.6f),
			Wagoneer.instance.resources.engineEffect.object, 1.f, false);

	public EngineWagonObject(final Vector2 position, final float angle) {
		super(Wagoneer.instance.resources.oryxAtlas.object.createSprite("interceptor_white"), size, position, angle);
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
		delegates.add(WagonMessageType.ALL, new WagonDelegate() {
			@Override
			public void handle(final WagonMessage message) {
				Gdx.app.debug("EngineWagonObject", "Received message: " + message);
			}
		});
		aftEngineEffectRenderer.setFountainSpeedCone(-5.5f, 10.f);
		aftEngineEffectRenderer.setFountainSpeedCone(-5.5f, 10.f);
	}

	@Override
	public void render(final RenderOptions opts) {
		super.render(opts);
		aftEngineEffectRenderer.render(opts);
		foreEngineEffectRenderer.render(opts);
	}

	@Override
	public void update(final Interval interval) {
		super.update(interval);
		float thrust = 0.f;
		if (thrustAftManeuver) {
			thrust = THRUST_MANEUVER;
		}
		if (thrustAft) {
			thrust = THRUST_FULL;
		}
		if (thrustFore) {
			thrust -= THRUST_MANEUVER;
		}

		if (thrust == 0.f) {
			aftEngineEffectRenderer.setEmitting(false);
			foreEngineEffectRenderer.setEmitting(false);
		} else {
			final ParticleRenderer target = (thrust < 0.f ? foreEngineEffectRenderer : aftEngineEffectRenderer);
			final float scale = MathUtils.lerp(zeroThrustEffectScale, fullThrustEffectScale, Math.abs(thrust) / THRUST_FULL);
			target.setEmitting(true);
			target.setScale(scale);
			addOrientedForce(thrust);
		}
		aftEngineEffectRenderer.update(interval);
		foreEngineEffectRenderer.update(interval);
	}

	@Override
	protected void busWillUpdate() {
		thrustFore = thrustAft = thrustAftManeuver = false;
	}

	protected void handleRotateLeft(final WagonMessage message) {
		if (message.originX > 0) {
			// Message originated right
			thrustFore = true;
		} else if (message.originX < 0) {
			// Message originated left
			thrustAftManeuver = true;
		}
	}

	protected void handleRotateRight(final WagonMessage message) {
		if (message.originX < 0) {
			// Message originated left
			thrustFore = true;
		} else if (message.originX > 0) {
			// Message originated right
			thrustAftManeuver = true;
		}
	}

	protected void handleThrust(final WagonMessage message) {
		thrustAft = true;
	}

	@Override
	protected boolean willAcceptConnection(final int direction) {
		return (direction == WagonHitch.LEFT || direction == WagonHitch.RIGHT);
	}

}
