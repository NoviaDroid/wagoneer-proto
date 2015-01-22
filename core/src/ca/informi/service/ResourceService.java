package ca.informi.service;

import ca.informi.gdx.ApplicationDelegate;
import ca.informi.gdx.assets.loaders.DFFontLoader;
import ca.informi.gdx.assets.loaders.ShaderProgramLoader;
import ca.informi.gdx.assets.loaders.resolvers.PrefixedAbsoluteFileHandleResolver;
import ca.informi.gdx.graphics.g2d.DFFont;
import ca.informi.service.ResourcePackage.ResourcePackagePayload;
import ca.informi.service.ResourcePackage.ResourcePackagePayloadLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ResourceService extends ApplicationDelegate implements Service {

	private final AssetManager assetManager;
	private final FileHandleResolver resolver;
	private boolean disposed = false;

	public ResourceService() {
		final String assetBase = System.getProperty("assetBase");
		if (assetBase != null) {
			resolver = new PrefixedAbsoluteFileHandleResolver(assetBase);
			// resolver = new ChainedFileHandleResolver(new
			// PrefixedAbsoluteFileHandleResolver(loadExternal),
			// new InternalFileHandleResolver());
		} else {
			resolver = new InternalFileHandleResolver();
		}
		assetManager = new AssetManager(resolver);
		assetManager.setLoader(DFFont.class, new DFFontLoader(resolver));
		assetManager.setLoader(ResourcePackagePayload.class, new ResourcePackagePayloadLoader(resolver));
		assetManager.setLoader(ShaderProgram.class, new ShaderProgramLoader(resolver));
	}

	@Override
	public void adding(final ApplicationDelegate delegate) {
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		super.dispose();
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
		controller.suspend();
		assetManager.load(resource, klass, parameters);
	}

	@Override
	public void preUpdate() {
		if (assetManager.update()) {
			// Queue is empty
			controller.resume();
		}
	}

	@Override
	public void removing(final ApplicationDelegate delegate) {
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
		assetManager.clear();
	}

	public void unload(final String name) {
		if (disposed) return;

		try {
			assetManager.unload(name);
		} catch (final GdxRuntimeException ex) {
			Gdx.app.log("ResourceService", assetManager.getDiagnostics());
		}
	}
}
