package dev.costas.vertext

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.preference.PreferenceManager
import dev.costas.vertext.data.getDarkModeValue
import dev.costas.vertext.ui.components.ScaffoldMain
import dev.costas.vertext.ui.theme.VerTextTheme
import dev.costas.vertext.viewmodels.ContentViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.io.InputStream
import java.util.*


class VertextActivity : ComponentActivity() {
	private val TAG = this::class.simpleName
	private val vm: ContentViewModel by viewModels()

	private val fileOpeningLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
		if (it != null) {
			val sb = StringBuilder()
			val inputStream: InputStream
			try {
				inputStream = contentResolver.openInputStream(it)!!

				val scanner = Scanner(inputStream)
				while (scanner.hasNextLine()) {
					sb.append(scanner.nextLine())
					sb.append("\n")
				}
				vm.content = sb.toString()
				vm.title = getFilenameFromUri(it)
			} catch (e: Exception) {
				vm.content = e.message.toString()
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val action = intent.action
		val type = intent.type
		val uri: Uri?

		Log.d(TAG, "Activity created with action $action")
		when (action) {
			Intent.ACTION_EDIT, Intent.ACTION_VIEW -> {
				uri = intent.data
				if (uri == null) {
					return
				}

				val afd = contentResolver.openTypedAssetFileDescriptor(uri, type!!, null)
				val sb = StringBuilder()
				val scanner = Scanner(afd!!.createInputStream())
				while (scanner.hasNextLine()) {
					sb.append(scanner.nextLine())
					sb.append("\n")
				}
				vm.content = sb.toString()
				afd.close()
				vm.title = getFilenameFromUri(uri)
			}
			Intent.ACTION_SEND -> {
				uri = if (Build.VERSION.SDK_INT >= 33) {
					intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
				} else {
					intent.getParcelableExtra(Intent.EXTRA_STREAM)
				}

				if (uri != null) {
					try {
						vm.title = getFilenameFromUri(uri)
						val afd = contentResolver.openTypedAssetFileDescriptor(uri, type!!, null)
						val sb = StringBuilder()
						val scanner = Scanner(afd!!.createInputStream())
						while (scanner.hasNextLine()) {
							sb.append(scanner.nextLine())
							sb.append("\n")
						}
						vm.content = sb.toString()
						afd.close()
					} catch (e: IOException) {
						vm.content = Arrays.toString(e.stackTrace)
						return
					}
				} else {
					vm.title = getString(R.string.file_unknown)
					vm.content = getString(R.string.error_reading)
				}
			}
			else -> {
				if (vm.title == "") {
					vm.title = "VerText"
					vm.content = getString(R.string.none_open)
				}

			}
		}

		val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
		intent.type = "text/*"

		val nightModeFlow: Flow<String> = dataStore.data.map { preferences ->
			preferences[PreferenceKeys.NIGHT_MODE_PREFERENCE] ?: "MODE_NIGHT_FOLLOW_SYSTEM"
		}

		val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
		val spDarkMode = sharedPreferences.getString("theme", "MODE_NIGHT_FOLLOW_SYSTEM")!!

		setContent {
			val darkTheme: String by nightModeFlow.collectAsState(initial = spDarkMode)
			VerTextTheme(
				darkTheme = when (darkTheme) {
					"MODE_NIGHT_NO" -> false
					"MODE_NIGHT_YES" -> true
					else -> isSystemInDarkTheme()
				}
			) {
				ScaffoldMain(
					vm.title,
					vm.content,
					{ fileOpeningLauncher.launch(arrayOf("text/*")) })
			}
		}

		val nightMode = getDarkModeValue(spDarkMode)
		AppCompatDelegate.setDefaultNightMode(nightMode)
	}

	private fun getFilenameFromUri(uri: Uri?): String {
		val cursor = contentResolver.query(uri!!, null, null, null, null)
			?: return getString(R.string.file_unknown)
		val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
		cursor.moveToFirst()
		val name = cursor.getString(nameIndex)
		cursor.close()
		return name
	}

}
