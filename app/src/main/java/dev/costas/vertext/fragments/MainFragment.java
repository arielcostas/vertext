package dev.costas.vertext.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

import dev.costas.vertext.R;
import dev.costas.vertext.viewmodels.ContentViewModel;

public class MainFragment extends Fragment {
	private TextView textView;
	private AppCompatActivity activity;

	public MainFragment() {
		super(R.layout.fragment_main);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		textView = view.findViewById(R.id.principal);
		activity = (AppCompatActivity) getActivity();

		((MaterialToolbar)view.findViewById(R.id.main_toolbar)).setOnMenuItemClickListener(item -> {
			if (item.getItemId() == R.id.mainmenu_abrir) {
				openFile();
				return true;
			} else if (item.getItemId() == R.id.mainmenu_theme) {
				int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
				if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
					activity.getPreferences(Context.MODE_PRIVATE).edit().putBoolean("useDarkMode", false).commit();
					AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
				} else {
					activity.getPreferences(Context.MODE_PRIVATE).edit().putBoolean("useDarkMode", true).commit();
					AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
				}
				return true;
			} else {
				return false;
			}

		});

		Intent intent = activity.getIntent();
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
					try {
						setTitle(getFilenameFromUri(uri));
						AssetFileDescriptor afd = activity.getContentResolver().openTypedAssetFileDescriptor(uri, type, null);
						StringBuilder sb = new StringBuilder();
						Scanner scanner = new Scanner(afd.createInputStream());
						while (scanner.hasNextLine()) {
							sb.append(scanner.nextLine());
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
				ContentViewModel contentViewModel = new ViewModelProvider(activity).get(ContentViewModel.class);
				String title = contentViewModel.getTitle();
				String content = contentViewModel.getContent();
				Log.d("main", "titulo \"" + title + "\"");
				if (
						title == null || title.isEmpty() || title.trim().isEmpty() ||
						content == null || content.isEmpty() || content.trim().isEmpty()
				) {
					clearView();
				} else {
					setToolbarTitle(title + " - VerText");
					setTextviewContent(content);
				}
		}
	}

	private void openFile() {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.setType("text/*");
		mStartForResult.launch(intent);
	}

	ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(),
			result -> {
				if (result.getResultCode() == Activity.RESULT_OK) {
					Intent data = result.getData();
					assert data != null;
					Uri fileUri = data.getData();
					InputStream is;
					var sb = new StringBuilder();
					try {
						is = activity.getContentResolver().openInputStream(fileUri);
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

	public String getFilenameFromUri(Uri uri) {
		Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
		if (cursor == null) {
			return getString(R.string.file_unknown);
		}

		int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
		cursor.moveToFirst();
		String name = cursor.getString(nameIndex);
		cursor.close();
		return name;
	}

	private void setToolbarTitle(@NonNull CharSequence title) {
		MaterialToolbar toolbar = getView().findViewById(R.id.main_toolbar);
		toolbar.setTitle(title);
	}

	public void setTitle(@NonNull CharSequence title) {
		ContentViewModel contentViewModel = new ViewModelProvider(activity).get(ContentViewModel.class);
		if (title == "") {
			contentViewModel.setTitle("");
			setToolbarTitle(getContext().getApplicationInfo().name);
		} else {
			contentViewModel.setTitle(title.toString());
			setToolbarTitle(title + " - VerText");
		}
	}

	private void setTextviewContent(@NonNull CharSequence content) {
		textView.setText(content.toString().replaceAll("\t", "    "));
		textView.setLines(content.toString().split("\n").length);
	}

	public void setContent(@NonNull CharSequence content) {
		ContentViewModel contentViewModel = new ViewModelProvider(activity).get(ContentViewModel.class);
		contentViewModel.setContent(content.toString());

		setTextviewContent(content);
	}

	private void clearView() {
		setToolbarTitle("VerText");
		setTextviewContent(getString(R.string.none_open));
	}


}
