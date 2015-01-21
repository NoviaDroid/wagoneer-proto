package ca.informi.wagoneer.screen;

import ca.informi.FontDraw;
import ca.informi.IntervalTimer.Interval;
import ca.informi.ResourcePackage;
import ca.informi.gdx.graphics.g2d.DFFont;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class TitleScreen extends Screen {

	private class MyResourcePackage extends ResourcePackage {
		final Handle<Music> music = add("music/logo.ogg", Music.class);
		final Handle<Texture> logo = add("image/logo.png", Texture.class);
		final Handle<DFFont> font = add("font/syncopate-df.fnt", DFFont.class);
		final Handle<I18NBundle> bundle = add("bundle/Title", I18NBundle.class);
	}

	private final MyResourcePackage resources = new MyResourcePackage();
	private SpriteBatch spriteBatch;
	private float totalTime;
	private float scale;
	private OrthographicCamera camera;
	private FitViewport viewport;

	@Override
	public void added() {
	}

	@Override
	public void create() {
		spriteBatch = controller.services.get(SpriteBatch.class);
		resources.load(this);
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
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void removed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.f, 0.f, 0.f, 0.f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();

		final BitmapFont font = resources.font.o.begin(spriteBatch);
		resources.font.o.setSize(72);
		resources.font.o.setWeight(100);
		FontDraw.drawCenteredOn(spriteBatch, font, resources.bundle.o.get("title"), 640, 360);

		resources.font.o.setWeight(400);
		resources.font.o.setSize(12);
		font.draw(spriteBatch, resources.bundle.o.get("copyright"), 10, 16);

		resources.font.o.end();

		spriteBatch.end();
	}

	@Override
	public void resize(final int width, final int height) {
		viewport.update(width, height);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(final Interval interval) {
		if (Gdx.input.isButtonPressed(0)) {
			Gdx.input.getTextInput(new TextInputListener() {
				@Override
				public void canceled() {
					Gdx.app.log("TIL", "Cancelled");
				}

				@Override
				public void input(final String text) {
					Gdx.app.log("TIL", "String: " + text);
				}
			}, "Hello", "text", "hint");
		}
	}

}
