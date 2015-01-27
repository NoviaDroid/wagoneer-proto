package ca.informi.wagoneer.oo.gameobject;

import ca.informi.gdx.delegate.IntervalTimer.Interval;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class WagonMessageBus implements Updatable, Disposable {
	private final Array<WagonMessage> bus = new Array<WagonMessage>();
	private float busClock = 0.f;
	private final float busClockInterval = 1.f / 10.f;
	private final WagonObject owner;

	public WagonMessageBus(final WagonObject owner) {
		this.owner = owner;
	}

	@Override
	public void dispose() {
		WagonMessage.releaseAll(bus);
		bus.clear();
	}

	public void receive(final WagonMessage message) {
		if (message.receive(this.owner)) {
			owner.delegates.handle(message);
			bus.add(message);
		}
	}

	@Override
	public void update(final Interval interval) {
		busClock += interval.dt;
		while (busClock > busClockInterval) {
			owner.busWillUpdate();
			busClock -= busClockInterval;
			for (int i = 0, n = bus.size; i < n; ++i) {
				final WagonMessage message = bus.get(i);
				for (final WagonHitch wh : owner.hitches) {
					if (!wh.isConnected()) {
						continue;
					}
					wh.connected.owner.messageBus.receive(WagonMessage.obtain(message, wh.offsetX, wh.offsetY));
				}
				bus.set(i, null);
				message.release();
			}
			bus.clear();
		}
	}

}
