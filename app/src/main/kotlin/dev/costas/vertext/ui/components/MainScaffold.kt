package dev.costas.vertext.ui.components

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import dev.costas.vertext.R

@OptIn(
	ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class
)
@Composable
fun MainScaffold() {
	val content = """
        Hola que tal
        
        Esto es un ejemplo
        
        Tiene cinco líneas, dos de ellas vacías y tres con contenido, que puede pasar de una línea de tamaño
        Seis
        Siete
        Ocho
        Nueve
        Diez
        Once
    """.trimIndent()

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("VerText") },
				actions = {
					var isExpanded by remember { mutableStateOf(false) }

					IconButton(onClick = { /*TODO*/ }) {
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
							onClick = { },
							leadingIcon = {
								Icon(Icons.Outlined.Settings, "")
							},
							text = {
								Text(stringResource(R.string.menu_settings))
							})
						DropdownMenuItem(
							onClick = { },
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