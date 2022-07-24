package dev.costas.editexto;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Uri fileUri = data.getData();
                    InputStream is;
                    var sb = new StringBuffer();
                    try {
                        is = getContentResolver().openInputStream(fileUri);
                        Scanner scanner  =  new Scanner(is);
                        while (scanner.hasNextLine()) {
                            sb.append(scanner.nextLine() + "\n");
                        }
                    } catch (FileNotFoundException e) {
                        setWebViewContent(e.getStackTrace().toString());
                        return;
                    } catch (IOException e) {
                        setWebViewContent(e.getStackTrace().toString());
                        return;
                    }

                    setWebViewContent(sb.toString());
                }
            }
        }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.vistaweb);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            openFile();
        });

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        List<String> openingOptions = new ArrayList<String>();
        openingOptions.add(Intent.ACTION_SEND);
        openingOptions.add(Intent.ACTION_VIEW);
        openingOptions.add(Intent.ACTION_EDIT);

        if (openingOptions.contains(action) && type != null) {
            String entrada = intent.getStringExtra(Intent.EXTRA_TEXT);
            setWebViewContent(Html.escapeHtml(entrada));
        } else {
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
}