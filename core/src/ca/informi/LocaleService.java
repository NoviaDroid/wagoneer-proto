package ca.informi;

import java.util.Locale;

import com.badlogic.gdx.utils.I18NBundle;

public class LocaleService implements Service {

	private Locale currentLocale;

	@Override
	public void adding(final ApplicationDelegate delegate) {
		delegate.setLocale(currentLocale);
	}

	@Override
	public void removing(final ApplicationDelegate delegate) {
		delegate.setLocale(null);
	}

	@Override
	public void start() {
		currentLocale = Locale.getDefault();
		I18NBundle.setSimpleFormatter(true);
	}

	@Override
	public void stop() {
		currentLocale = null;
	}

}
