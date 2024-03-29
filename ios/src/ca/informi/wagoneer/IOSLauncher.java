package ca.informi.wagoneer;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

public class IOSLauncher extends IOSApplication.Delegate {
	public static void main(final String[] argv) {
		final NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(argv, null, IOSLauncher.class);
		pool.close();
	}

	@Override
	protected IOSApplication createApplication() {
		final IOSApplicationConfiguration config = new IOSApplicationConfiguration();
		return new IOSApplication(new Wagoneer(new InternalFileHandleResolver()), config);
	}
}