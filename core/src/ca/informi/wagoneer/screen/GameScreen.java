package ca.informi.wagoneer.screen;

import ca.informi.ApplicationDelegate;
import ca.informi.FontDraw;
import ca.informi.IntervalTimer.Interval;
import ca.informi.gdx.graphics.g2d.DFFont;
import ca.informi.service.ResourcePackage;
import ca.informi.service.ResourcePackage.Ready;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
	private Sprite logo;
	private final MyResourcePackage resources = new MyResourcePackage();
	private SpriteBatch spriteBatch;
	private FitViewport viewport;

	@Override
	public void added() {
	}

	@Override
	public void create() {
		spriteBatch = controller.services.get(SpriteBatch.class);
		resources.load(this).onReady(new Ready() {
			@Override
			public void onReady() {
				resources.music.o.play();
				logo = resources.atlas.o.createSprite("logo");
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
		if (Gdx.input.isButtonPressed(0)) {
			controller.replace(this, new GameScreen());
		}
	}

}
