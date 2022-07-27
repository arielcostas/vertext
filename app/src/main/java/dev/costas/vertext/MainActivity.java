package dev.costas.vertext;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
	private TextView principal;

	private String currentTitle = "";
	private String currentContent = "";

	private final String COMMON_STYLE = "pre {font-size: 14px;}";
	private final String LIGHT_STYLE = COMMON_STYLE + " body { background-color: white; color: black; }";
	private final String DARK_STYLE = COMMON_STYLE + "body { background-color: black; color: white; }";

	/*
	 *  MENU
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mainmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (item.getItemId()) {
			case R.id.mainmenu_abrir:
				openFile();
				break;
			case R.id.mainmenu_theme:
				int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
				if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
					getPreferences(MODE_PRIVATE).edit().putBoolean("useDarkMode", false).commit();
					AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
				} else {
					getPreferences(MODE_PRIVATE).edit().putBoolean("useDarkMode", true).commit();
					AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
				}
		}
		return super.onOptionsItemSelected(item);
	}

	ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(),
			result -> {
				if (result.getResultCode() == RESULT_OK) {
					Intent data = result.getData();
					assert data != null;
					Uri fileUri = data.getData();
					InputStream is;
					var sb = new StringBuilder();
					try {
						is = getContentResolver().openInputStream(fileUri);
						Scanner scanner = new Scanner(is);
						while (scanner.hasNextLine()) {
							sb.append(scanner.nextLine());
							sb.append("\n");
						}
					} catch (IOException e) {
						setContent(Arrays.toString(e.getStackTrace()));
						return;
					}

					setTitle(getFilenameFromUri(fileUri));
					setContent(sb.toString());
				}
			}
	);

	protected void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putString("currentTitle", currentTitle);
		bundle.putString("currentContent", currentContent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		principal = (TextView) findViewById(R.id.principal);
		principal.setHorizontallyScrolling(true);

		// Dark mode
		boolean isDarkMode = getPreferences(MODE_PRIVATE).getBoolean("useDarkMode", false);
		if (isDarkMode) {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
		} else {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
		}

		// Handle intents
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		Uri uri;

		switch (action) {
			case Intent.ACTION_EDIT:
			case Intent.ACTION_VIEW:
				String entrada = intent.getStringExtra(Intent.EXTRA_TEXT);
				uri = Uri.parse(intent.getStringExtra(Intent.EXTRA_ORIGINATING_URI));
				setTitle(getFilenameFromUri(uri));
				setContent(Html.escapeHtml(entrada));
				break;
			case Intent.ACTION_SEND:
				uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
				String text = intent.getStringExtra(Intent.EXTRA_TEXT);

				if (uri != null) {
					setTitle(getFilenameFromUri(uri));
					try {
						AssetFileDescriptor afd = getContentResolver().openTypedAssetFileDescriptor(uri, type, null);
						StringBuilder sb = new StringBuilder();
						Scanner scanner = new Scanner(afd.createInputStream());
						while (scanner.hasNextLine()) {
							sb.append(scanner.nextLine() + "\n");
							sb.append("\n");
						}
						setContent(sb.toString());
					} catch (IOException e) {
						setContent(Arrays.toString(e.getStackTrace()));
						return;
					}
				} else {
					setTitle(getString(R.string.file_shared));
					setContent(text);
				}
				break;
			default:
				// On setting dark mode or rotating screen
				if (savedInstanceState != null) {
					String content = savedInstanceState.getString("currentContent");
					if (content == "") {
						clearContent();
					} else {
						setTitle(savedInstanceState.getString("currentTitle"));
						setContent(content);
					}
				} else { // Started from scrach
					clearContent();
				}
		}
	}

	private void openFile() {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.setType("text/*");
		mStartForResult.launch(intent);
	}

	private void setContent(CharSequence innerContent) {
		principal.setText(innerContent.toString().replaceAll("\t", "    "));
		principal.setLines(innerContent.toString().split("\n").length);
		this.currentContent = innerContent.toString();
	}

	private void clearContent() {
		principal.setText(getString(R.string.none_open));
		principal.setLines(1);
		currentContent = "";
		currentTitle = "";
	}

	public String getFilenameFromUri(Uri uri) {
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		if (cursor == null) {
			return getString(R.string.file_unknown);
		}

		int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
		cursor.moveToFirst();
		String name = cursor.getString(nameIndex);
		cursor.close();
		return name;
	}

	@Override
	public void setTitle(CharSequence title) {
		if (title == null || title == "") {
			this.currentTitle = "";
			super.setTitle("VerText");
		} else {
			this.currentTitle = title.toString();
			super.setTitle(title + " - VerText");
		}
	}
}