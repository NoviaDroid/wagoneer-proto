package ca.informi.wagoneer.oo.gameobject;

import com.badlogic.gdx.math.MathUtils;

public class AngleUtils {

	public static float absAngleDifferenceDegrees(final float a1, final float a2) {
		return absAngleDifferenceDomain(a1, a2, 360.f);
	}

	public static float absAngleDifferenceRadians(final float a1, final float a2) {
		return absAngleDifferenceDomain(a1, a2, MathUtils.PI2);
	}

	public static float angleDifferenceDegrees(final float a1, final float a2) {
		return angleDifferenceDomain(a1, a2, 360.f);
	}

	public static float angleDifferenceRadians(final float a1, final float a2) {
		return angleDifferenceDomain(a1, a2, MathUtils.PI2);
	}

	private static float abs(final float v) {
		return v >= 0.f ? v : -v;
	}

	private static float absAngleDifferenceDomain(final float a1, final float a2, final float domain) {
		return abs(angleDifferenceDomain(a1, a2, domain));
	}

	private static float angleDifferenceDomain(final float a1, final float a2, final float domain) {
		float r = a2 - a1;
		final float halfDomain = domain * 0.5f;
		if (r > halfDomain) {
			r -= domain;
		}
		if (r < -halfDomain) {
			r += domain;
		}
		return r;
	}
}
