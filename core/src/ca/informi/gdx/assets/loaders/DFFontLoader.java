package ca.informi.gdx.assets.loaders;

import java.io.BufferedReader;

import ca.informi.gdx.assets.loaders.ShaderProgramLoader.ShaderProgramParameters;
import ca.informi.gdx.graphics.g2d.DFFont;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;

public class DFFontLoader extends AsynchronousAssetLoader<DFFont, DFFontLoader.DFFontParameter> {

	/**
	 * Parameter to be passed to
	 * {@link AssetManager#load(String, Class, AssetLoaderParameters)} if
	 * additional configuration is necessary for the {@link BitmapFont}.
	 *
	 * @author mzechner
	 */
	static public class DFFontParameter extends AssetLoaderParameters<DFFont> {
		public String fragmentShader;
		public String fontName;
		public BitmapFontParameter fontParameter;
		public ShaderProgramParameters shaderParameters = null;
	}

	private static BitmapFontParameter defaultBitmapFontParameter = new BitmapFontParameter() {
		{
			minFilter = TextureFilter.Linear;
			magFilter = TextureFilter.Linear;
		}
	};

	private static DFFontParameter defaultParameter = new DFFontParameter() {
		{
			fontParameter = defaultBitmapFontParameter;
		}
	};

	BitmapFontData data;
	private int baseSize;
	private String shaderName;
	private AssetDescriptor<BitmapFont> fontDescriptor;
	private AssetDescriptor<ShaderProgram> shaderDescriptor;

	public DFFontLoader(final FileHandleResolver resolver) {
		super(resolver);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(final String fileName, final FileHandle file, final DFFontParameter paramIn) {
		final Array<AssetDescriptor> deps = new Array<AssetDescriptor>();

		final DFFontParameter param = paramIn != null ? paramIn : defaultParameter;
		final BitmapFontParameter bfParam = param.fontParameter != null ? param.fontParameter : defaultBitmapFontParameter;
		final ShaderProgramParameters sParam = param.shaderParameters != null ? param.shaderParameters : null;
		shaderName = sParam == null ? "shader/font-df.shader" : shaderName(fileName);
		fontDescriptor = new AssetDescriptor<BitmapFont>(file, BitmapFont.class, bfParam);
		shaderDescriptor = new AssetDescriptor<ShaderProgram>(shaderName, ShaderProgram.class, sParam);
		deps.add(fontDescriptor);
		deps.add(shaderDescriptor);

		return deps;
	}

	@Override
	public void loadAsync(final AssetManager manager, final String fileName, final FileHandle file, final DFFontParameter parameter) {
		final BufferedReader reader = file.reader(1024);
		try {
			final String infoLine = reader.readLine();
			final String[] info = infoLine.split(" ");
			if (info.length < 3) throw new GdxRuntimeException("Invalid header.");
			if (!info[2].startsWith("size=")) throw new GdxRuntimeException("Missing size.");
			baseSize = Integer.parseInt(info[2].substring(5));
		} catch (final Exception ex) {
			throw new GdxRuntimeException("Error parsing font file for base size: " + fileName, ex);
		} finally {
			StreamUtils.closeQuietly(reader);
		}
	}

	@Override
	public DFFont loadSync(final AssetManager manager, final String fileName, final FileHandle file, final DFFontParameter parameter) {
		final ShaderProgram program = manager.get(shaderDescriptor.fileName, ShaderProgram.class);
		final BitmapFont font = manager.get(fontDescriptor.fileName, BitmapFont.class);
		return new DFFont(baseSize, font, program);
	}

	private String shaderName(final String fileName) {
		return "dfCustomShader:" + fileName;
	}
}
