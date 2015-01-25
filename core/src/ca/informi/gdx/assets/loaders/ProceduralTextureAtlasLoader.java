package ca.informi.gdx.assets.loaders;

import ca.informi.gdx.assets.loaders.ProceduralTextureAtlasLoader.ProceduralTextureAtlasParameter;
import ca.informi.gdx.graphics.g2d.ProceduralTextureAtlas;
import ca.informi.gdx.graphics.g2d.ProceduralTextureAtlas.ProceduralPage;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class ProceduralTextureAtlasLoader extends SynchronousAssetLoader<ProceduralTextureAtlas, ProceduralTextureAtlasParameter> {
	public static class ProceduralTextureAtlasParameter extends AssetLoaderParameters<ProceduralTextureAtlas> {
		private final ProceduralPage[] pages;

		public ProceduralTextureAtlasParameter(final ProceduralPage... pages) {
			this.pages = pages;
		}

	}

	public ProceduralTextureAtlasLoader(final FileHandleResolver resolver) {
		super(resolver);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Array<AssetDescriptor> getDependencies(final String fileName, final FileHandle file,
			final ProceduralTextureAtlasParameter parameter) {
		final Array<AssetDescriptor> result = new Array<AssetDescriptor>();
		for (final ProceduralPage page : parameter.pages) {
			result.add(new AssetDescriptor(page.imageFilename, Pixmap.class));
		}
		return result;
	}

	@Override
	public ProceduralTextureAtlas load(final AssetManager assetManager, final String fileName, final FileHandle file,
			final ProceduralTextureAtlasParameter parameter) {
		for (final ProceduralPage page : parameter.pages) {
			page.pixmap = assetManager.get(page.imageFilename, Pixmap.class);
			page.texture = new Texture(page.pixmap);
		}
		return new ProceduralTextureAtlas(parameter.pages);
	}

}
