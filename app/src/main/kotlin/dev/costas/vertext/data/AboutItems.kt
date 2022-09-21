package dev.costas.vertext.data

import dev.costas.vertext.R
import dev.costas.vertext.models.AboutItem

val AboutItems = listOf<AboutItem>(
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
