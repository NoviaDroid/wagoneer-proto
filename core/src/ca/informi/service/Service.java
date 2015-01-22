package ca.informi.service;

import ca.informi.ApplicationDelegate;

public interface Service {

	void adding(ApplicationDelegate delegate);

	void removing(ApplicationDelegate delegate);

	void start();

	void stop();

}
