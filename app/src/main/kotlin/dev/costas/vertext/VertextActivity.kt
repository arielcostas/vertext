package dev.costas.vertext

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager
import dev.costas.vertext.ui.components.MainScaffold
import dev.costas.vertext.ui.theme.VerTextTheme

class VertextActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        setContent {
            VerTextTheme(
                darkTheme = when (
                    prefs.getString("theme", "MODE_NIGHT_FOLLOW_SYSTEM")) {
                    "MODE_NIGHT_NO" -> false
                    "MODE_NIGHT_YES" -> true
                    else -> isSystemInDarkTheme()
                }
            ) {
                MainScaffold()
            }
        }
    }
}
