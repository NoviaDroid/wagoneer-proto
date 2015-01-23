package ca.informi.gdx.graphics.g2d;

import ca.informi.gdx.assets.loaders.Slicer;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ProceduralTextureAtlas extends TextureAtlas {

	public static class ProceduralPage {
		public final String imageFilename;
		public Pixmap pixmap;
		public final Slicer slicer;
		public Texture texture;

		public ProceduralPage(final String imageFilename, final Slicer slicer) {
			this.imageFilename = imageFilename;
			this.slicer = slicer;
		}

	}

	public ProceduralTextureAtlas(final ProceduralPage... pages) {
		for (final ProceduralPage page : pages) {
			page.slicer.slice(this, page.texture, page.pixmap);
		}
	}

}
