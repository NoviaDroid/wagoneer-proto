package ca.informi.gdx.assets.loaders;

import ca.informi.gdx.graphics.g2d.ProceduralTextureAtlas;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public interface Slicer {

	void slice(ProceduralTextureAtlas atlas, Texture texture, Pixmap pixmap);
}
