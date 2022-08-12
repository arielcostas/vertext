package dev.costas.vertext;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import dev.costas.vertext.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {
	public MainActivity() {
		super(R.layout.activity_main);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		boolean useDarkMode = getPreferences(Context.MODE_PRIVATE).getBoolean("useDarkMode", false);
		boolean currentDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;

		if (useDarkMode != currentDarkMode) {
			AppCompatDelegate.setDefaultNightMode(useDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
		}

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.setReorderingAllowed(true)
					.add(R.id.fragment_container_view, MainFragment.class, savedInstanceState)
					.commit();
		}
	}
}