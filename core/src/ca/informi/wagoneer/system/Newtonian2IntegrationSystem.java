package ca.informi.wagoneer.system;

import ca.informi.wagoneer.component.Newtonian2;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class Newtonian2IntegrationSystem extends IteratingSystem {

	private ComponentMapper<Newtonian2> newtonianMapper = ComponentMapper.getFor(Newtonian2.class);

	public Newtonian2IntegrationSystem() {
		super(Family.getFor(Newtonian2.class));
	}

}
