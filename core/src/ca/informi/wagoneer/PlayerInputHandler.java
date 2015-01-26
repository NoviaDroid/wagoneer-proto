package ca.informi.wagoneer;

import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.wagoneer.oo.gameobject.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class PlayerInputHandler {

	private static final float THRUST = 10.f; // m / s^2
	private static final float TORQUE = 10.f; // rad / s^2

	public void update(Interval interval, GameObject object) {
		float dt = interval.dt;
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			object.addTorque(TORQUE);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			object.addTorque(-TORQUE);
		}
		if (Gdx.input.isKeyJustPressed(Keys.UP)) {
			object.addOrientedForce(THRUST);
		}
	}

}
