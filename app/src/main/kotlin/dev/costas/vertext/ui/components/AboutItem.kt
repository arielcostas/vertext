package dev.costas.vertext.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.costas.vertext.R
import dev.costas.vertext.ui.theme.VerTextTheme

@Composable
fun AboutPageItem(title: String, subtitle: String, icon: Painter, clickListener: () -> Unit) {
	VerTextTheme() {
		Row(
			modifier = Modifier
				.clickable { clickListener() }
				.fillMaxWidth()
				.wrapContentHeight()
				.background(MaterialTheme.colorScheme.background)
				.padding(vertical = 12.dp, horizontal = 24.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Icon(
				painter = icon,
				contentDescription = "Source code icon",
				tint = MaterialTheme.colorScheme.onBackground
			)
			Column(
				Modifier.padding(start = 16.dp)
			) {

				Text(
					text = title,
					fontWeight = FontWeight.Medium,
					color = MaterialTheme.colorScheme.onBackground,
				)
				Text(
					text = subtitle,
					fontWeight = FontWeight.Light,
					color = MaterialTheme.colorScheme.onBackground,
				)
			}
		}
	}
}

@Preview(
	uiMode = UI_MODE_NIGHT_NO,
	name = "Light Mode"
)
@Preview(
	uiMode = UI_MODE_NIGHT_YES,
	name = "Dark Mode"
)
@Composable
fun AboutItemDarkPreview() {
	AboutPageItem(
		title = "GitHub",
		subtitle = "https://github.com",
		icon = painterResource(id = R.drawable.ic_code_24)
	) {}
}