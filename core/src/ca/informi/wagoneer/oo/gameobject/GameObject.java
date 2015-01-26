package ca.informi.wagoneer.oo.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public abstract class GameObject extends GameAware implements Disposable {

	public final int id;

	public GameObject() {
		id = game.objectRegistry.register(this);
	}

	@Override
	public final void dispose() {
		disposeInner();
		game.objectRegistry.unregister(this);
	}

	public abstract float getAngleRadians();

	public abstract Vector2 getPosition();

	public abstract boolean isAlwaysVisible();

	public abstract void setAngleRadians(float angle);

	public abstract void setPosition(Vector2 position);

	protected abstract void disposeInner();

}
