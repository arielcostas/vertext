package dev.costas.vertext

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.preference.PreferenceManager
import dev.costas.vertext.data.getDarkModeValue
import dev.costas.vertext.ui.components.ScaffoldAbout
import dev.costas.vertext.ui.theme.VerTextTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AboutActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val nightModeFlow: Flow<String> = dataStore.data.map { preferences ->
			preferences[PreferenceKeys.NIGHT_MODE_PREFERENCE] ?: "MODE_NIGHT_FOLLOW_SYSTEM"
		}

		val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
		val spDarkMode = sharedPreferences.getString("theme", "MODE_NIGHT_FOLLOW_SYSTEM")!!

		setContent {
			val darkTheme: String by nightModeFlow.collectAsState(initial = spDarkMode)
			VerTextTheme(
				darkTheme = when (darkTheme) {
					"MODE_NIGHT_NO" -> false
					"MODE_NIGHT_YES" -> true
					else -> isSystemInDarkTheme()
				}
			) {
				ScaffoldAbout {
					finish()
				}
			}
		}

		val nightMode = getDarkModeValue(spDarkMode)
		AppCompatDelegate.setDefaultNightMode(nightMode)
	}

}

