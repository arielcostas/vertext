package dev.costas.vertext.ui.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.startActivity
import dev.costas.vertext.AboutActivity
import dev.costas.vertext.PreferenceActivity
import dev.costas.vertext.R

@OptIn(
	ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class
)
@Composable
fun ScaffoldMain(title: String, content: String, onFileOpenAction: () -> Unit) {
	val context = LocalContext.current
	Scaffold(
		topBar = {
			TopAppBar(
				modifier = Modifier.background(MaterialTheme.colorScheme.onSurface),
				title = { Text(text = title) },
				actions = {
					var isExpanded by rememberSaveable { mutableStateOf(false) }

					IconButton(onClick = { onFileOpenAction() }) {
						Icon(painterResource(R.drawable.ic_openfile_24), "")
					}
					IconButton(onClick = { isExpanded = !isExpanded }) {
						Icon(Icons.Default.MoreVert, "")
					}
					DropdownMenu(
						expanded = isExpanded,
						onDismissRequest = { isExpanded = false }
					) {
						DropdownMenuItem(
							onClick = {
								val intent = Intent(context, PreferenceActivity::class.java)

								startActivity(context, intent, null)
							},
							leadingIcon = {
								Icon(Icons.Outlined.Settings, "")
							},
							text = {
								Text(stringResource(R.string.menu_settings))
							})
						DropdownMenuItem(
							onClick = {
								val intent = Intent(context, AboutActivity::class.java)

								startActivity(context, intent, null)
							},
							leadingIcon = {
								Icon(Icons.Outlined.Info, "")
							},
							text = {
								Text(stringResource(R.string.menu_about))
							})
					}
				}
			)
		},
		content = { innerPadding ->
			LazyColumn(
				modifier = Modifier
					.consumedWindowInsets(innerPadding)
					.fillMaxSize()
					.background(MaterialTheme.colorScheme.onSecondary),
				contentPadding = innerPadding
			) {
				itemsIndexed(content.split("\n")) { ln, text ->
					TextLine(ln + 1, text, content.length)
				}
			}
		}
	)

}