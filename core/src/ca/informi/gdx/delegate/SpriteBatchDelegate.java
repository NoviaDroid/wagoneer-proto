package ca.informi.gdx.delegate;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteBatchDelegate extends ApplicationDelegate {

	public final SpriteBatch batch = new SpriteBatch();

	@Override
	public void dispose() {
		batch.dispose();
	}

}
