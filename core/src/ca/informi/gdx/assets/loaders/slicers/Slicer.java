package ca.informi.gdx.assets.loaders.slicers;

import ca.informi.gdx.graphics.g2d.ProceduralTextureAtlas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.StringBuilder;

public abstract class Slicer {

	protected ProceduralTextureAtlas atlas;
	protected boolean invertCoordinates;
	protected boolean invertTexture;
	protected Pixmap pixmap;
	protected Texture texture;

	public Slicer() {
	}

	public void setInvertCoordinates(final boolean invertCoordinates) {
		this.invertCoordinates = invertCoordinates;
	}

	public void setInvertTexture(final boolean invertTexture) {
		this.invertTexture = invertTexture;
	}

	public void setSliceParams(final ProceduralTextureAtlas atlas, final Texture texture, final Pixmap pixmap) {
		this.atlas = atlas;
		this.texture = texture;
		this.pixmap = pixmap;
	}

	public abstract void slice();

	protected void slice(final String name, final int x, int y, final int w, final int h) {
		if (invertCoordinates) {
			y = pixmap.getHeight() - y;
		}
		Gdx.app.debug(getClass().getSimpleName(), "Slice: " + name + ":" + x + "," + y + "/" + w + "," + h);
		final TextureRegion r = new TextureRegion(texture, x, invertTexture != invertCoordinates ? y - h : h, w, h);
		if (invertTexture) {
			r.flip(false, true);
		}

		final AtlasRegion ar = atlas.addRegion(name, r);

		// Find an unindexed region with the same name.
		final AtlasRegion base = atlas.findRegion(name, -1);
		if (base == ar) {
			// Not found. Is there an indexed region?
			if (atlas.findRegion(name, 0) != null) {
				// Yes, there is. Find the last index.
				for (int i = 1;; ++i) {
					if (atlas.findRegion(name, i) == null) {
						// Add at the unused index.
						ar.index = i;
						break;
					}
				}
			} // else leave unindexed.
		} else {
			// There was an unindexed region. Index them.
			base.index = 0;
			ar.index = 1;
		}

	}

	protected void sliceGrid(final String[] columns, final String[] rows, final int baseX, final int baseY, final int width,
			final int height, final int stepX, final int stepY) {
		for (int columnIndex = 0; columnIndex < columns.length; ++columnIndex) {
			for (int rowIndex = 0; rowIndex < rows.length; ++rowIndex) {
				final int x = baseX + columnIndex * stepX;
				final int y = baseY + rowIndex * stepY;
				final String name = new StringBuilder().append(rows[rowIndex])
														.append("_")
														.append(columns[columnIndex])
														.toString();
				slice(name, x, y, width, height);
			}
		}
	}

}
