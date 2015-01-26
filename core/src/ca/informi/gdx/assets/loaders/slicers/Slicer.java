package ca.informi.gdx.assets.loaders.slicers;

import ca.informi.gdx.graphics.g2d.ProceduralTextureAtlas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Slicer {

	protected ProceduralTextureAtlas atlas;
	protected boolean invertY;
	protected Pixmap pixmap;
	protected Texture texture;

	public Slicer() {
	}

	public void setSliceParams(final ProceduralTextureAtlas atlas, final Texture texture, final Pixmap pixmap) {
		this.atlas = atlas;
		this.texture = texture;
		this.pixmap = pixmap;
	}

	public abstract void slice();

	protected void invertY(final boolean invertY) {
		this.invertY = invertY;
	}

	protected void slice(final String name, final int x, int y, final int w, final int h) {
		if (invertY) {
			y = pixmap.getHeight() - y;
		}
		Gdx.app.debug(getClass().getSimpleName(), String.format("Slice: %s: [%d,%d] [%dx%d]", name, x, y, w, h));
		final TextureRegion r = new TextureRegion(texture, x, y, w, h);
		r.setRegion(x, y, w, h);
		if (invertY) {
			r.flip(false, true);
		}
		final AtlasRegion s = atlas.findRegion(name);
		if (s == null) {
			atlas.addRegion(name, r);
		} else {
			Gdx.app.debug("Slicer", "Moving atlas region " + name);
			s.setRegion(r);
		}
	}

}
