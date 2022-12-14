package dev.costas.vertext.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.costas.vertext.R
import dev.costas.vertext.data.AboutItems

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ScaffoldAbout(onBackButtonPressed: () -> Unit) {
	val context = LocalContext.current
	Scaffold(topBar = {
		Surface(shadowElevation = 8.dp) {
			TopAppBar(colors = TopAppBarDefaults.smallTopAppBarColors(
				containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
				titleContentColor = MaterialTheme.colorScheme.onSurface,
				navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
			), title = { Text(stringResource(R.string.menu_about)) }, navigationIcon = {
				IconButton(onClick = { onBackButtonPressed() }) {
					Icon(Icons.Default.ArrowBack, "")
				}
			})
		}

	}, content = { innerPadding ->
		LazyColumn(
			modifier = Modifier
				.consumedWindowInsets(innerPadding)
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.onSecondary),
			contentPadding = innerPadding
		) {
			items(AboutItems) { it ->
				AboutPageItem(
					stringResource(it.title), it.subtitle, painterResource(id = it.icon)
				) {
					val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
					context.startActivity(intent)
				}
			}
		}
	})

}

