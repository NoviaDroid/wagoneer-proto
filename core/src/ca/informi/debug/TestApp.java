package ca.informi.debug;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TestApp implements ApplicationListener {

	String[] paths = { "image/main.png", "image/main2.png", "image/main3.png", "image/main4.png", "image/main5.png",
			"image/main6.png", "image/main7.png", "image/main8.png", "image/main9.png" };

	@Override
	public void create() {
		for (final String path : paths) {
			Gdx.app.log("TestApp", "Loading: " + path);
			final Texture single = new Texture(path);
			Gdx.app.log("TestApp", "Success: " + path);
			single.dispose();
		}

	}

	@Override
	public void dispose() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void render() {
	}

	@Override
	public void resize(final int width, final int height) {
	}

	@Override
	public void resume() {
	}

}
