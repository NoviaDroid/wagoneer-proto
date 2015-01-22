package ca.informi.wagoneer.desktop;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePack {

	public static void main(final String[] args) {

		final Path root = Paths.get(".")
								.toAbsolutePath()
								.getParent()
								.getParent();
		final Path resourceRoot = root.resolve("android");
		final Path input = resourceRoot.resolve("assets-src/atlas");
		final Path output = resourceRoot.resolve("assets/image");

		final TexturePacker.Settings settings = new TexturePacker.Settings();
		// settings.alias = true;
		// settings.bleed = false;
		// settings.debug = true;

		TexturePacker.process(settings, input.toString(), output.toString(), "main");
	}

}
