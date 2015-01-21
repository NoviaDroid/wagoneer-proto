package ca.informi;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;

public class FontDraw {

	public static void drawCenteredOn(final Batch batch, final BitmapFont font, final String string, int x, int y) {
		font.getBounds(string, bounds);
		x -= 0.5f * bounds.width;
		y += 0.5f * bounds.height;
		font.draw(batch, string, x, y);
	}

	private static final TextBounds bounds = new TextBounds();

}
