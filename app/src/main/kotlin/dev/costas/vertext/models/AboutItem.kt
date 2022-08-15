package dev.costas.vertext.models

import android.graphics.drawable.Drawable
import android.view.View

data class AboutItem(
	val title: String,
	val subtitle: String,
	val icon: Drawable,
	val onClickListener: View.OnClickListener
)
