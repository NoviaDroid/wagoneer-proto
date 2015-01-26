package ca.informi.gdx.assets.loaders.slicers;

public class OryxScifiV2Slicer extends Slicer {

	public OryxScifiV2Slicer() {
		setInvertCoordinates(false);
		setInvertTexture(true);
	}

	@Override
	public void slice() {
		sliceShips();
		sliceAsteroids();
		sliceHitches();
	}

	private void sliceAsteroids() {
		final String[] columns = new String[] { "medium", "medium", "medium", "medium", "small", "small", "small", "small" };
		final String[] rows = new String[] { "asteroid" };
		sliceGrid(columns, rows, 128, 768, 8, 8, 8, 8);
	}

	private void sliceHitches() {
		slice("wagon_hitch", 48, 552, 8, 8);
		slice("wagon_hitch", 48, 568, 8, 8);
		slice("wagon_hitch", 48, 544, 8, 8);
		slice("wagon_hitch", 48, 560, 8, 8);
	}

	private void sliceShips() {
		final String[] columns = new String[] { "white", "black", "red", "blue", "cyan", "yellow", "green" };
		final String[] rows = new String[] { "fighter", "interceptor", "shuttle", "runabout", "hunter", "hauler" };
		sliceGrid(columns, rows, 8, 272, 8, 8, 32, 8);
	}
}
