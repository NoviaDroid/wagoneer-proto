package ca.informi.wagoneer;

import java.util.Random;

import ca.informi.gdx.assets.loaders.ProceduralTextureAtlasLoader.ProceduralTextureAtlasParameter;
import ca.informi.gdx.assets.loaders.slicers.OryxScifiV2Slicer;
import ca.informi.gdx.delegate.IntervalTimer;
import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.gdx.graphics.g2d.ProceduralTextureAtlas;
import ca.informi.gdx.graphics.g2d.ProceduralTextureAtlas.ProceduralPage;
import ca.informi.gdx.resource.ResPackage;
import ca.informi.gdx.resource.ResourceService;
import ca.informi.wagoneer.oo.GOManager;
import ca.informi.wagoneer.oo.GORenderer;
import ca.informi.wagoneer.oo.Handle;
import ca.informi.wagoneer.oo.RenderOptions;
import ca.informi.wagoneer.oo.gameobject.GameObject;
import ca.informi.wagoneer.oo.gameobject.PlayerWagonHead;
import ca.informi.wagoneer.oo.gameobject.Renderable;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.I18NBundle;

public class Wagoneer extends Game {

	public class MyResourcePackage extends ResPackage {
		public final ResHandle<ProceduralTextureAtlas> oryxAtlas = add("image/oryx.atlas", ProceduralTextureAtlas.class,
				new ProceduralTextureAtlasParameter(new ProceduralPage("image/lofi_scifi_v2_trans.png", new OryxScifiV2Slicer())));

		public MyResourcePackage() {
			super("shared");
		}
	}

	public class ObjectRegistry {
		public int register(final GameObject gameObject) {
			return objects.register(gameObject);
		}

		public void unregister(final GameObject gameObject) {
			objects.remove(gameObject);
		}
	}

	private class GameLogic implements Disposable {

		private final Vector2 gameSize = new Vector2(1, 1);
		private boolean isStarted;
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

		public void createWorld() {
			world = new World(new Vector2(), true);
		}

		@Override
		public void dispose() {
			endGame();
			world.dispose();
		}

		public void endGame() {
			objects.clear();
			isStarted = false;
		}

		public boolean isStarted() {
			return isStarted;
		}

		public void startGame() {
			isStarted = true;
		}

	}

	public static Wagoneer instance;
	public final ObjectRegistry objectRegistry = new ObjectRegistry();
	public Handle<GameObject> player;
	public final GORenderer renderer = new GORenderer();
	public final MyResourcePackage resources = new MyResourcePackage();
	private final GOManager objects = new GOManager(renderer);
	private boolean paused;
	private final ResourceService resourceService = new ResourceService();
	private final IntervalTimer timer = new IntervalTimer();
	protected GameLogic gameLogic;

	@Override
	public void create() {
		instance = this;
		I18NBundle.setSimpleFormatter(true);
		resources.load(resourceService)
					.onReady(new Runnable() {
						@Override
						public void run() {
							renderer.init();
							timer.start();
							setScreen(new GamePlayScreen());
							gameLogic = new GameLogic();
							gameLogic.createWorld();
							player = gameLogic.createPlayer();
							gameLogic.startGame();
						}
					});
	}

	@Override
	public void dispose() {
		resources.dispose();
		player.dispose();
		gameLogic.dispose();
		super.dispose();
	}

	public World getWorld() {
		return gameLogic.world;
	}

	@Override
	public void pause() {
		paused = true;
		super.pause();
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		resourceService.update();
		final Interval interval = timer.getInterval();
		if (!paused && gameLogic != null && gameLogic.isStarted()) {
			getWorld().step(interval.dt, 8, 3);
			objects.update(interval);
		}
		super.render();
	}

	public void renderRQ(final OrthographicCamera camera, final Array<Renderable> rq, final RenderOptions renderOptions) {
		renderer.render(getWorld(), camera, renderOptions, rq);
	}

	@Override
	public void resume() {
		paused = false;
		super.resume();
	}

	public void updateRQ(final OrthographicCamera camera, final Array<Renderable> rq) {
		renderer.update(getWorld(), camera, rq);
	}

}
