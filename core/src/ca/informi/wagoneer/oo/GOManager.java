package ca.informi.wagoneer.oo;

import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.wagoneer.oo.gameobject.Box2DObject;
import ca.informi.wagoneer.oo.gameobject.GameObject;
import ca.informi.wagoneer.oo.gameobject.Renderable;
import ca.informi.wagoneer.oo.gameobject.Updatable;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Values;

public class GOManager implements Disposable {
	private final IntMap<Box2DObject> box2dObjects = new IntMap<Box2DObject>();
	private final IntMap<GameObject> gameObjects = new IntMap<GameObject>();
	private int nextId = 1;
	private final RenderableContainer renderableContainer;
	private final IntMap<Updatable> updatables = new IntMap<Updatable>();

	public GOManager(final RenderableContainer renderableContainer) {
		super();
		this.renderableContainer = renderableContainer;
	}

	public void clear() {
		for (final GameObject go : gameObjects.values()) {
			go.dispose();
		}
		gameObjects.clear();
		box2dObjects.clear();
		updatables.clear();
		renderableContainer.clear();
	}

	@Override
	public void dispose() {
		clear();
		renderableContainer.dispose();
	}

	public int register(final GameObject gameObject) {
		final int id = nextId++;
		gameObjects.put(id, gameObject);
		if (gameObject instanceof Box2DObject) {
			box2dObjects.put(id, (Box2DObject) gameObject);
		}
		if (gameObject instanceof Renderable) {
			final Renderable renderable = (Renderable) gameObject;
			renderableContainer.put(id, renderable);
		}
		if (gameObject instanceof Updatable) {
			updatables.put(id, (Updatable) gameObject);
		}
		return id;
	}

	public void remove(final GameObject go) {
		gameObjects.remove(go.id);
		box2dObjects.remove(go.id);
		updatables.remove(go.id);
		if (go instanceof Renderable) {
			renderableContainer.remove(go.id);
		}
	}

	public void update(final Interval interval) {
		updateUpdatables(interval);
	}

	private void updateUpdatables(final Interval interval) {
		final Values<Updatable> values = updatables.values();
		for (final Updatable u : values) {
			u.update(interval);
		}
	}
}