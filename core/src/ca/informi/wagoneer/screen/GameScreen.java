package ca.informi.wagoneer.screen;

import ca.informi.gdx.ApplicationDelegate;
import ca.informi.gdx.graphics.g2d.DFFont;
import ca.informi.service.IntervalTimer.Interval;
import ca.informi.service.ResourcePackage;
import ca.informi.service.ResourcePackage.Ready;
import ca.informi.service.Services;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen extends ApplicationDelegate {

	private class MyResourcePackage extends ResourcePackage {
		final Handle<TextureAtlas> atlas = add("image/main.atlas", TextureAtlas.class);
		final Handle<I18NBundle> bundle = add("bundle/Title", I18NBundle.class);
		final Handle<DFFont> font = add("font/syncopate-df.fnt", DFFont.class);
		final Handle<Music> music = add("music/logo.ogg", Music.class);
	}

	private OrthographicCamera camera;
	private final MyResourcePackage resources = new MyResourcePackage();
	private SpriteBatch spriteBatch;
	private FitViewport viewport;

	@Override
	public void added() {
	}

	@Override
	public void create() {
		spriteBatch = Services.instance.get(SpriteBatch.class);
		resources.load(this)
					.onReady(new Ready() {
						@Override
						public void onReady() {
						}
					});
		camera = new OrthographicCamera();
		viewport = new FitViewport(1280, 720, camera);
	}

	@Override
	public void dispose() {
		resources.dispose();
	}

	@Override
	public boolean isReady() {
		return resources.isReady();
	}

	@Override
	public void render() {
		spriteBatch.begin();

		spriteBatch.end();
	}

	@Override
	public void resize(final int width, final int height) {
		viewport.update(width, height);
	}

	@Override
	public void update(final Interval interval) {
	}

}
