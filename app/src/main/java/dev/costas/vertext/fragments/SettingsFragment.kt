package dev.costas.vertext.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.appbar.MaterialToolbar;

import dev.costas.vertext.R;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
	@Override
	public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
		setPreferencesFromResource(R.xml.preferences, rootKey);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		MaterialToolbar toolbar = view.findViewById(R.id.settings_toolbar);
		toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
	}

	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals("theme")) {

			switch (sharedPreferences.getString(key, "MODE_NIGHT_FOLLOW_SYSTEM")) {
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
					Log.e("onSharedPreferenceChanged", "unexpected new mode " + sharedPreferences.getString(key, ""));
			}
		}
	}
}
