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
import ca.informi.wagoneer.oo.gameobject.EngineWagonObject;
import ca.informi.wagoneer.oo.gameobject.GameObject;
import ca.informi.wagoneer.oo.gameobject.LifeboatWagonObject;
import ca.informi.wagoneer.oo.gameobject.Renderable;
import ca.informi.wagoneer.oo.gameobject.WagonObject;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.I18NBundle;

public class Wagoneer extends Game {

	public class MyResourcePackage extends ResPackage {
		public final ResHandle<ParticleEffect> engineEffect = add("image/engine.p", ParticleEffect.class);
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

		private final Vector2 gameSize = new Vector2(10, 10);
		private boolean isStarted;
		private Handle<GameObject> playerHandle;
		private final Random random = new Random();
		private World world;

		public Handle<GameObject> createPlayer() {
			playerHandle = new Handle<GameObject>(new LifeboatWagonObject(randomVec2(null), randomAngle()));
			return playerHandle;
		}

		public void createWorld() {
			world = new World(new Vector2(), true);
			world.setContactListener(new DelegatingContactListener());
			new EngineWagonObject(randomVec2(null), randomAngle());
			new EngineWagonObject(randomVec2(null), randomAngle());
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

		private float randomAngle() {
			return random.nextFloat() * MathUtils.PI * 2;
		}

		private Vector2 randomVec2(Vector2 v) {
			if (v == null) v = new Vector2();
			return v.set(random.nextFloat(), random.nextFloat())
					.scl(gameSize);
		}

	}

	public static Wagoneer instance;
	public final ObjectRegistry objectRegistry = new ObjectRegistry();
	public Handle<GameObject> player;
	public final GORenderer renderer = new GORenderer();
	public final MyResourcePackage resources = new MyResourcePackage();
	public final ResourceService resourceService;
	private final PlayerInputHandler input = new PlayerInputHandler();
	private final GOManager objects = new GOManager(renderer);
	private boolean paused;
	private final IntervalTimer timer = new IntervalTimer();
	protected GameLogic gameLogic;

	public Wagoneer(final FileHandleResolver resolver) {
		resourceService = new ResourceService(resolver);
		instance = this;
	}

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		I18NBundle.setSimpleFormatter(true);
		resources.load()
					.onReady(new Runnable() {
						@Override
						public void run() {
							renderer.init();
							timer.start();
							new GamePlayScreen();
							gameLogic = new GameLogic();
							gameLogic.createWorld();
							player = gameLogic.createPlayer();
							gameLogic.startGame();
						}
					});
	}

	@Override
	public void dispose() {
		if (resources != null) resources.dispose();
		if (player != null) player.dispose();
		if (gameLogic != null) gameLogic.dispose();
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
		update();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}

	public void renderRQ(final RenderOptions renderOptions, final Array<Renderable> rq) {
		renderer.render(getWorld(), renderOptions, rq);
	}

	@Override
	public void resume() {
		paused = false;
		super.resume();
	}

	public void updateRQ(final RenderOptions opts, final Array<Renderable> rq) {
		opts.camera.update();
		renderer.update(getWorld(), opts, rq);
	}

	private void update() {
		final Interval interval = timer.getInterval();
		resourceService.update();
		if (!paused && gameLogic != null && gameLogic.isStarted()) {
			if (player != null && player.object != null && player.object instanceof WagonObject) {
				input.update(interval, (WagonObject) player.object);
			}
			// Gdx.app.debug("Wagoneer", String.format("Player position: %s",
			// player.object.getPosition()));
			getWorld().step(interval.dt, 8, 3);
			objects.update(interval);
		}
	}

}
