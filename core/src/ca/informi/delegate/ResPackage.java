package ca.informi.delegate;

import java.util.HashSet;
import java.util.Set;

import ca.informi.gdx.ApplicationDelegate;
import ca.informi.gdx.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetLoaderParameters.LoadedCallback;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ResPackage implements LoadedCallback, Disposable {

	public interface Ready {
		void onReady();
	}

	public static class ResHandle<T> {
		public T o;
		private final AssetDescriptor<T> descriptor;

		public ResHandle(final T object) {
			descriptor = null;
			o = object;
		}

		ResHandle(final AssetDescriptor<T> descriptor) {
			this.descriptor = descriptor;
		}
	}

	public static class ResourcePackagePayload {

		public final Set<ResHandle<Object>> completed;

		public ResourcePackagePayload(final Set<ResHandle<Object>> completed) {
			this.completed = completed;
		}
	};

	public static class ResourcePackagePayloadLoader extends
			SynchronousAssetLoader<ResourcePackagePayload, ResourcePackagePayloadParameter> {

		public ResourcePackagePayloadLoader(final FileHandleResolver resolver) {
			super(resolver);
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Array<AssetDescriptor> getDependencies(final String fileName, final FileHandle file,
				final ResourcePackagePayloadParameter parameter) {
			final Array<AssetDescriptor> array = new Array<AssetDescriptor>();
			for (final ResHandle<Object> handle : parameter.dependencies) {
				array.add(handle.descriptor);
			}
			return array;
		}

		@Override
		public ResourcePackagePayload load(final AssetManager assetManager, final String fileName, final FileHandle file,
				final ResourcePackagePayloadParameter parameter) {
			for (final ResHandle<Object> handle : parameter.dependencies) {
				handle.o = assetManager.get(handle.descriptor);
			}
			return new ResourcePackagePayload(parameter.dependencies);
		}

	}

	public static class ResourcePackagePayloadParameter extends AssetLoaderParameters<ResourcePackagePayload> {
		public Set<ResHandle<Object>> dependencies = new HashSet<ResHandle<Object>>();

		ResourcePackagePayloadParameter(final LoadedCallback loadedCallback) {
			this.loadedCallback = loadedCallback;
		}
	}

	protected boolean loadStarted;
	protected String name;
	protected Ready onReady;
	protected ApplicationDelegate owner;
	protected ResourcePackagePayloadParameter parameter = new ResourcePackagePayloadParameter(this);
	protected boolean ready;

	public <T> ResHandle<T> add(final String filename, final Class<T> klass) {
		return add(filename, klass, null);
	}

	@SuppressWarnings("unchecked")
	public <T> ResHandle<T> add(final String filename, final Class<T> klass, final AssetLoaderParameters<T> parameters) {
		final ResHandle<T> result = new ResHandle<T>(new AssetDescriptor<T>(filename, klass, parameters));
		parameter.dependencies.add((ResHandle<Object>) result);
		ready = false;
		return result;
	}

	@Override
	public void dispose() {
		final ResourceService resources = Controller.instance.get(ResourceService.class);
		resources.unload(name);
	}

	@Override
	public void finishedLoading(final AssetManager assetManager, final String fileName, final Class type) {
		if (this.ready == true) throw new GdxRuntimeException("finishedLoading called multiply");
		this.ready = true;
		if (onReady != null) Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				onReady.onReady();
			}
		});
	}

	public boolean isReady() {
		return ready;
	}

	public ResPackage load(final ApplicationDelegate delegate) {
		if (loadStarted) { throw new GdxRuntimeException("Load multiply invoked on " + this); }
		loadStarted = true;
		owner = delegate;
		this.name = "package:" + delegate.toString();
		final ResourceService resources = Controller.instance.get(ResourceService.class);
		resources.load(name, ResourcePackagePayload.class, parameter);
		return this;
	}

	public Ready onReady(final Ready ready) {
		this.onReady = ready;
		return ready;
	}

}
