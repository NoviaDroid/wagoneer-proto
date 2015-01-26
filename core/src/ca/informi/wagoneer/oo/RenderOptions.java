package ca.informi.wagoneer.oo;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;

public class RenderOptions {

	public Batch batch;
	public OrthographicCamera camera;

	public RenderOptions(final Batch batch, final OrthographicCamera camera) {
		super();
		this.batch = batch;
		this.camera = camera;
	}

}
