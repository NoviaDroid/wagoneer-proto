package ca.informi.wagoneer.oo.gameobject;

import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.wagoneer.oo.gameobject.WagonConnections.WagonConnection;

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

	public void add(final WagonMessage message) {
		bus.add(message);
	}

	@Override
	public void dispose() {
		WagonMessage.releaseAll(bus);
		bus.clear();
	}

	@Override
	public void update(final Interval interval) {
		busClock += interval.dt;
		while (busClock > busClockInterval) {
			owner.busWillUpdate();
			busClock -= busClockInterval;
			for (final WagonConnection wc : owner.connections.connections) {
				if (wc.wagon == null) continue;
				for (final WagonMessage message : bus) {
					wc.wagon.receiveMessage(message.clone(wc.offsetX, wc.offsetY));
				}
			}
		}
	}

}
