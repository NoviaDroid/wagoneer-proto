package ca.informi.gdx.assets.loaders.slicers;

import ca.informi.gdx.graphics.g2d.ProceduralTextureAtlas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Slicer {

	protected ProceduralTextureAtlas atlas;
	protected Pixmap pixmap;
	protected Texture texture;
	protected boolean topDown;

	public Slicer() {
	}

	public void setSliceParams(final ProceduralTextureAtlas atlas, final Texture texture, final Pixmap pixmap) {
		this.atlas = atlas;
		this.texture = texture;
		this.pixmap = pixmap;
	}

	public abstract void slice();

	protected void setTopDown(final boolean topDown) {
		this.topDown = topDown;
	}

	protected void slice(final String name, final int x, int y, final int w, final int h) {
		final TextureRegion r = new TextureRegion(texture);
		if (topDown) {
			y = pixmap.getHeight() - y;
		}
		r.setRegion(x, y, w, h);
		final AtlasRegion s = atlas.findRegion(name);
		if (s == null) {
			atlas.addRegion(name, r);
		} else {
			Gdx.app.debug("Slicer", "Moving atlas region " + name);
			s.setRegion(r);
		}
	}

}
