package ca.informi.wagoneer.android;

import android.os.Bundle;
import ca.informi.wagoneer.Wagoneer;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Wagoneer().getApplicationListener(), config);
	}
}
