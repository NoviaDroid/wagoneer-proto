package ca.informi.wagoneer.oo;

import java.util.Random;

import ca.informi.gdx.assets.loaders.ProceduralTextureAtlasLoader.ProceduralTextureAtlasParameter;
import ca.informi.gdx.assets.loaders.slicers.OryxScifiV2Slicer;
import ca.informi.gdx.delegate.ApplicationDelegate;
import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.gdx.delegate.ResPackage;
import ca.informi.gdx.delegate.screen.GameScreen;
import ca.informi.gdx.graphics.g2d.ProceduralTextureAtlas;
import ca.informi.gdx.graphics.g2d.ProceduralTextureAtlas.ProceduralPage;
import ca.informi.wagoneer.oo.gameobject.GameObject;
import ca.informi.wagoneer.oo.gameobject.PlayerWagonHead;
import ca.informi.wagoneer.oo.gameobject.Renderable;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Game extends ApplicationDelegate {

	public class MyResourcePackage extends ResPackage {
		public final ResHandle<ProceduralTextureAtlas> oryxAtlas = add("image/oryx.atlas", ProceduralTextureAtlas.class,
				new ProceduralTextureAtlasParameter(new ProceduralPage("image/lofi_scifi_v2_trans.png", new OryxScifiV2Slicer())));
	}

	public class ObjectRegistry {
		public int register(final GameObject gameObject) {
			return objects.register(gameObject);
		}

		public void unregister(final GameObject gameObject) {
			objects.remove(gameObject);
		}
	}

	private class GameLogic {

		private final Vector2 gameSize = new Vector2(10000, 10000);
		private Handle<GameObject> playerHandle;
		private final Random random = new Random();
		private World world;

		public Handle<GameObject> createPlayer() {
			final Vector2 startingPosition = new Vector2();
			startingPosition.set(random.nextFloat(), random.nextFloat())
							.scl(gameSize);
			final float startingAngle = random.nextFloat() * MathUtils.PI * 2;
			final PlayerWagonHead player = new PlayerWagonHead(startingPosition, startingAngle);
			playerHandle = new Handle<GameObject>(player);
			return playerHandle;
		}

		public World createWorld() {
			world = new World(new Vector2(), true);
			return world;
		}

		public void startGame() {
		}

	}

	public final ObjectRegistry objectRegistry = new ObjectRegistry();
	public Handle<GameObject> player;
	public final GORenderer renderer = new GORenderer();
	public final MyResourcePackage resources = new MyResourcePackage();
	public World world;
	private GOManager objects;
	private boolean paused;
	protected GameLogic gameLogic;

	@Override
	public void added() {
		Box2D.init();
		resources.load(this)
					.onReady(new Runnable() {
						@Override
						public void run() {
							gameLogic = new GameLogic();
							world = gameLogic.createWorld();
							player = gameLogic.createPlayer();
							new GameScreen().add()
											.onAdded(new Runnable() {
												@Override
												public void run() {
													gameLogic.startGame();
												}
											});
						};
					});
	}

	@Override
	public void dispose() {
		objects.dispose();
		renderer.dispose();
		world.dispose();
		resources.dispose();
	}

	@Override
	public void pause() {
		paused = true;
	}

	public void renderRQ(final OrthographicCamera camera, final RenderOptions renderOptions, final Array<Renderable> rq) {
		renderer.render(world, camera, renderOptions, rq);
	}

	@Override
	public void resume() {
	}

	@Override
	public void suspend() {
	}

	@Override
	public void unpause() {
		paused = false;
	}

	@Override
	public void update(final Interval interval) {
		if (!paused) {
			world.step(interval.dt, 8, 3);
			objects.update(interval);
		}
	}

	public void updateRQ(final OrthographicCamera camera, final Array<Renderable> rq) {
		renderer.update(world, camera, rq);
	}

	@Override
	public boolean updateWhilePaused() {
		return true;
	}

}
