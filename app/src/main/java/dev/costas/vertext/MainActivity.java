package dev.costas.vertext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Html;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
	private WebView webView;
	private TextView principal;

	private String currentContent;

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
				SharedPreferences sharedPreferences = getSharedPreferences("dev.costas.vertext", MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				String mode = sharedPreferences.getString("mode", "light");
				if (mode.equals("light")) {
					editor.putString("mode", "dark").commit();
				} else {
					editor.putString("mode", "light").commit();
				}
				refreshView();
				break;
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
						setWebViewContent(Arrays.toString(e.getStackTrace()));
						return;
					}

					setTitle(getFilenameFromUri(fileUri));
					setWebViewContent(sb.toString());
				}
			}
	);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		webView = (WebView) findViewById(R.id.vistaweb);
		principal = (TextView) findViewById(R.id.principal);

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
				setWebViewContent(Html.escapeHtml(entrada));
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
						setWebViewContent(sb.toString());
					} catch (IOException e) {
						setWebViewContent(Arrays.toString(e.getStackTrace()));
						return;
					}
				} else {
					setTitle(getString(R.string.file_shared));
					setWebViewContent(text);
				}
				break;
			default:
				clearWebViewContent();
		}
	}

	private void openFile() {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.setType("text/*");
		mStartForResult.launch(intent);
	}

	private void setWebViewContent(CharSequence innerContent) {
		String raw = "<html><head><style>" + getStyle() + "</style></head><body><pre>" + innerContent + "</pre></body></html>";
		String encoded = Base64.encodeToString(raw.getBytes(), Base64.NO_PADDING);
		webView.loadData(encoded, "text/html", "base64");
		this.currentContent = innerContent.toString();
	}

	private void clearWebViewContent() {
		String contenido = "<html><head><style>" + getStyle() + "</style></head><body><i>" + getString(R.string.none_open) + "</i></body></html>";
		String encoded = Base64.encodeToString(contenido.getBytes(), Base64.NO_PADDING);
		webView.loadData(encoded, "text/html", "base64");
	}

	private void refreshView() {
		if (currentContent == null) {
			clearWebViewContent();
		} else {
			setWebViewContent(currentContent);

		}
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

	private String getStyle() {
		String mode = getSharedPreferences("dev.costas.vertext", MODE_PRIVATE).getString("mode", "light");
		if (mode.equals("dark")) {
			return DARK_STYLE;
		}
		return LIGHT_STYLE;
	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title + " - VerText");
	}
}