package ca.informi.wagoneer.oo.gameobject;

import java.util.Iterator;
import java.util.LinkedList;

import ca.informi.gdx.delegate.IntervalTimer.Interval;
import ca.informi.wagoneer.oo.RenderOptions;

import com.badlogic.gdx.utils.Disposable;

public class WagonHitches implements Renderable, Updatable, Disposable, Iterable<WagonHitch> {

	public final WagonHitch[] hitches = new WagonHitch[4];
	public LinkedList<WagonHitch> hitchesList = new LinkedList<WagonHitch>();

	public WagonHitches(final WagonObject owner) {
		for (int i = 0; i < 4; ++i) {
			if (owner.willAcceptConnection(i)) {
				hitches[i] = new WagonHitch(owner, i);
				hitchesList.add(hitches[i]);
			}
		}
	}

	@Override
	public void dispose() {
		for (int i = 0, n = hitches.length; i < n; ++i) {
			if (hitches[i] == null) {
				continue;
			}
			hitches[i].dispose();
			hitches[i] = null;
		}
		hitchesList.clear();
	}

	@Override
	public int getLayer() {
		return hitchesList.getFirst()
							.getLayer();
	}

	@Override
	public boolean isAlwaysVisible() {
		return hitchesList.getFirst()
							.isAlwaysVisible();
	}

	public boolean isHitched(final int i) {
		return hitches[i] != null && hitches[i].connected != null;
	}

	@Override
	public Iterator<WagonHitch> iterator() {
		return hitchesList.iterator();
	}

	public void render(final RenderOptions opts) {
		for (final WagonHitch hitch : hitchesList) {
			hitch.render(opts);
		}
	}

	@Override
	public void update(final Interval interval) {
		for (final WagonHitch hitch : hitchesList) {
			hitch.update(interval);
		}
	}

}
