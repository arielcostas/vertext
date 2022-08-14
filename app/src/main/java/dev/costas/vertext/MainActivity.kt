package dev.costas.vertext;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import dev.costas.vertext.fragments.MainFragment;

public class MainActivity extends AppCompatActivity implements FragmentChangeListener {
	public MainActivity() {
		super(R.layout.activity_main);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Log.d("Create", "mode: " + sharedPreferences.getString("theme", "NONE"));

		if (savedInstanceState == null) {
			switch(sharedPreferences.getString("theme", "MODE_NIGHT_FOLLOW_SYSTEM")) {
				case "MODE_NIGHT_FOLLOW_SYSTEM":
					AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
					break;
				case "MODE_NIGHT_NO":
					AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
					break;
				case "MODE_NIGHT_YES":
					AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
					break;
				default:
					Log.e("activityOnCreate", "unexpected mode option " + sharedPreferences.getString("theme", ""));
			}

			getSupportFragmentManager().beginTransaction()
					.setReorderingAllowed(true)
					.add(R.id.fragment_container_view, MainFragment.class, savedInstanceState)
					.commit();
		}
	}

	@Override
	public void setFragment(Fragment fragment) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container_view, fragment, fragment.toString())
				.addToBackStack(fragment.toString())
				.commit();
	}
}