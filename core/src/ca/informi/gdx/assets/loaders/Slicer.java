package ca.informi.gdx.assets.loaders;

import ca.informi.gdx.graphics.g2d.ProceduralTextureAtlas;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;

public abstract class Slicer {

	protected final ProceduralTextureAtlas atlas;

	private int cellsX, cellsY, cellX, cellY;
	private TextureRegion lastTRorigin;
	protected final Pixmap pixmap;
	private int stepX, stepY;
	protected final Texture texture;

	private boolean topDown;

	public Slicer(final ProceduralTextureAtlas atlas, final Texture texture, final Pixmap pixmap, final boolean topDown) {
		super();
		this.atlas = atlas;
		this.texture = texture;
		this.pixmap = pixmap;
		this.topDown = topDown;
	}

	protected void setSliceGrid(final int stepX, final int stepY, final int cellsX, final int cellsY) {
		this.stepX = stepX;
		this.stepY = stepY * (topDown ? -1 : 1);
		this.cellsX = cellsX;
		this.cellsY = cellsY;
	}

	protected void setTopDown(final boolean topDown) {
		this.topDown = topDown;
	}

	public abstract void slice();

	protected void slice(final String name) {
		TextureRegion r = new TextureRegion(lastTRorigin);
		cellX += 1;
		if (cellX >= cellsX) {
			cellX = 0;
			cellY += 1;
			if (cellY > cellsY) {
				throw new GdxRuntimeException("Grid overrun");
			}
		}
		r.setRegionX(r.getRegionX() + stepX * cellX);
		r.setRegionY(r.getRegionY() + stepY * cellY);
	}

	protected void slice(final String name, final int x, int y, final int w, final int h) {
		TextureRegion r = new TextureRegion(texture);
		if (topDown) {
			y = pixmap.getHeight() - y;
		}
		r.setRegion(x, y, w, h);
		atlas.addRegion(name, r);
		lastTRorigin = r;
		cellX = 0;
		cellY = 0;
	}

}
