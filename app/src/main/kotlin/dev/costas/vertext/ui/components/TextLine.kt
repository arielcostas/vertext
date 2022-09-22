package dev.costas.vertext.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import dev.costas.vertext.R

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun TextLine(lineNumber: Int, content: String, totalLines: Int) {
    val context = LocalContext.current

    val spacesBefore = totalLines.toString().length - lineNumber.toString().length - 1
    val spaces = (1..spacesBefore).joinToString("") { " " }

    Row() {
        Text(
            text = spaces + lineNumber.toString(),
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .background(MaterialTheme.colorScheme.onSecondary),
            color = MaterialTheme.colorScheme.secondary,
            style = TextStyle(fontFamily = FontFamily.Monospace)
        )

        Text(
            text = content,
            modifier = Modifier
                .combinedClickable(onClick = {}, onLongClick = {
                    val clipboardManager = context.getSystemService(
                        Context.CLIPBOARD_SERVICE
                    ) as ClipboardManager
                    clipboardManager.setPrimaryClip(ClipData.newPlainText("", content))
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
                        Toast
                            .makeText(context, R.string.line_copied, Toast.LENGTH_SHORT)
                            .show()

                })
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 2.dp),
            style = TextStyle(fontFamily = FontFamily.Monospace)
        )
    }
}