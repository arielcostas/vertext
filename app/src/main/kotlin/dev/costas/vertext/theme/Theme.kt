package dev.costas.vertext.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
	primary = Primary,
	onPrimary = Black,
	onBackground = White
)

private val LightColorScheme = lightColorScheme(
	primary = Primary,
	onPrimary = White,
	onBackground = Black
)

@Composable
fun VerTextTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	content: @Composable () -> Unit
) {
	val colorScheme = when {
		darkTheme -> DarkColorScheme
		else -> LightColorScheme
	}
	val view = LocalView.current
	if (!view.isInEditMode) {
		val window = (view.context as Activity).window

		val systemUiController = rememberSystemUiController()

		DisposableEffect(systemUiController, darkTheme) {
			systemUiController.setStatusBarColor(
				color = PrimaryDarker,
				darkIcons = false
			)

			onDispose {}
		}
	}

	MaterialTheme(
		colorScheme = colorScheme,
		typography = Typography,
		content = content
	)
}