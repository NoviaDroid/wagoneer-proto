package ca.informi.wagoneer.component;

import ca.informi.service.PoolableComponent;

import com.badlogic.gdx.math.Vector2;

public class Newtonian2 extends PoolableComponent {

	public Vector2 position = new Vector2();
	public float rotation;
	public float torque;
	public Vector2 velocity = new Vector2();

	@Override
	public void reset() {
		position.setZero();
		velocity.setZero();
		rotation = 0.f;
		torque = 0.f;
	}

}
