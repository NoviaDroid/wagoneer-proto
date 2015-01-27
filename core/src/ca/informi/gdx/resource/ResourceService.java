package ca.informi.gdx.resource;

import ca.informi.gdx.assets.loaders.DFFontLoader;
import ca.informi.gdx.assets.loaders.ProceduralTextureAtlasLoader;
import ca.informi.gdx.assets.loaders.ShaderProgramLoader;
import ca.informi.gdx.graphics.g2d.DFFont;
import ca.informi.gdx.graphics.g2d.ProceduralTextureAtlas;
import ca.informi.gdx.resource.ResPackage.ResourcePackagePayload;
import ca.informi.gdx.resource.ResPackage.ResourcePackagePayloadLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ResourceService implements Disposable {

	private final AssetManager assetManager;
	private boolean disposed = false;
	private final FileHandleResolver resolver;

	public ResourceService(final FileHandleResolver resolver) {
		this.resolver = resolver;
		assetManager = new AssetManager(resolver);
		assetManager.setLoader(DFFont.class, new DFFontLoader(resolver));
		assetManager.setLoader(ResourcePackagePayload.class, new ResourcePackagePayloadLoader(resolver));
		assetManager.setLoader(ShaderProgram.class, new ShaderProgramLoader(resolver));
		assetManager.setLoader(ProceduralTextureAtlas.class, new ProceduralTextureAtlasLoader(resolver));
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		disposed = true;
	}

	public FileHandle file(final String fileName) {
		return resolver.resolve(fileName);
	}

	public void load(final AssetDescriptor<Object> descriptor) {
		assetManager.load(descriptor);
	}

	public <T> void load(final String resource, final Class<T> klass) {
		load(resource, klass, null);
	}

	public <T> void load(final String resource, final Class<T> klass, final AssetLoaderParameters<T> parameters) {
		if (assetManager.isLoaded(resource, klass)) return;
		assetManager.load(resource, klass, parameters);
	}

	public void unload(final String name) {
		if (disposed) return;

		try {
			assetManager.unload(name);
		} catch (final GdxRuntimeException ex) {
			Gdx.app.error("ResourceService", assetManager.getDiagnostics(), ex);
		}
	}

	public void update() {
		assetManager.update();
	}
}
