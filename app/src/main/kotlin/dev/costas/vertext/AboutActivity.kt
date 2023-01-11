package dev.costas.vertext

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.preference.PreferenceManager
import dev.costas.vertext.ui.components.ScaffoldAbout
import dev.costas.vertext.ui.theme.VerTextTheme

class AboutActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val prefs = PreferenceManager.getDefaultSharedPreferences(this)

		setContent {
			VerTextTheme(
				darkTheme = when (
					prefs.getString("theme", "MODE_NIGHT_FOLLOW_SYSTEM")
				) {
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
	}

}

