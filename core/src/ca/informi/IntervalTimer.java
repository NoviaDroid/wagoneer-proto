package ca.informi;

import ca.informi.service.Service;

import com.badlogic.gdx.utils.TimeUtils;

public class IntervalTimer extends ApplicationDelegate implements Service {

	public static class Interval {
		public float dt;
		public float absDt;
		public long runTime;
		public long absTime;
	}

	private long lastRunTime;
	private long lastAbsTime;
	private long timeBase;
	private final Interval interval = new Interval();

	@Override
	public void adding(final ApplicationDelegate delegate) {
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
	public void removing(final ApplicationDelegate delegate) {
	}

	@Override
	public void start() {
		timeBase = TimeUtils.nanoTime();
		lastRunTime = lastAbsTime = timeBase;
	}

	@Override
	public void stop() {
		lastRunTime = -1;
	}

}
