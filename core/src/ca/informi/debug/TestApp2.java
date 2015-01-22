package ca.informi.debug;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TestApp2 implements ApplicationListener {

	String[] paths = { "lofi_1bit_scifi.png", "oryx_roguelike_b_castle.png", "oryx_roguelike_b_graveyard.png",
			"oryx_roguelike_b_sewers.png", "preview_creatures.png", "lofi_extra_features.png", "oryx_roguelike_b_catacombs.png",
			"oryx_roguelike_b_landmark.png", "oryx_roguelike_b_sky.png", "preview_ships.png", "lofi_interface.png",
			"oryx_roguelike_b_cave.png", "oryx_roguelike_b_mountains.png", "oryx_roguelike_b_swamp.png", "preview_space.png",
			"lofi_scifi_v2.png", "oryx_roguelike_b_city.png", "oryx_roguelike_b_observatory.png", "oryx_roguelike_b_temple.png",
			"preview_stations.png", "lofi_scifi_v2_trans.png", "oryx_roguelike_b_desert.png", "oryx_roguelike_b_orchard.png",
			"oryx_roguelike_b_volcano.png", "mockup_1.png", "oryx_roguelike_b_discards.png", "oryx_roguelike_b_ruins.png",
			"oryx_roguelike_classes.png", "mockup_2.png", "oryx_roguelike_b_dungeon.png", "oryx_roguelike_b_salt_plains.png",
			"oryx_roguelike_portraits.png", "mockup_3.png", "oryx_roguelike_b_forest.png", "oryx_roguelike_b_sanctuary.png",
			"oryx_roguelike_skills.png" };

	@Override
	public void create() {
		for (final String path : paths) {
			Gdx.app.log("TestApp", "Loading: " + path);
			final Texture single = new Texture("solo/" + path);
			Gdx.app.log("TestApp", "Success: " + path);
			single.dispose();
		}
	}

	@Override
	public void dispose() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void render() {
	}

	@Override
	public void resize(final int width, final int height) {
	}

	@Override
	public void resume() {
	}

}
