package ca.informi.gdx.assets.loaders;

import ca.informi.gdx.graphics.g2d.ProceduralTextureAtlas;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public abstract class Slicer {

	private boolean topDown;

	protected final ProceduralTextureAtlas atlas;
	protected final Pixmap pixmap;
	protected final Texture texture;

	public Slicer(final ProceduralTextureAtlas atlas, final Texture texture, final Pixmap pixmap, final boolean topDown) {
		super();
		this.atlas = atlas;
		this.texture = texture;
		this.pixmap = pixmap;
		this.topDown = topDown;
	}

	public abstract void slice();

	protected void setTopDown(final boolean topDown) {
		this.topDown = topDown;
	}

	protected void slice(final int x, final int y, final int w, final int h) {

	}
}
