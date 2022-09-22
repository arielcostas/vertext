package dev.costas.vertext.data

import dev.costas.vertext.BuildConfig
import dev.costas.vertext.R
import dev.costas.vertext.models.AboutItem

val AboutItems = listOf(
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
	),
	AboutItem(
		R.string.about_version,
		"Tag ${BuildConfig.VERSION_NAME}",
		R.drawable.ic_version_24,
		"https://github.com/arielcostas/vertext/releases/tag/${BuildConfig.VERSION_NAME}"
	),
	AboutItem(
		R.string.about_license_title,
		"Apache Public License 2.0",
		R.drawable.ic_license_24,
		"https://github.com/arielcostas/vertext/blob/main/LICENSE"
	)
)
