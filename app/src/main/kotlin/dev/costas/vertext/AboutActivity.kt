package dev.costas.vertext

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.preference.PreferenceManager
import dev.costas.vertext.theme.VerTextTheme

class AboutActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val prefs = PreferenceManager.getDefaultSharedPreferences(this)

		setContent {
			VerTextTheme(
				darkTheme = when (
					prefs.getString("theme", "MODE_NIGHT_FOLLOW_SYSTEM")) {
					"MODE_NIGHT_NO" -> false
					"MODE_NIGHT_YES" -> true
					else -> isSystemInDarkTheme()
				}
			) {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					Greeting("Android")
				}
			}
		}
	}
}

@Composable
fun Greeting(name: String) {
	Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
	VerTextTheme {
		Greeting("Android")
	}
}