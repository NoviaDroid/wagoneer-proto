package ca.informi.wagoneer.oo;

import ca.informi.delegate.IntervalTimer.Interval;
import ca.informi.delegate.ResPackage;
import ca.informi.delegate.SpriteService;
import ca.informi.delegate.screen.Screen;
import ca.informi.gdx.Controller;
import ca.informi.gdx.SpriteBatchDelegate;
import ca.informi.gdx.assets.loaders.ProceduralTextureAtlasLoader.ProceduralTextureAtlasParameter;
import ca.informi.gdx.graphics.g2d.DFFont;
import ca.informi.gdx.graphics.g2d.ProceduralTextureAtlas;
import ca.informi.gdx.graphics.g2d.ProceduralTextureAtlas.ProceduralPage;
import ca.informi.wagoneer.oo.GameScreen.MyResourcePackage;
import ca.informi.wagoneer.oo.gameobject.GameObject;
import ca.informi.wagoneer.oo.gameobject.PlayerWagonHead;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen extends Screen<MyResourcePackage> {

	static class MyResourcePackage extends ResPackage {
		final ResHandle<I18NBundle> bundle = add("bundle/Title", I18NBundle.class);
		final ResHandle<DFFont> font = add("font/syncopate-df.fnt", DFFont.class);
		final ResHandle<TextureAtlas> mainAtlas = add("image/main.atlas", TextureAtlas.class);
		final ResHandle<Music> music = add("music/logo.ogg", Music.class);
		final ResHandle<? extends TextureAtlas> oryxAtlas = add("image/oryx.atlas", ProceduralTextureAtlas.class,
				new ProceduralTextureAtlasParameter(new ProceduralPage("image/lofi_scifi_v2_trans.png", new OryxScifiV2Slicer())));
	}

	public World world;

	private OrthographicCamera camera;

	private GOManager objects;

	private boolean paused;

	private Handle<GameObject> player;
	private GORenderer renderer;
	private RenderOptions renderOptions;

	private SpriteBatch spriteBatch;

	private SpriteService spriteService;
	private FitViewport viewport;
	private float worldScale;

	public GameScreen() {
		super(new MyResourcePackage());
	}

	public float getWorldScale() {
		return worldScale;
	}

	@Override
	public void pause() {
		paused = true;
	}

	public int register(final GameObject gameObject) {
		return objects.register(gameObject);
	}

	@Override
	public void removed() {
		objects.dispose();
		renderer.dispose();
		Controller.instance.remove(spriteService);
		world.dispose();
	}

	@Override
	public void render() {
		renderer.render(world, camera, renderOptions);
	}

	@Override
	public void resize(final int width, final int height) {
		super.resize(width, height);
		viewport.update(width, height);
	}

	@Override
	public void resume() {
		paused = false;
	}

	public void setWorldScale(final float worldScale) {
		this.worldScale = worldScale;
	}

	public void unregister(final GameObject gameObject) {
		objects.remove(gameObject);
	}

	@Override
	public void update(final Interval interval) {
		if (!paused) {
			world.step(interval.dt, 8, 3);
			objects.update(interval);
		}
		renderer.update(world, camera);
	}

	@Override
	public boolean updateWhilePaused() {
		return true;
	}

	@Override
	protected void addedInternal() {
		Box2D.init();

		spriteBatch = Controller.instance.get(SpriteBatchDelegate.class).batch;
		camera = new OrthographicCamera();
		viewport = new FitViewport(1280, 720, camera);

		renderer = new GORenderer();
		objects = new GOManager(renderer);
		renderOptions = new RenderOptions();
		spriteService = new SpriteService();
		world = new World(new Vector2(), true);
		worldScale = 0.01f;

		camera = new OrthographicCamera(1280 * worldScale, 720 * worldScale);
		viewport = new FitViewport(1280 * worldScale, 720 * worldScale, camera);

		Controller.instance.add(spriteService);

		player = new Handle<GameObject>(new PlayerWagonHead(new Vector2(), 0));
	}

	@Override
	protected void disposeInternal() {
	}

}
