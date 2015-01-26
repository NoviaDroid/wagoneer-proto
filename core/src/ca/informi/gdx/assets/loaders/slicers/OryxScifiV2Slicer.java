package ca.informi.gdx.assets.loaders.slicers;

import com.badlogic.gdx.utils.StringBuilder;

public class OryxScifiV2Slicer extends Slicer {

	public OryxScifiV2Slicer() {
		invertY(false);
	}

	@Override
	public void slice() {
		final int baseX = 8, baseY = 272, stepX = 32, stepY = 8, width = 8, height = 8;
		final String[] columns = new String[] { "white", "black", "red", "blue", "cyan", "yellow", "green" };
		final String[] rows = new String[] { "fighter", "interceptor", "shuttle", "runabout", "hunter", "hauler" };
		sliceGrid(columns, rows, baseX, baseY, width, height, stepX, stepY);
	}

	private void sliceGrid(final String[] columns, final String[] rows, final int baseX, final int baseY, final int width,
			final int height, final int stepX, final int stepY) {
		for (int columnIndex = 0; columnIndex < columns.length; ++columnIndex) {
			for (int rowIndex = 0; rowIndex < rows.length; ++rowIndex) {
				final int x = baseX + columnIndex * stepX;
				final int y = baseY + rowIndex * stepY;
				final String name = new StringBuilder().append(rows[rowIndex]).append("_").append(columns[columnIndex]).toString();
				slice(name, x, y, width, height);
			}
		}
	}
}
