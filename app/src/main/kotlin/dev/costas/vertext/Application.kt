package dev.costas.vertext

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PreferenceKeys.ROOT)

object PreferenceKeys {
	val ROOT = "settings"
	val NIGHT_MODE_PREFERENCE = stringPreferencesKey("NIGHT_MODE")
}
