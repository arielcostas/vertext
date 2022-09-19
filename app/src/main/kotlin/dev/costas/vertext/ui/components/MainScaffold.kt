package dev.costas.vertext.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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