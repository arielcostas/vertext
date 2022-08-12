package dev.costas.vertext.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.appbar.MaterialToolbar;

import dev.costas.vertext.R;

public class SettingsFragment extends PreferenceFragmentCompat {
	@Override
	public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
		setPreferencesFromResource(R.xml.preferences, rootKey);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		MaterialToolbar toolbar = view.findViewById(R.id.settings_toolbar);
		toolbar.setNavigationOnClickListener(v -> {
			getActivity().onBackPressed();
		});
	}
}
