package ca.informi.gdx.graphics.g2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

public class DFFont implements Disposable {

	public interface DFFontApplyUniforms {
		void applyUniforms(DFFont font, ShaderProgram program);
	}

	public final BitmapFont font;
	public final ShaderProgram fontShader;

	private Batch batchInUse;

	private final int baseSize;
	private float threshold = 0.5f;
	private final float smoothingBase = 1.f / 10.f;
	private float scale = 1.f;

	private final int uniformThreshold;
	private final int uniformSmoothing;
	private DFFontApplyUniforms applyUniformsDelegate;

	public DFFont(final int baseSize, final BitmapFont font, final ShaderProgram fontShader) {
		this.baseSize = baseSize;
		this.font = font;
		this.fontShader = fontShader;
		uniformThreshold = fontShader.getUniformLocation("u_threshold");
		uniformSmoothing = fontShader.getUniformLocation("u_smoothing");
	}

	public BitmapFont begin(final Batch batch) {
		batch.setShader(fontShader);
		applyUniforms();
		batchInUse = batch;
		return font;
	}

	@Override
	public void dispose() {
		// These are all passed in, and not owned by the font itself.
		// This makes use with AssetManager easy, and other use hard.
		// fontTexture.dispose(); fontShader.dispose(); font.dispose();
	}

	public void end() {
		batchInUse.setShader(null);
		batchInUse = null;
	}

	public void setApplyUniforms(final DFFontApplyUniforms applyUniforms) {
		applyUniformsDelegate = applyUniforms;
	}

	public void setSize(final int size) {
		scale = (float) size / (float) baseSize;
		font.setScale(scale);
		applyUniforms();
	}

	public void setWeight(int weight) {
		weight = MathUtils.clamp(weight, 100, 1000);
		final float thresholdMin = 0.28f;
		final float thresholdMax = 0.60f;
		threshold = 1.f - (float) weight / 1000;
		threshold = (threshold * (thresholdMax - thresholdMin)) + thresholdMin;
		applyUniforms();
	}

	protected void applyUniforms() {
		if (batchInUse != null) {
			batchInUse.flush();
		}
		fontShader.setUniformf(uniformThreshold, threshold);
		fontShader.setUniformf(uniformSmoothing, smoothingBase / scale);
		if (applyUniformsDelegate != null) {
			applyUniformsDelegate.applyUniforms(this, fontShader);
		}
	}

}
