package ca.informi.gdx.assets.loaders.resolvers;

import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

public class PrefixedAbsoluteFileHandleResolver extends AbsoluteFileHandleResolver {

	protected final String prefix;

	public PrefixedAbsoluteFileHandleResolver(final String prefix) {
		this.prefix = prefix + (prefix.endsWith("/") ? "" : "/");
	}

	@Override
	public FileHandle resolve(final String fileName) {
		return super.resolve(fileName.startsWith(prefix) ? fileName : prefix + fileName);
	}
}
