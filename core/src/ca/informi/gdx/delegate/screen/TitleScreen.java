package ca.informi.gdx.delegate.screen;

import ca.informi.gdx.delegate.ResPackage;
import ca.informi.gdx.delegate.SpriteBatchDelegate;
import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.gdx.delegate.controller.Controller;
import ca.informi.gdx.delegate.screen.TitleScreen.MyResourcePackage;
import ca.informi.gdx.graphics.g2d.DFFont;
import ca.informi.gdx.graphics.g2d.FontDraw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class TitleScreen extends Screen<MyResourcePackage> {

	static class MyResourcePackage extends ResPackage {
		final ResHandle<TextureAtlas> atlas = add("image/main.atlas", TextureAtlas.class);
		final ResHandle<I18NBundle> bundle = add("bundle/Title", I18NBundle.class);
		final ResHandle<DFFont> font = add("font/syncopate-df.fnt", DFFont.class);
		final ResHandle<Music> music = add("music/logo.ogg", Music.class);
	}

	private OrthographicCamera camera;
	private Sprite logo;
	private SpriteBatch spriteBatch;
	private FitViewport viewport;

	public TitleScreen() {
		super(new MyResourcePackage());
	}

	@Override
	public void render() {
		spriteBatch.begin();

		logo.draw(spriteBatch);

		final BitmapFont font = resources.font.o.begin(spriteBatch);
		resources.font.o.setSize(72);
		resources.font.o.setWeight(100);
		FontDraw.drawCenteredOn(spriteBatch, font, resources.bundle.o.get("title"), 640, 360);

		resources.font.o.setWeight(400);
		resources.font.o.setSize(12);
		font.drawWrapped(spriteBatch, resources.bundle.o.get("copyright"), 50, 16, 1270);

		resources.font.o.end();

		spriteBatch.end();
	}

	@Override
	public void resize(final int width, final int height) {
		viewport.update(width, height);
	}

	@Override
	public void update(final Interval interval) {
		if (Gdx.input.isButtonPressed(0) && !isReplacing()) {
			new GameScreen().replace(this);
		}
	}

	@Override
	protected void addedInternal() {
		spriteBatch = Controller.instance.get(SpriteBatchDelegate.class).batch;
		resources.music.o.play();
		logo = resources.atlas.o.createSprite("logo");
		camera = new OrthographicCamera();
		viewport = new FitViewport(1280, 720, camera);
	}

	@Override
	protected void disposeInternal() {
	}

}
