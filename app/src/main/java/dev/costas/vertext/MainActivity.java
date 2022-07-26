package dev.costas.vertext;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
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
        
        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(v -> {
            openFile();
        });

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        switch(action) {
            case Intent.ACTION_EDIT:
            case Intent.ACTION_VIEW:
                String entrada = intent.getStringExtra(Intent.EXTRA_TEXT);
                setWebViewContent(Html.escapeHtml(entrada));
                break;
            case Intent.ACTION_SEND:
                Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                String text = intent.getStringExtra(Intent.EXTRA_TEXT);

                if (uri != null) {
                    try {
                        AssetFileDescriptor afd =  getContentResolver().openTypedAssetFileDescriptor(uri, type, null);
                        StringBuilder sb =  new StringBuilder();
                        Scanner scanner = new Scanner(afd.createInputStream());
                        while(scanner.hasNextLine()) {
                            sb.append(scanner.nextLine() + "\n");
                        }
                        setWebViewContent(sb.toString());
                    } catch (FileNotFoundException e) {
                        setWebViewContent(e.getStackTrace().toString());
                        return;
                    } catch (IOException e) {
                        setWebViewContent(e.getStackTrace().toString());
                        return;
                    }
                } else {
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
}