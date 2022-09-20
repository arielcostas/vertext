package dev.costas.vertext.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class AboutItem(
	@StringRes val title: Int,
	val subtitle: String,
	@DrawableRes val icon: Int,
	val url: String
)