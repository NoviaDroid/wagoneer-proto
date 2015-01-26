package ca.informi.wagoneer.oo.gameobject;

import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.wagoneer.Wagoneer;

import com.badlogic.gdx.math.Vector2;

public class EngineWagonObject extends WagonObject {

	private static Vector2 size = new Vector2(3, 3);
	private static final float THRUST_FULL = 10.f;
	private static final float THRUST_MANEUVER = 1.f;
	private boolean thrustAft;
	private boolean thrustAftManeuver;
	private boolean thrustFore;

	public EngineWagonObject(final Vector2 position, final float angle) {
		super(Wagoneer.instance.resources.oryxAtlas.object.createSprite("hauler_white"), size, position, angle);
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
	}

	@Override
	public void update(final Interval interval) {
		super.update(interval);
		float thrust = 0.f;
		if (thrustAftManeuver) thrust = THRUST_MANEUVER;
		if (thrustAft) thrust = THRUST_FULL;
		if (thrustFore) thrust -= THRUST_MANEUVER;
		addOrientedForce(thrust);
	}

	@Override
	protected void busWillUpdate() {
		thrustFore = thrustAft = thrustAftManeuver = false;
	}

	protected void handleRotateLeft(final WagonMessage message) {
		if (message.originX < 0) {
			// Message originated right
			thrustFore = true;
		} else if (message.originX > 0) {
			// Message originated left
			thrustAftManeuver = true;
		}
	}

	protected void handleRotateRight(final WagonMessage message) {
		if (message.originX > 0) {
			// Message originated left
			thrustFore = true;
		} else if (message.originX < 0) {
			// Message originated right
			thrustAftManeuver = true;
		}
	}

	protected void handleThrust(final WagonMessage message) {
		thrustAft = true;
	}

	@Override
	protected boolean willAcceptConnection(final int direction) {
		return (direction == LEFT || direction == RIGHT);
	}

}
