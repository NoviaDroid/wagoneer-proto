package ca.informi.gdx.delegate;

import com.badlogic.gdx.utils.TimeUtils;

public class IntervalTimer {

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

	public void pause() {
		lastRunTime = -1;
	}

	public void resume() {

	}

	public void start() {
		timeBase = TimeUtils.nanoTime();
		lastRunTime = lastAbsTime = timeBase;
	}

	public void stop() {
		lastRunTime = -1;
	}

}
