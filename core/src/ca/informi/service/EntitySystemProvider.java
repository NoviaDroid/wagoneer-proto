package ca.informi.service;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.utils.Array;

public interface EntitySystemProvider {

	Array<EntitySystem> getSystems();

}
