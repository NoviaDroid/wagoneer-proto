package ca.informi.delegate;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public interface SpriteProvider {

	Array<TextureAtlas> getAtlases();

	Array<NamedTexture> getTextures();

}
