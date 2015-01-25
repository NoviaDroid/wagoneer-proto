package ca.informi.gdx.delegate.screen;

import ca.informi.gdx.delegate.ResPackage;
import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.gdx.delegate.controller.Controller;
import ca.informi.gdx.delegate.screen.GameScreen.MyResourcePackage;
import ca.informi.gdx.graphics.g2d.DFFont;
import ca.informi.wagoneer.oo.Game;
import ca.informi.wagoneer.oo.RenderOptions;
import ca.informi.wagoneer.oo.gameobject.Renderable;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen extends Screen<MyResourcePackage> {

	static class MyResourcePackage extends ResPackage {
		final ResHandle<I18NBundle> bundle = add("bundle/Title", I18NBundle.class);
		final ResHandle<DFFont> font = add("font/syncopate-df.fnt", DFFont.class);
		final ResHandle<TextureAtlas> mainAtlas = add("image/main.atlas", TextureAtlas.class);
		final ResHandle<Music> music = add("music/logo.ogg", Music.class);
	}

	private OrthographicCamera camera;
	private RenderOptions renderOptions;
	private final Array<Renderable> rq = new Array<Renderable>();
	private FitViewport viewport;
	private float worldScale;

	public GameScreen() {
		super(new MyResourcePackage());
	}

	public float getWorldScale() {
		return worldScale;
	}

	@Override
	public void removed() {
	}

	@Override
	public void render() {
		final Game game = Controller.instance.get(Game.class);
		game.renderRQ(camera, renderOptions, rq);
	}

	@Override
	public void resize(final int width, final int height) {
		super.resize(width, height);
		viewport.update(width, height);
	}

	public void setWorldScale(final float worldScale) {
		this.worldScale = worldScale;
	}

	@Override
	public void update(final Interval interval) {
		final Game game = Controller.instance.get(Game.class);
		game.updateRQ(camera, rq);
	}

	@Override
	protected void addedInternal() {
		Box2D.init();

		camera = new OrthographicCamera();
		viewport = new FitViewport(1280, 720, camera);

		renderOptions = new RenderOptions();
		worldScale = 0.01f;

		camera = new OrthographicCamera(1280 * worldScale, 720 * worldScale);
		viewport = new FitViewport(1280 * worldScale, 720 * worldScale, camera);
	}

	@Override
	protected void disposeInternal() {
	}

}
