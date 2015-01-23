package ca.informi.delegate;

import ca.informi.gdx.ApplicationDelegate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class SpriteService extends ApplicationDelegate {

	ObjectMap<String, TextureRegion> regions = new ObjectMap<String, TextureRegion>();

	@Override
	public void addedSibling(final ApplicationDelegate delegate) {
		if (!(delegate instanceof SpriteProvider)) return;

		final SpriteProvider provider = (SpriteProvider) delegate;
		final Array<TextureAtlas> atlases = provider.getAtlases();
		if (atlases != null) {
			for (final TextureAtlas atlas : atlases) {
				for (final AtlasRegion region : atlas.getRegions()) {
					if (regions.containsKey(region.name)) {
						Gdx.app.error("SpriteService", region.name + " was already present");
					}
					regions.put(region.name, region);
				}
			}
		}

		final Array<NamedTexture> textures = provider.getTextures();
		if (textures != null) {
			for (final NamedTexture nt : textures) {
				if (regions.containsKey(nt.name)) {
					Gdx.app.error("SpriteService", nt.name + " was already present");
				}
				regions.put(nt.name, new TextureRegion(nt.texture));
			}
		}
	}

	public Sprite createSprite(final String name) {
		final TextureRegion region = regions.get(name);
		if (region == null) {
			Gdx.app.error("SpriteService", "No TextureRegion found for " + name);
			return null;
		}
		return new Sprite(region);
	}

	@Override
	public void dispose() {
		regions.clear();
	}

	@Override
	public void removingSibling(final ApplicationDelegate delegate) {
		if (!(delegate instanceof SpriteProvider)) return;

		final SpriteProvider provider = (SpriteProvider) delegate;
		final Array<TextureAtlas> atlases = provider.getAtlases();
		if (atlases != null) {
			for (final TextureAtlas atlas : atlases) {
				for (final AtlasRegion region : atlas.getRegions()) {
					regions.remove(region.name);
				}
			}
		}

		final Array<NamedTexture> textures = provider.getTextures();
		if (textures != null) {
			for (final NamedTexture nt : textures) {
				regions.put(nt.name, new TextureRegion(nt.texture));
			}
		}
	}

}
