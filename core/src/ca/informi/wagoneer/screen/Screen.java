package ca.informi.wagoneer.screen;

import ca.informi.gdx.ApplicationDelegate;

import com.badlogic.gdx.Gdx;

public abstract class Screen extends ApplicationDelegate {

	@Override
	public void create() {
		final int width = Gdx.graphics.getWidth();
		final int height = Gdx.graphics.getHeight();
		super.create();
		resize(width, height);
	}

}
