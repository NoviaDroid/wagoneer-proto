package ca.informi.gdx.assets.loaders.resolvers;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

public class ChainedFileHandleResolver implements FileHandleResolver {

	private final ArrayList<FileHandleResolver> delegates;

	public ChainedFileHandleResolver(final FileHandleResolver... delegates) {
		this.delegates = new ArrayList<FileHandleResolver>(Arrays.asList(delegates));
	}

	@Override
	public FileHandle resolve(final String fileName) {
		FileHandle handle = null;
		for (final FileHandleResolver resolver : delegates) {
			handle = resolver.resolve(fileName);
			if (handle.exists()) return handle;
		}
		return handle;
	}

}
