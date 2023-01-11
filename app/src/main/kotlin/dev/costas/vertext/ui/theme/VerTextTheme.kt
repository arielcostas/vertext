package dev.costas.vertext.ui.theme

import android.os.Build
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
	primary = Primary,
	surfaceTint = Gray900,
	onPrimary = Gray900,
	background = Gray1000,
	onBackground = White,
	surface = Black,
	onSecondary = Gray900,
	secondary = Gray100
)

private val LightColorScheme = lightColorScheme(
	primary = Primary,
	surfaceTint = White,
	onPrimary = White,
	background = Gray50,
	onBackground = Black,
	surface = White,
	onSecondary = Gray100,
	secondary = Gray800
)

@Composable
fun VerTextTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	content: @Composable () -> Unit
) {
	val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
	Log.d("VerTextTheme", "dynamicColor: $dynamicColor")
	val colorScheme = when {
		dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
		dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
		darkTheme -> DarkColorScheme
		else -> LightColorScheme
	}

	MaterialTheme(
		colorScheme = colorScheme,
		typography = Typography,
		content = content
	)
}