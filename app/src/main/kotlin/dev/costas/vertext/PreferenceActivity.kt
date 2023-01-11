package dev.costas.vertext

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceFragmentCompat
import dev.costas.vertext.data.getDarkModeValue
import dev.costas.vertext.databinding.FragmentPreferencesBinding
import kotlinx.coroutines.launch

class PreferenceActivity : AppCompatActivity(R.layout.activity_preferences) {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		supportFragmentManager.beginTransaction()
			.setReorderingAllowed(true)
			.add(R.id.fragment_container_view, SettingsFragment::class.java, savedInstanceState)
			.commit()
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> {
				finish()
			}

			else -> super.onOptionsItemSelected(item)
		}
		return super.onOptionsItemSelected(item)
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

		_binding = FragmentPreferencesBinding.bind(view)
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

			val nightMode =
				getDarkModeValue(sharedPreferences.getString(key, "MODE_NIGHT_FOLLOW_SYSTEM")!!)
			AppCompatDelegate.setDefaultNightMode(nightMode)
		}
	}
}