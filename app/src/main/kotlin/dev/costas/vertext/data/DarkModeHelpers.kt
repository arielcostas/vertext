package dev.costas.vertext.data

import androidx.appcompat.app.AppCompatDelegate

fun getDarkModeValue(mode: String): Int {
	return when (mode) {
		"MODE_NIGHT_FOLLOW_SYSTEM" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
		"MODE_NIGHT_NO" -> AppCompatDelegate.MODE_NIGHT_NO
		"MODE_NIGHT_YES" -> AppCompatDelegate.MODE_NIGHT_YES
		else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
	}
}