package ca.informi.service;

import java.util.HashSet;
import java.util.Set;

import ca.informi.gdx.ApplicationDelegate;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetLoaderParameters.LoadedCallback;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class ResourcePackage implements LoadedCallback, Disposable {

	public static class Handle<T> {
		private final AssetDescriptor<T> descriptor;
		public T o;

		Handle(final AssetDescriptor<T> descriptor) {
			this.descriptor = descriptor;
		}

		public Handle(final T object) {
			descriptor = null;
			o = object;
		}
	}

	public interface Ready {
		void onReady();
	}

	public static class ResourcePackagePayload {

		public final Set<Handle<Object>> completed;

		public ResourcePackagePayload(final Set<Handle<Object>> completed) {
			this.completed = completed;
		}
	};

	public static class ResourcePackagePayloadLoader extends
			SynchronousAssetLoader<ResourcePackagePayload, ResourcePackagePayloadParameters> {

		public ResourcePackagePayloadLoader(final FileHandleResolver resolver) {
			super(resolver);
		}

		@Override
		public Array<AssetDescriptor> getDependencies(final String fileName, final FileHandle file,
				final ResourcePackagePayloadParameters parameter) {
			final Array<AssetDescriptor> array = new Array<AssetDescriptor>();
			for (final Handle<Object> handle : parameter.waiting) {
				array.add(handle.descriptor);
			}
			return array;
		}

		@Override
		public ResourcePackagePayload load(final AssetManager assetManager, final String fileName, final FileHandle file,
				final ResourcePackagePayloadParameters parameter) {
			for (final Handle<Object> handle : parameter.waiting) {
				handle.o = assetManager.get(handle.descriptor);
			}
			return new ResourcePackagePayload(parameter.waiting);
		}

	}

	public static class ResourcePackagePayloadParameters extends AssetLoaderParameters<ResourcePackagePayload> {
		public Set<Handle<Object>> waiting = new HashSet<Handle<Object>>();

		ResourcePackagePayloadParameters(final LoadedCallback loadedCallback) {
			this.loadedCallback = loadedCallback;
		}
	}

	protected String name;
	protected Ready onReady;
	protected ApplicationDelegate owner;
	protected ResourcePackagePayloadParameters parameters = new ResourcePackagePayloadParameters(this);
	protected boolean ready;

	public <T> Handle<T> add(final String filename, final Class<T> klass) {
		final Handle<T> result = new Handle<T>(new AssetDescriptor<T>(filename, klass));
		parameters.waiting.add((Handle<Object>) result);
		ready = false;
		return result;
	}

	@Override
	public void dispose() {
		final ResourceService resources = owner.getController().services.get(ResourceService.class);
		resources.unload(name);
	}

	@Override
	public void finishedLoading(final AssetManager assetManager, final String fileName, final Class type) {
		this.ready = true;
		if (onReady != null)
			onReady.onReady();
	}

	public boolean isReady() {
		return ready;
	}

	public ResourcePackage load(final ApplicationDelegate delegate) {
		owner = delegate;
		this.name = "package:" + delegate.toString();
		final ResourceService resources = owner.getController().services.get(ResourceService.class);
		resources.load(name, ResourcePackagePayload.class, parameters);
		return this;
	}

	public Ready onReady(final Ready ready) {
		this.onReady = ready;
		return ready;
	}

}
