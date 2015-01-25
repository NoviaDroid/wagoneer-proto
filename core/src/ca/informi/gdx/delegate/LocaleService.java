package ca.informi.gdx.delegate;

import java.util.Locale;

import com.badlogic.gdx.utils.I18NBundle;

public class LocaleService extends ApplicationDelegate {

	private Locale currentLocale;

	@Override
	public void added() {
		currentLocale = Locale.getDefault();
		I18NBundle.setSimpleFormatter(true);
	}

	@Override
	public void addedSibling(final ApplicationDelegate delegate) {
		delegate.setLocale(currentLocale);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void removed() {
		currentLocale = null;
	}

	@Override
	public void removingSibling(final ApplicationDelegate delegate) {
		delegate.setLocale(null);
	}

}
