package dev.costas.vertext.fragments

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.appbar.MaterialToolbar
import dev.costas.vertext.R

class SettingsFragment : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.findViewById<MaterialToolbar>(R.id.settings_toolbar)
        toolbar.setNavigationOnClickListener { v: View? -> requireActivity().onBackPressed() }
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