package ca.informi.delegate;

import ca.informi.gdx.ApplicationDelegate;

import com.badlogic.gdx.utils.TimeUtils;

public class IntervalTimer extends ApplicationDelegate {

	public static class Interval {
		public float absDt;
		public long absTime;
		public float dt;
		public long runTime;
	}

	private final Interval interval = new Interval();
	private long lastAbsTime;
	private long lastRunTime;
	private long timeBase;

	@Override
	public void added() {
		timeBase = TimeUtils.nanoTime();
		lastRunTime = lastAbsTime = timeBase;
	}

	@Override
	public void dispose() {

	}

	public Interval getInterval() {
		final long nanoTime = TimeUtils.nanoTime();
		final double nanoRunInterval = nanoTime - lastRunTime;
		final double nanoAbsInterval = nanoTime - lastAbsTime;
		interval.dt = lastRunTime > 0 ? (float) (nanoRunInterval / 1000000000.0) : 0.f;
		interval.absDt = (float) (nanoAbsInterval / 1000000000.0);
		interval.runTime += nanoRunInterval;
		interval.absTime = nanoTime - timeBase;

		lastRunTime = nanoTime;
		lastAbsTime = nanoTime;
		return interval;
	}

	@Override
	public void pause() {
		lastRunTime = -1;
	}

	@Override
	public void removed() {
		lastRunTime = -1;
	}

}
