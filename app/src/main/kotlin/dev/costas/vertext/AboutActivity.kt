package dev.costas.vertext

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager
import dev.costas.vertext.ui.components.AboutPageItem
import dev.costas.vertext.ui.theme.VerTextTheme

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
					color = MaterialTheme.colorScheme.background
				) {
					Column() {
						Text(
							text = getString(R.string.menu_about),
							style = MaterialTheme.typography.headlineMedium,
							modifier = Modifier
								.padding(bottom = 18.dp, top = 18.dp)
								.align(CenterHorizontally)
						)

						items.forEach {
							AboutPageItem(
								stringResource(it.title),
								it.subtitle,
								painterResource(id = it.icon)
							) {
								val intent =
									Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
								startActivity(intent)
							}
						}

					}
				}
			}
		}
	}

	companion object {
		data class AboutItem(
			@StringRes val title: Int,
			val subtitle: String,
			@DrawableRes val icon: Int,
			val url: String
		)

		val items = listOf<AboutItem>(
			AboutItem(
				R.string.about_sourcecode,
				"GitHub",
				R.drawable.ic_code_24,
				"https://github.com/arielcostas/vertext"
			),
			AboutItem(
				R.string.about_translate,
				"Crowdin",
				R.drawable.ic_translate_24,
				"https://crowdin.com/project/vertext"
			)
		)
	}


}

