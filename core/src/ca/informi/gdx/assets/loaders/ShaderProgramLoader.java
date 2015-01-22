package ca.informi.gdx.assets.loaders;

import ca.informi.gdx.assets.loaders.ShaderProgramLoader.ShaderProgramParameters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;

public class ShaderProgramLoader extends AsynchronousAssetLoader<ShaderProgram, ShaderProgramParameters> {

	public static class ShaderProgramFileHandles {
		public String vsFilename, fsFilename;
		public FileHandle vsFile, fsFile;
	}

	public static class ShaderProgramParameters extends AssetLoaderParameters<ShaderProgram> {
		public String vertexShaderFileName;
		public String fragmentShaderFileName;
	}

	FileHandleResolver resolver;
	ShaderProgramFileHandles handles = new ShaderProgramFileHandles();
	String vertexShaderContent;
	String fragmentShaderContent;

	public ShaderProgramLoader(final FileHandleResolver resolver) {
		super(resolver);
		this.resolver = resolver;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(final String fileName, final FileHandle file, final ShaderProgramParameters parameter) {
		return null;
	}

	@Override
	public void loadAsync(final AssetManager manager, final String fileName, final FileHandle file,
			final ShaderProgramParameters parameter) {
		if (parameter != null) {
			handles.vsFilename = parameter.vertexShaderFileName;
			handles.fsFilename = parameter.fragmentShaderFileName;
		}
		if (handles.vsFilename == null || handles.fsFilename == null) {
			// Load the file handle as JSON and extract the program parts from
			// there
			final FileHandle dir = file.parent();
			final JsonReader reader = new JsonReader() {
				@Override
				protected void string(final String name, final String value) {
					if (handles.vsFilename == null && name.startsWith("v")) {
						handles.vsFile = dir.child(value);
						handles.vsFilename = handles.vsFile.name();
					} else if (handles.fsFilename == null && name.startsWith("f")) {
						handles.fsFile = dir.child(value);
						handles.fsFilename = handles.fsFile.name();
					}
				}
			};
			reader.parse(file);
		}
		if (handles.vsFile == null) handles.vsFile = resolver.resolve(handles.vsFilename);
		if (handles.fsFile == null) handles.fsFile = resolver.resolve(handles.fsFilename);

		vertexShaderContent = handles.vsFile.readString();
		fragmentShaderContent = handles.fsFile.readString();
	}

	@Override
	public ShaderProgram loadSync(final AssetManager manager, final String fileName, final FileHandle file,
			final ShaderProgramParameters parameter) {
		final ShaderProgram program = new ShaderProgram(vertexShaderContent, fragmentShaderContent);
		if (!program.isCompiled()) {
			Gdx.app.log("ShaderProgramLoader", "Couldn't compile shader using " + parameter.vertexShaderFileName + ", "
					+ parameter.fragmentShaderFileName + ", " + fileName);
			Gdx.app.log("ShaderProgramLoader", "Error:\n" + program.getLog());
			program.dispose();
			return null;
		}
		return program;
	}
}
