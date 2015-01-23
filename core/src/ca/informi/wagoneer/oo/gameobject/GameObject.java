package ca.informi.wagoneer.oo.gameobject;

import ca.informi.gdx.Controller;
import ca.informi.wagoneer.oo.GameScreen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public abstract class GameObject implements Disposable {

	public final int id;

	public GameObject() {
		id = Controller.instance.get(GameScreen.class)
								.register(this);
	}

	@Override
	public final void dispose() {
		disposeInner();
		Controller.instance.get(GameScreen.class)
							.unregister(this);
	}

	public abstract float getAngle();

	public abstract float getAngularVelocity();

	public abstract Vector2 getPosition();

	public abstract Vector2 getVelocity();

	public abstract void setAngle(float angle);

	public abstract void setAngularVelocity(float avel);

	public abstract void setPosition(Vector2 position);

	public abstract void setVelocity(Vector2 velocity);

	protected abstract void disposeInner();

}