package dev.costas.vertext;

import android.content.Intent;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mainmenu_abrir) {
            openFile();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title + " - VerText");
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    assert data != null;
                    Uri fileUri = data.getData();
                    InputStream is;
                    var sb = new StringBuilder();
                    try {
                        is = getContentResolver().openInputStream(fileUri);
                        Scanner scanner  =  new Scanner(is);
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

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        Uri uri;

        switch(action) {
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
                        AssetFileDescriptor afd =  getContentResolver().openTypedAssetFileDescriptor(uri, type, null);
                        StringBuilder sb =  new StringBuilder();
                        Scanner scanner = new Scanner(afd.createInputStream());
                        while(scanner.hasNextLine()) {
                            sb.append(scanner.nextLine() + "\n");
                            sb.append("\n");
                        }
                        setWebViewContent(sb.toString());
                    } catch (IOException e) {
                        setWebViewContent(Arrays.toString(e.getStackTrace()));
                        return;
                    }
                } else {
                    setTitle("Compartido");
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
        String noncoded = "<html><body><pre>" + innerContent + "</pre></body></html>";
        String encoded = Base64.encodeToString(noncoded.getBytes(), Base64.NO_PADDING);
        webView.loadData(encoded, "text/html", "base64");
    }

    private void clearWebViewContent() {
        String contenido = "<html><body><i>Ningún archivo abierto</i></body></html>";
        String encoded = Base64.encodeToString(contenido.getBytes(), Base64.NO_PADDING);
        webView.loadData(encoded, "text/html", "base64");
    }

    public String getFilenameFromUri(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return "Desconocido"; // TODO: Translations
        }

        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String name = cursor.getString(nameIndex);
        cursor.close();
        return name;
    }
}