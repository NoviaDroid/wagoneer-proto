package ca.informi.wagoneer.oo;

import java.util.Comparator;

import ca.informi.wagoneer.oo.gameobject.GameObject;
import ca.informi.wagoneer.oo.gameobject.Renderable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Values;

class GORenderer implements RenderableContainer, Disposable {
	private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);
	private final Comparator<Renderable> layerComparator = new Comparator<Renderable>() {
		@Override
		public int compare(final Renderable o1, final Renderable o2) {
			final int l1 = o1.getLayer();
			final int l2 = o2.getLayer();
			return l1 > l2 ? 1 : l1 == l2 ? 0 : -1;
		}
	};

	private final QueryCallback visiblesCallback = new QueryCallback() {
		@Override
		public boolean reportFixture(final Fixture fixture) {
			final Body body = fixture.getBody();
			final GameObject obj = (GameObject) body.getUserData();
			if (obj instanceof Renderable) {
				visibles.put(obj.id, (Renderable) obj);
			}
			return true; // Continue query
		}
	};

	final IntMap<Renderable> renderables = new IntMap<Renderable>();
	final Array<Renderable> rq = new Array<Renderable>();
	final IntMap<Renderable> visibleAlways = new IntMap<Renderable>();
	final IntMap<Renderable> visibles = new IntMap<Renderable>();

	@Override
	public void clear() {
		renderables.clear();
		visibles.clear();
		visibleAlways.clear();
		rq.clear();
	}

	@Override
	public void dispose() {
		clear();
		debugRenderer.dispose();
	}

	@Override
	public void put(final int id, final Renderable renderable) {
		renderables.put(id, renderable);
		if (renderable.isAlwaysVisible()) {
			visibleAlways.put(id, renderable);
		}
	}

	@Override
	public void remove(final int id) {
		renderables.remove(id);
		visibles.remove(id);
		visibleAlways.remove(id);
	}

	public void render(final World world, final Camera camera, final RenderOptions renderOptions) {
		renderRQ(renderOptions);
		debugRenderer.render(world, camera.combined);
	}

	public void update(final World world, final OrthographicCamera camera) {
		rq.clear();
		updateCalculateVisibles(world, camera);
		updateBuildRQ();
	}

	private void renderRQ(final RenderOptions opts) {
		for (int i = 0; i < rq.size; ++i) {
			final Renderable r = rq.get(i);
			r.render(opts);
		}
	}

	private void updateBuildRQ() {
		final Values<Renderable> visible = visibleAlways.values();
		for (final Renderable r : visible) {
			rq.add(r);
		}
		final Values<Renderable> visibleConditional = visibles.values();
		for (final Renderable r : visibleConditional) {
			rq.add(r);
		}
		rq.sort(layerComparator);
	}

	private void updateCalculateVisibles(final World world, final OrthographicCamera camera) {
		visibles.clear();

		final Vector3 position = camera.position;
		final float halfwidth = camera.viewportWidth / 2.f;
		final float halfheight = camera.viewportHeight / 2.f;
		final float left = position.x - halfwidth;
		final float right = position.x + halfwidth;
		final float bottom = position.y - halfheight;
		final float top = position.y + halfheight;
		world.QueryAABB(visiblesCallback, left, bottom, right, top);
	}

}