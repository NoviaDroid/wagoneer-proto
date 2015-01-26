package ca.informi.wagoneer;

import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.wagoneer.oo.gameobject.WagonMessageType;
import ca.informi.wagoneer.oo.gameobject.WagonObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class PlayerInputHandler {

	public void update(final Interval interval, final WagonObject object) {
		final float dt = interval.dt;
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			object.sendMessge(WagonMessageType.CONTROL_ROTATE_LEFT, null);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			object.sendMessge(WagonMessageType.CONTROL_ROTATE_RIGHT, null);
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			object.sendMessge(WagonMessageType.CONTROL_THRUST, null);
		}
	}

}
