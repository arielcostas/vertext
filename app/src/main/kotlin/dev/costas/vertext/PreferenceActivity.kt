package dev.costas.vertext

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.appbar.MaterialToolbar
import dev.costas.vertext.databinding.FragmentPreferencesBinding
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class PreferenceActivity : AppCompatActivity(R.layout.activity_preferences) {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		supportFragmentManager.beginTransaction()
			.setReorderingAllowed(true)
			.add(R.id.fragment_container_view, SettingsFragment::class.java, savedInstanceState)
			.commit()
	}
}

class SettingsFragment : PreferenceFragmentCompat(),
	SharedPreferences.OnSharedPreferenceChangeListener {
	private var _binding: FragmentPreferencesBinding? = null
	private val binding get() = _binding!!

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.preferences, rootKey)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// TODO: Set dark mode for this view

		_binding = FragmentPreferencesBinding.bind(view)
		val toolbar = binding.settingsToolbar
		toolbar.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
	}

	override fun onPause() {
		super.onPause()
		preferenceScreen.sharedPreferences!!.unregisterOnSharedPreferenceChangeListener(this)
	}

	override fun onResume() {
		super.onResume()
		preferenceScreen.sharedPreferences!!.registerOnSharedPreferenceChangeListener(this)
	}

	override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
		if (key == "theme") {
			sharedPreferences.getString(key, "MODE_NIGHT_FOLLOW_SYSTEM")
			lifecycleScope.launch {
				requireContext().dataStore.edit { preference ->
					preference[PreferenceKeys.NIGHT_MODE_PREFERENCE] =
						sharedPreferences.getString(key, null) ?: "MODE_NIGHT_FOLLOW_SYSTEM"
				}
			}

			val nightMode: Int =
				when (sharedPreferences.getString(key, "MODE_NIGHT_FOLLOW_SYSTEM")) {
					"MODE_NIGHT_FOLLOW_SYSTEM" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
					"MODE_NIGHT_NO" -> AppCompatDelegate.MODE_NIGHT_NO
					"MODE_NIGHT_YES" -> AppCompatDelegate.MODE_NIGHT_YES
					else -> {
						Log.e(
							"onSharedPreferenceChanged",
							"unexpected new mode " + sharedPreferences.getString(key, "")
						)
						AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
					}
				}
			AppCompatDelegate.setDefaultNightMode(nightMode)
		}
	}
}