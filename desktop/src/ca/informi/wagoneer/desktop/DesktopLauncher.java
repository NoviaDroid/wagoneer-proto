package ca.informi.wagoneer.desktop;

import ca.informi.gdx.assets.loaders.resolvers.PrefixedAbsoluteFileHandleResolver;
import ca.informi.wagoneer.Wagoneer;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(final String[] arg) {

		final String assetBase = System.getProperty("assetBase");
		FileHandleResolver resolver;
		if (assetBase != null) {
			resolver = new PrefixedAbsoluteFileHandleResolver(assetBase);
			// resolver = new ChainedFileHandleResolver(new
			// PrefixedAbsoluteFileHandleResolver(loadExternal),
			// new InternalFileHandleResolver());
		} else {
			resolver = new InternalFileHandleResolver();
		}

		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new Wagoneer(resolver), config);
	}

}
